package com.darzee.shankh.request.innerObjects;

import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.OutfitType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetails {
    private Long customerId;
    private Long boutiqueId;
    private Boolean isPriorityOrder;
    private LocalDateTime trialDate;
    private LocalDateTime deliveryDate;
    private OutfitType outfitType;
    private OrderType orderType;
    private String inspiration;
    private String specialInstructions;
    private Boolean isMeasurementOnPaper;
}
