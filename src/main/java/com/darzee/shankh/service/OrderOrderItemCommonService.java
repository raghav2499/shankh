package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderAmountDAO;
import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.dao.OrderItemDAO;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.OrderCreationRequest;
import com.darzee.shankh.request.innerObjects.OrderAmountDetails;
import com.darzee.shankh.request.innerObjects.OrderDetails;
import com.darzee.shankh.request.innerObjects.OrderItemDetailRequest;
import com.darzee.shankh.response.OrderSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

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
    public OrderSummary createOrder(OrderCreationRequest orderCreationRequest) {
        OrderDetails orderDetails = orderCreationRequest.getOrderDetails();
        OrderDAO orderDAO = orderService.findOrCreateNewOrder(orderDetails.getOrderId(),
                orderDetails.getBoutiqueId(), orderDetails.getCustomerId());
        List<OrderItemDetailRequest> alreadySavedItems = orderDetails.getOrderItems()
                .stream().filter(itemDetail -> itemDetail.getId() != null).collect(Collectors.toList());
        List<OrderItemDetailRequest> newItems = orderDetails.getOrderItems()
                .stream().filter(itemDetail -> itemDetail.getId() == null).collect(Collectors.toList());
        List<OrderItemDAO> newOrderItems = orderItemService.createOrderItems(newItems, orderDAO);

        List<OrderItemDAO> savedItems = new ArrayList<>();
        for (OrderItemDetailRequest itemDetailRequest : alreadySavedItems) {
            OrderItemDAO orderItemDAO = orderItemService.updateOrderItem(itemDetailRequest.getId(), itemDetailRequest);
            savedItems.add(orderItemDAO);
        }
        List<OrderItemDAO> allItems = new ArrayList<>();
        allItems.addAll(newOrderItems);
        allItems.addAll(savedItems);
        orderDAO.setOrderItems(allItems);
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        orderAmountDAO = orderService.setOrderAmountSpecificDetails(orderDetails.getOrderAmountDetails(), orderDAO);
        OrderSummary orderSummary = new OrderSummary(orderDAO.getId(), orderDAO.getInvoiceNo(),
                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(), orderDAO.getNonDeletedItems());
        return orderSummary;
    }

    @Transactional(REQUIRES_NEW)
    public OrderSummary createOrderItem(OrderCreationRequest orderCreationRequest) {
        OrderDetails orderDetails = orderCreationRequest.getOrderDetails();
        OrderDAO orderDAO = orderService.findOrCreateNewOrder(orderDetails.getOrderId(), orderDetails.getBoutiqueId(), orderDetails.getCustomerId());
        List<OrderItemDAO> orderItems = orderItemService.createOrderItems(orderDetails.getOrderItems(), orderDAO);
        OrderAmountDAO orderAmountDAO = orderService.setOrderAmountSpecificDetails(new OrderAmountDetails(), orderDAO);
        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(
                orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        if (orderDAO.getOrderAmount() == null) {
            orderDAO.setOrderAmount(orderAmountDAO);
            orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext()));
        }
        OrderSummary orderSummary = new OrderSummary(orderDAO.getId(), orderDAO.getInvoiceNo(),
                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(), orderItems);
        return orderSummary;
    }

    public OrderSummary updateOrderItem(Long orderItemId, OrderItemDetailRequest orderItemDetails) {
        OrderItemDAO updatedItem = orderItemService.updateOrderItem(orderItemId, orderItemDetails);
        OrderDAO orderDAO = updatedItem.getOrder();
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        OrderSummary orderItemSummary = new OrderSummary(orderDAO.getId(), orderDAO.getInvoiceNo(),
                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(), orderDAO.getOrderItems());
        return orderItemSummary;
    }

}
