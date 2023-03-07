package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OrderStatus;
import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.OutfitType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDAO {
    private Long id;
    private LocalDateTime trialDate;
    private LocalDateTime deliveryDate;
    private OutfitType outfitType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isPriorityOrder;
    private OrderStatus orderStatus = OrderStatus.STITCHING_NOT_STARTED;
    private OrderType orderType;
    private String specialInstructions;
    private Boolean isDeleted = Boolean.FALSE;
    private String inspiration;
    private Boolean isMeasurementOnPaper;
    private BoutiqueDAO boutique;
    private CustomerDAO customer;
    public OrderDAO(LocalDateTime trialDate, LocalDateTime deliveryDate, OutfitType outfitType, String specialInstructions, String inspiration,OrderType orderType, Boolean isMeasurementOnPaper, BoutiqueDAO boutiqueDAO, CustomerDAO customerDAO) {
        this.trialDate = trialDate;
        this.deliveryDate = deliveryDate;
        this.outfitType = outfitType;
        this.specialInstructions = specialInstructions;
        this.orderType = orderType;
        this.inspiration = inspiration;
        this.isMeasurementOnPaper = isMeasurementOnPaper;
        this.boutique = boutiqueDAO;
        this.customer = customerDAO;
    }

}