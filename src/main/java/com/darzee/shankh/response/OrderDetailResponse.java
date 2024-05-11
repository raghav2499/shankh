package com.darzee.shankh.response;

import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.dao.OrderAmountDAO;
import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.dao.OrderItemDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailResponse {

    private Long orderId;
    private Long boutiqueOrderId;
    private String orderStatus;
    private List<OrderItemDetails> orderItemDetails = new ArrayList<>();
    private OrderAmountDetails orderAmountDetails;
    private CustomerDetails customerDetails;
    private Integer paymentMode;

    private String message;


    public OrderDetailResponse(OrderDAO order, List<Pair<OrderItemDAO, String>> orderItemOutfitLinkPairList,
                               String customerProfilePicLnk) {
        CustomerDAO customer = order.getCustomer();
        OrderAmountDAO orderAmountDAO = order.getOrderAmount();
        this.customerDetails = new CustomerDetails(customer, customerProfilePicLnk);
        this.orderAmountDetails = new OrderAmountDetails(orderAmountDAO);
        this.orderId = order.getId();
        this.boutiqueOrderId = order.getBoutiqueOrderId();
        this.orderStatus = order.getOrderStatus().getDisplayString();
        this.orderItemDetails = new ArrayList<>();
        for (Pair<OrderItemDAO, String> orderItemOutfitLinkPair : orderItemOutfitLinkPairList) {
            OrderItemDetails orderItemDetails = new OrderItemDetails(orderItemOutfitLinkPair.getFirst(),
                    orderItemOutfitLinkPair.getSecond());
            this.orderItemDetails.add(orderItemDetails);
        }
    }

    public OrderDetailResponse(CustomerDAO customer, OrderDAO order, OrderAmountDAO orderAmountDAO,
                               List<OrderItemDetails> orderItemDetails, Integer paymentMode, String message) {
        this.orderId = order.getId();
        this.boutiqueOrderId = order.getBoutiqueOrderId();
        this.orderStatus = order.getOrderStatus().getDisplayString();
        this.orderAmountDetails = new OrderAmountDetails(orderAmountDAO);
        this.customerDetails = new CustomerDetails(customer);
        this.orderItemDetails = orderItemDetails;
        this.paymentMode = paymentMode;
        this.message = message;
    }

}
