package com.darzee.shankh.response;

import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.dao.OrderAmountDAO;
import com.darzee.shankh.dao.OrderDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailResponse {

    private CustomerDetails customerDetails;
    private String orderStatus;
    private Boolean isPriorityOrder;
    private String orderType;
    private String trialDate;
    private String deliveryDate;
    private PaymentDetails paymentDetails;

    public OrderDetailResponse(CustomerDAO customer, OrderDAO order, OrderAmountDAO orderAmount) {
        this.customerDetails = new CustomerDetails(customer);
        this.orderStatus = order.getOrderStatus().getName();
        this.isPriorityOrder = order.getIsPriorityOrder();
        this.orderType = order.getOrderType().getName();
        this.trialDate = order.getTrialDate().toString();
        this.paymentDetails = new PaymentDetails(orderAmount);
    }
}
