package com.darzee.shankh.service;

import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.MeasurementRevisions;
import com.darzee.shankh.entity.Order;
import com.darzee.shankh.entity.OrderItem;
import com.darzee.shankh.enums.Language;
import com.darzee.shankh.enums.OrderItemStatus;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.OrderCreationRequest;
import com.darzee.shankh.request.innerObjects.OrderAmountDetails;
import com.darzee.shankh.request.innerObjects.OrderDetails;
import com.darzee.shankh.request.innerObjects.OrderItemDetailRequest;
import com.darzee.shankh.response.InnerMeasurementDetails;
import com.darzee.shankh.response.OrderStitchOptionDetail;
import com.darzee.shankh.response.OrderSummary;
import com.darzee.shankh.utils.pdfutils.PdfGenerator;
import io.jsonwebtoken.lang.Collections;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderOrderItemCommonService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private PriceBreakUpService priceBreakUpService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private StitchOptionsRepo stitchOptionsRepo;

    @Autowired
    private OrderAmountRepo orderAmountRepo;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private StitchOptionService stitchOptionService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private OrderStitchOptionsRepo orderStitchOptionsRepo;

    @Autowired
    private EntityManager entityManager;

    public ResponseEntity<OrderSummary> confirmOrderAndGenerateInvoice(Long orderId, Long boutiqueId, OrderCreationRequest request) throws Exception {
        OrderDAO orderDAO = orderService.confirmOrder(orderId, boutiqueId, request);
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        OrderSummary summary = new OrderSummary(orderDAO.getId(), orderDAO.getBoutiqueOrderId(), orderDAO.getInvoiceNo(),
                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(), orderDAO.getNonDeletedItems());
        orderService.generateInvoiceV2(orderDAO);
//        generateItemDetailPdfs(orderDAO);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

    @Transactional
    public OrderDAO createOrder(OrderCreationRequest orderCreationRequest) {

        OrderDetails orderDetails = orderCreationRequest.getOrderDetails();
        OrderDAO orderDAO = orderService.findOrCreateNewOrder(orderDetails.getOrderId(),
                orderDetails.getBoutiqueId(), orderDetails.getCustomerId());
        List<OrderItemDetailRequest> alreadySavedItems = orderDetails.getOrderItems()
                .stream().filter(itemDetail -> itemDetail.getId() != null).collect(Collectors.toList());
        List<OrderItemDetailRequest> newItems = orderDetails.getOrderItems()
                .stream().filter(itemDetail -> itemDetail.getId() == null).collect(Collectors.toList());
        List<OrderItemDAO> allOrderItems = orderItemService.createOrderItems(newItems, orderDAO);

        List<OrderItemDAO> savedItems = new ArrayList<>();
        for (OrderItemDetailRequest itemDetailRequest : alreadySavedItems) {
            OrderItemDAO orderItemDAO = orderItemService.updateOrderItem(itemDetailRequest.getId(), itemDetailRequest);
            if (!CollectionUtils.isEmpty(itemDetailRequest.getPriceBreakup())) {
                List<PriceBreakupDAO> updatedPriceBreakupDAOList = priceBreakUpService.updatePriceBreakups(itemDetailRequest.getPriceBreakup(), orderItemDAO);
                orderItemDAO.setPriceBreakup(updatedPriceBreakupDAOList);
            }
            savedItems.add(orderItemDAO);
        }

        List<OrderItemDAO> allItems = collateAllItems(allOrderItems, savedItems);
        orderDAO.setOrderItems(allItems);
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        orderAmountDAO = orderService.setOrderAmountSpecificDetails(orderDetails.getOrderAmountDetails(), orderDAO);
        orderDAO.setOrderAmount(orderAmountDAO);
        return orderDAO;
    }

    @Transactional
    public OrderDAO createOrderItem(OrderCreationRequest orderCreationRequest) {
        OrderDetails orderDetails = orderCreationRequest.getOrderDetails();
        OrderDAO orderDAO = orderService.findOrCreateNewOrder(orderDetails.getOrderId(), orderDetails.getBoutiqueId(), orderDetails.getCustomerId());
        List<OrderItemDAO> orderItems = orderItemService.createOrderItems(orderDetails.getOrderItems(), orderDAO);
        OrderAmountDAO orderAmountDAO = orderService.setOrderAmountSpecificDetails(new OrderAmountDetails(), orderDAO);
        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        if (orderDAO.getOrderAmount() == null) {
            orderDAO.setOrderAmount(orderAmountDAO);
            orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        }
        return orderDAO;
    }

    @Transactional
    public OrderDAO refresh(Long orderId) {
        Order refreshedOrder = orderRepo.findById(orderId)
                .map(order -> {
                    Session session = entityManager.unwrap(Session.class);
                    session.refresh(order); // Force refresh from database
                    return order;
                })
                .orElse(null);
        return mapper.orderObjectToDao(refreshedOrder, new CycleAvoidingMappingContext());
    }

    public OrderSummary updateOrderItem(Long orderItemId, OrderItemDetailRequest orderItemDetails) throws Exception {
        OrderItemDAO updatedItem = orderItemService.updateOrderItem(orderItemId, orderItemDetails);

        if (!Collections.isEmpty(orderItemDetails.getPriceBreakup())) {
            List<PriceBreakupDAO> updatedPriceBreakup = priceBreakUpService.updatePriceBreakups(orderItemDetails.getPriceBreakup(),
                    updatedItem);
            updatedItem.setPriceBreakup(updatedPriceBreakup);
        }

        boolean shouldUpdateLedger = doesLedgerNeedUpdate(updatedItem, orderItemDetails);
        OrderDAO orderDAO = updatedItem.getOrder();
        updateItemsInOrder(updatedItem, orderDAO);
        orderDAO = orderService.updateOrderPostItemUpdation(orderDAO, orderItemDetails.getAmountRefunded(), shouldUpdateLedger);
        orderService.generateInvoiceV2(orderDAO);
        Long orderNo = Optional.ofNullable(orderDAO.getBoutiqueOrderId()).orElse(orderDAO.getId());
//        generateItemDetailPdf(updatedItem, orderDAO.getCustomer().getId(), orderDAO.getBoutiqueId(), orderDAO.getBoutique().getName(), orderNo);
        OrderSummary orderItemSummary = new OrderSummary(orderDAO.getId(), orderDAO.getBoutiqueOrderId(),
                orderDAO.getInvoiceNo(), orderDAO.getOrderAmount().getTotalAmount(), orderDAO.getOrderAmount().getAmountRecieved(),
                orderDAO.getNonDeletedItems());
        return orderItemSummary;
    }

    private void updateItemsInOrder(OrderItemDAO updatedItem, OrderDAO orderDAO) {
        List<OrderItemDAO> orderItems = orderDAO.getOrderItems();
        orderItems.stream()
                .filter(item -> item.getId().equals(updatedItem.getId()))
                .findFirst()
                .ifPresent(item -> orderItems.set(orderItems.indexOf(item), updatedItem));
    }

    private List<OrderItemDAO> collateAllItems(List<OrderItemDAO> allItems, List<OrderItemDAO> updatedItems) {
        Map<Long, OrderItemDAO> allItemsMap = allItems.stream().collect(Collectors.toMap(OrderItemDAO::getId, orderItem -> orderItem));
        Map<Long, OrderItemDAO> updatedItemsMap = updatedItems.stream().collect(Collectors.toMap(OrderItemDAO::getId, orderItem -> orderItem));
        List<OrderItemDAO> updatedItemsList = new ArrayList<>();
        for (Map.Entry<Long, OrderItemDAO> orderItemEntrySet : allItemsMap.entrySet()) {
            if (updatedItemsMap.containsKey(orderItemEntrySet.getKey())) {
                updatedItemsList.add(updatedItemsMap.get(orderItemEntrySet.getKey()));
            } else {
                updatedItemsList.add(allItemsMap.get(orderItemEntrySet.getKey()));
            }
        }
        return updatedItemsList;
    }

    public String getItemDetailPdfLink(Long orderItemId, Language language) {
        Optional<OrderItem> item = orderItemRepo.findById(orderItemId);
        if(item.isPresent()) {
            OrderItemDAO orderItemDAO = mapper.orderItemToOrderItemDAO(item.get(), new CycleAvoidingMappingContext());
            OrderDAO orderDAO = orderItemDAO.getOrder();
            Long orderNo = Optional.ofNullable(orderDAO.getBoutiqueOrderId()).orElse(orderDAO.getId());
            try {
                String url = generateItemDetailPdf(orderItemDAO, orderDAO.getCustomerId(), orderDAO.getBoutiqueId(),
                        orderDAO.getBoutique().getName(), orderNo, language);
                return url;
            } catch(Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while generating item PDF");
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Order Item Id");
    }

    public String generateItemDetailPdf(OrderItemDAO orderItemDAO, Long customerId, Long boutiqueId, String boutiqueName,
                                      Long orderNo, Language language) throws Exception {
        Map<String, List<OrderStitchOptionDetail>> groupedStitchOptions =
                stitchOptionService.getOrderItemStitchOptions(orderItemDAO.getId());
        MeasurementRevisions measurementRevisions =
                measurementService.getMeasurementObject(null, customerId, orderItemDAO.getId(),
                        orderItemDAO.getOutfitType());
        List<InnerMeasurementDetails> innerMeasurementDetailsList = new ArrayList<>();
        if (measurementRevisions != null) {
            MeasurementRevisionsDAO revisionsDAO = mapper.measurementRevisionsToMeasurementRevisionDAO(measurementRevisions);
            innerMeasurementDetailsList = measurementService.generateInnerMeasurementDetails(boutiqueId, orderItemDAO.getOutfitType(), revisionsDAO, true);
        }
        List<String> clothImageLinks = orderItemService.getClothImageLinks(orderItemDAO.getId());
        List<String> audioInstructionLinks = orderItemService.getAudioInstructionLinks(orderItemDAO.getId());

        File itemDetailPdf = pdfGenerator.generateItemPdf(orderNo, boutiqueName, groupedStitchOptions,
                innerMeasurementDetailsList, clothImageLinks, audioInstructionLinks, orderItemDAO, language);
        String url = bucketService.uploadItemDetailsPDF(itemDetailPdf, orderItemDAO.getId(), language);
        return url;
    }

    /*
    Ledger should not be updated when drafted items are deleted
     */
    private boolean doesLedgerNeedUpdate(OrderItemDAO orderItemDAO, OrderItemDetailRequest orderItemDetails) {
        return !(OrderItemStatus.DRAFTED.equals(orderItemDAO.getOrderItemStatus())
                && Boolean.TRUE.equals(orderItemDetails.getIsDeleted()));
    }
}
