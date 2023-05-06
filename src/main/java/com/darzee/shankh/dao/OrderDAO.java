package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OrderStatus;
import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.OutfitType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDAO {
    private Long id;

    private String invoiceNo;
    private LocalDateTime trialDate;
    private LocalDateTime deliveryDate;
    private OutfitType outfitType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isPriorityOrder = Boolean.FALSE;
    private OrderStatus orderStatus = OrderStatus.STITCHING_NOT_STARTED;
    private OrderType orderType;
    private String specialInstructions;
    private Boolean isDeleted = Boolean.FALSE;
    private String inspiration;
    private BoutiqueDAO boutique;
    private CustomerDAO customer;
    private OrderAmountDAO orderAmount;

    public OrderDAO(LocalDateTime trialDate, LocalDateTime deliveryDate, OutfitType outfitType,
                    String specialInstructions, String inspiration, OrderType orderType, String invoiceNo,
                    BoutiqueDAO boutiqueDAO, CustomerDAO customerDAO) {
        this.invoiceNo = invoiceNo;
        this.trialDate = trialDate;
        this.deliveryDate = deliveryDate;
        this.outfitType = outfitType;
        this.specialInstructions = specialInstructions;
        this.orderType = orderType;
        this.inspiration = inspiration;
        this.boutique = boutiqueDAO;
        this.customer = customerDAO;
    }

}