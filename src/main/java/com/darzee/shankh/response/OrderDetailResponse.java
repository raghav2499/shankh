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

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailResponse {
    private Long orderId;
    private String orderStatus;
    private Boolean isPriorityOrder;
    private String outfitType;

    private Integer outfitTypeIndex;
    private String trialDate;
    private String deliveryDate;
    private String inspiration;
    private String specialInstructions;
    private String type;
    private List<String> clothImagesLink;

    private String outfitTypeImageLink;
    private OutfitMeasurementDetails measurementDetails;
    private OrderAmountDetails orderAmountDetails;
    private CustomerDetails customerDetails;

    private String message;


    public OrderDetailResponse(CustomerDAO customer, OrderDAO order, String customerProfilePicLnk,
                               OrderAmountDAO orderAmountDAO) {
        this.customerDetails = new CustomerDetails(customer, customerProfilePicLnk);
        this.orderAmountDetails = new OrderAmountDetails(orderAmountDAO);
        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus().getDisplayString();
        this.isPriorityOrder = Optional.ofNullable(order.getIsPriorityOrder()).orElse(Boolean.FALSE);
        this.outfitType = order.getOutfitType().getDisplayString();
        this.trialDate = order.getTrialDate().toString();
        this.deliveryDate = order.getDeliveryDate().toString();
        this.outfitTypeIndex = order.getOutfitType().getOrdinal();
        this.outfitTypeImageLink = order.getOutfitType().getImageLink();
    }

    public OrderDetailResponse(CustomerDAO customer, OrderDAO order, OutfitMeasurementDetails outfitMeasurementDetails,
                               OrderAmountDAO orderAmountDAO, List<String> clothImagesLink, String message) {
        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus().getDisplayString();
        this.isPriorityOrder = Optional.ofNullable(order.getIsPriorityOrder()).orElse(Boolean.FALSE);
        this.outfitType = order.getOutfitType().getDisplayString();
        this.outfitTypeIndex = order.getOutfitType().getOrdinal();
        this.outfitTypeImageLink = order.getOutfitType().getImageLink();
        this.trialDate = order.getTrialDate().toString();
        this.deliveryDate = order.getDeliveryDate().toString();
        this.type = order.getOrderType().getDisplayName();
        this.inspiration = order.getInspiration();
        this.specialInstructions = order.getSpecialInstructions();
        this.clothImagesLink = clothImagesLink;
        this.orderAmountDetails = new OrderAmountDetails(orderAmountDAO);
        this.customerDetails = new CustomerDetails(customer);
        this.measurementDetails = outfitMeasurementDetails;
        this.message = message;
    }

}
