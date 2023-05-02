package com.darzee.shankh.response;

import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.dao.OrderDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailResponse {

    private CustomerDetails customerDetails;

    private Long orderId;
    private String orderStatus;
    private Boolean isPriorityOrder;
    private String outfitType;
    private String trialDate;
    private String deliveryDate;
    private String customerImageLink;

    public OrderDetailResponse(CustomerDAO customer, OrderDAO order) {
        this.customerDetails = new CustomerDetails(customer);
        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus().getDisplayString();
        this.isPriorityOrder = Optional.ofNullable(order.getIsPriorityOrder()).orElse(Boolean.FALSE);
        this.outfitType = order.getOutfitType().getDisplayString();
        this.trialDate = order.getTrialDate().toString();
        this.deliveryDate = order.getDeliveryDate().toString();

    }
}
