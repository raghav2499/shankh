package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderAmountDAO;
import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.dao.OrderItemDAO;
import com.darzee.shankh.dao.OrderStitchOptionDAO;
import com.darzee.shankh.dao.StitchOptionsDAO;
import com.darzee.shankh.entity.Order;
import com.darzee.shankh.entity.OrderStitchOptions;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.OrderAmountRepo;
import com.darzee.shankh.repo.OrderItemRepo;
import com.darzee.shankh.repo.OrderRepo;
import com.darzee.shankh.repo.OrderStitchOptionsRepo;
import com.darzee.shankh.repo.StitchOptionsRepo;
import com.darzee.shankh.request.CreateOrderItemRequest;
import com.darzee.shankh.request.CreateStitchOptionRequest;
import com.darzee.shankh.request.PriceBreakUpDetails;
import com.darzee.shankh.request.innerObjects.OrderDetails;
import com.darzee.shankh.request.innerObjects.OrderItemDetailRequest;
import com.darzee.shankh.request.innerObjects.StitchDetails;
import com.darzee.shankh.request.innerObjects.UpdateOrderItemDetails;
import com.darzee.shankh.response.CreateStitchResponse;
import com.darzee.shankh.response.OrderItemSummary;
import com.darzee.shankh.response.OrderStitchOption;
import com.darzee.shankh.response.OrderSummary;
import com.darzee.shankh.response.StitchOptionDetail;
import com.darzee.shankh.response.StitchSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderOrderItemCommonService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private StitchOptionsRepo stitchOptionsRepo;

    @Autowired
    private OrderAmountRepo orderAmountRepo;

    @Autowired
    private DaoEntityMapper mapper;
    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private OrderStitchOptionsRepo orderStitchOptionsRepo;

    @Transactional
    public OrderSummary createOrderItem(CreateOrderItemRequest createOrderItemRequest) {
        OrderDetails orderDetails = createOrderItemRequest.getOrderDetails();
        OrderDAO orderDAO = null;
        OrderAmountDAO orderAmountDAO = null;
        if (orderDetails.getOrderId() != null) {
            Optional<Order> order = orderRepo.findById(orderDetails.getOrderId());
            if (order.isPresent()) {
                orderDAO = mapper.orderObjectToDao(order.get(), new CycleAvoidingMappingContext());
                orderAmountDAO = orderDAO.getOrderAmount();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order ID");
            }
        } else {
            orderDAO = orderService.createNewOrder(orderDetails.getBoutiqueId(), orderDetails.getCustomerId());
            orderAmountDAO = new OrderAmountDAO();
            orderAmountDAO.setOrder(orderDAO);
        }
        List<OrderItemDAO> allOrderItems = orderItemService.createOrderItems(orderDetails.getOrderItems(), orderDAO);
        List<PriceBreakUpDetails> priceBreakUpDetailsList = orderDetails.getOrderItems().stream()
                .map(OrderItemDetailRequest::getPriceBreakup).flatMap(List::stream).collect(Collectors.toList());
        calculateOrderAmount(orderAmountDAO, priceBreakUpDetailsList);
        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(
                orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO,
                        new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        if (orderDAO.getOrderAmount() == null) {
            orderDAO.setOrderAmount(orderAmountDAO);
            orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext()));
        }
        OrderSummary orderSummary = new OrderSummary(orderDAO.getId(), orderDAO.getInvoiceNo(),
                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(), allOrderItems);
        return orderSummary;
    }

    public OrderItemSummary updateOrderItem(Long orderItemId, UpdateOrderItemDetails updateOrderItemDetails) {
        OrderItemDAO updatedItem = orderItemService.updateOrderItem(orderItemId, updateOrderItemDetails);
        OrderItemSummary orderItemSummary = new OrderItemSummary(updatedItem);
        return orderItemSummary;
    }

    private void calculateOrderAmount(OrderAmountDAO orderAmountDAO, List<PriceBreakUpDetails> priceBreakups) {
        Double newItemsPrice = priceBreakups.stream().mapToDouble(PriceBreakUpDetails::getValue).sum();
        Double updatedAmount = orderAmountDAO.getTotalAmount() + newItemsPrice;
        orderAmountDAO.setTotalAmount(updatedAmount);
    }

   
    @Transactional
    public StitchSummary createStitchOptions(CreateStitchOptionRequest createStitchOptionRequest) {
        Long orderItemId = createStitchOptionRequest.getOrderItemId();
        List<StitchDetails> stitchDetails = createStitchOptionRequest.getStitchDetails();
        List<OrderStitchOptionDAO> orderStitchOptionDAOs = new ArrayList<>();

        for (StitchDetails stitchDetail : stitchDetails) {
            //generate a 6digit reference id for the stitch option using math rand
            String stitchOptionReferenceId = String.valueOf((int) (Math.random() * (999999 - 100000 + 1) + 100000));
            orderStitchOptionDAOs.add(new OrderStitchOptionDAO(stitchDetail.getStitchOptionId(), stitchDetail.getValues(), orderItemId, stitchOptionReferenceId));
        }

        StitchSummary stitchSummary = new StitchSummary(orderItemId,orderStitchOptionDAOs);
        // System.out.println(stitchSummary.toString());
        // print orderStitchOptionDAOs list
        for (OrderStitchOptionDAO orderStitchOptionDAO : orderStitchOptionDAOs) {
            // System.out.println(orderStitchOptionDAO.toString());
            //save in repo and use a mapper to convert to response object
            orderStitchOptionsRepo.save(mapper.createStitchOptionDaoToObject(orderStitchOptionDAO, new CycleAvoidingMappingContext()));
        }

        return stitchSummary;
    }

}
