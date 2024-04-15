package com.darzee.shankh.service;

import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.MeasurementRevisions;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        public ResponseEntity<OrderSummary> confirmOrderAndGenerateInvoice(Long boutiqueOrderId, Long boutiqueId,
                        OrderCreationRequest request) throws Exception {
                OrderDAO orderDAO = orderService.confirmOrder(boutiqueOrderId, boutiqueId, request);

                System.out.println("+++++++++++++++++OrderDAO++++++++++++++++" + orderDAO);
                OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
                System.out.println(
                                "+++++++++++++++++OrderAmountDAO++++++++++++++++" + orderAmountDAO.getAmountRecieved());
                OrderSummary summary = new OrderSummary(orderDAO.getBoutiqueOrderId(), orderDAO.getInvoiceNo(),
                                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(),
                                orderDAO.getNonDeletedItems());

                System.out.println("+++++++++++++++++summary++++++++++++" + summary);

                orderService.generateInvoiceV2(orderDAO);
                generateItemDetailPdfs(orderDAO);
                return new ResponseEntity<>(summary, HttpStatus.OK);
        }

        @Transactional
        public OrderSummary createOrder(OrderCreationRequest orderCreationRequest) {
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
                        OrderItemDAO orderItemDAO = orderItemService.updateOrderItem(itemDetailRequest.getId(),
                                        itemDetailRequest);
                        savedItems.add(orderItemDAO);
                }
                List<OrderItemDAO> allItems = collateAllItems(allOrderItems, savedItems);
                orderDAO.setOrderItems(allItems);
                OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
                orderAmountDAO = orderService.setOrderAmountSpecificDetails(orderDetails.getOrderAmountDetails(),
                                orderDAO);
                OrderSummary orderSummary = new OrderSummary(orderDAO.getBoutiqueOrderId(), orderDAO.getInvoiceNo(),
                                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(),
                                orderDAO.getNonDeletedItems());
                return orderSummary;
        }

        @Transactional
        public OrderSummary createOrderItem(OrderCreationRequest orderCreationRequest) {
                OrderDetails orderDetails = orderCreationRequest.getOrderDetails();
                OrderDAO orderDAO = orderService.findOrCreateNewOrder(orderDetails.getOrderId(),
                                orderDetails.getBoutiqueId(), orderDetails.getCustomerId());
                List<OrderItemDAO> orderItems = orderItemService.createOrderItems(orderDetails.getOrderItems(),
                                orderDAO);
                OrderAmountDAO orderAmountDAO = orderService.setOrderAmountSpecificDetails(new OrderAmountDetails(),
                                orderDAO);
                orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(
                                orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO,
                                                new CycleAvoidingMappingContext())),
                                new CycleAvoidingMappingContext());
                if (orderDAO.getOrderAmount() == null) {
                        orderDAO.setOrderAmount(orderAmountDAO);
                        orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO,
                                        new CycleAvoidingMappingContext())),
                                        new CycleAvoidingMappingContext());
                }
                OrderSummary orderSummary = new OrderSummary(orderDAO.getBoutiqueOrderId(), orderDAO.getInvoiceNo(),
                                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(), orderItems);
                return orderSummary;
        }

        public OrderSummary updateOrderItem(Long orderItemId, OrderItemDetailRequest orderItemDetails)
                        throws Exception {
                OrderItemDAO updatedItem = orderItemService.updateOrderItem(orderItemId, orderItemDetails);

                if (!Collections.isEmpty(orderItemDetails.getPriceBreakup())) {
                        List<PriceBreakupDAO> updatedPriceBreakup = priceBreakUpService
                                        .updatePriceBreakups(orderItemDetails.getPriceBreakup(), updatedItem);

                }
                OrderDAO orderDAO = orderService.updateOrderPostItemUpdation(updatedItem.getOrder().getId());

                OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
                orderService.generateInvoiceV2(orderDAO);
                System.out.println("order Amount :" + orderAmountDAO);
                Long orderNo = Optional.ofNullable(orderDAO.getBoutiqueOrderId()).orElse(orderDAO.getId());
                generateItemDetailPdf(updatedItem, orderDAO.getCustomer().getId(), orderDAO.getBoutiqueId(),
                                orderDAO.getBoutique().getName(), orderNo);
                OrderSummary orderItemSummary = new OrderSummary(orderDAO.getBoutiqueOrderId(), orderDAO.getInvoiceNo(),
                                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(),
                                orderDAO.getNonDeletedItems());
                System.out.println("Order Summary" + orderItemSummary);
                return orderItemSummary;
        }

        private List<OrderItemDAO> collateAllItems(List<OrderItemDAO> allItems, List<OrderItemDAO> updatedItems) {
                Map<Long, OrderItemDAO> allItemsMap = allItems.stream()
                                .collect(Collectors.toMap(OrderItemDAO::getId, orderItem -> orderItem));
                Map<Long, OrderItemDAO> updatedItemsMap = updatedItems.stream()
                                .collect(Collectors.toMap(OrderItemDAO::getId, orderItem -> orderItem));
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

        public void generateItemDetailPdfs(OrderDAO orderDAO) throws Exception {
                List<OrderItemDAO> orderItems = orderDAO.getOrderItems();
                Long orderNo = Optional.ofNullable(orderDAO.getBoutiqueOrderId()).orElse(orderDAO.getId());
                for (OrderItemDAO orderItem : orderItems) {
                        generateItemDetailPdf(orderItem, orderDAO.getCustomer().getId(),
                                        orderDAO.getBoutiqueId(), orderDAO.getBoutique().getName(), orderNo);
                }
        }

        public void generateItemDetailPdf(OrderItemDAO orderItemDAO, Long customerId, Long boutiqueId,
                        String boutiqueName,
                        Long orderNo) throws Exception {
                Map<String, List<OrderStitchOptionDetail>> groupedStitchOptions = stitchOptionService
                                .getOrderItemStitchOptions(orderItemDAO.getId());
                MeasurementRevisions measurementRevisions = measurementService.getMeasurementObject(customerId,
                                orderItemDAO.getId(),
                                orderItemDAO.getOutfitType());
                List<InnerMeasurementDetails> innerMeasurementDetailsList = new ArrayList<>();
                if (measurementRevisions != null) {
                        MeasurementRevisionsDAO revisionsDAO = mapper
                                        .measurementRevisionsToMeasurementRevisionDAO(measurementRevisions);
                        innerMeasurementDetailsList = measurementService.generateInnerMeasurementDetails(
                                        boutiqueId, orderItemDAO.getOutfitType(), revisionsDAO, true);
                }
                List<String> clothImageLinks = orderItemService.getClothImageLinks(orderItemDAO.getId());

                File itemDetailPdf = pdfGenerator.generateItemPdf(orderNo, boutiqueName, groupedStitchOptions,
                                innerMeasurementDetailsList, clothImageLinks, orderItemDAO);
                String fileUploadUrl = bucketService.uploadItemDetailsPDF(itemDetailPdf, orderItemDAO.getId());
        }
}
