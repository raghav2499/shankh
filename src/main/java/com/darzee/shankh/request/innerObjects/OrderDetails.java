package com.darzee.shankh.request.innerObjects;

import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.OutfitType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetails {

    @NotNull(message = "customer_id cannot be null")
    private Long customerId;
    @NotNull(message = "boutique_id cannot be null")
    private Long boutiqueId;
    private Boolean isPriorityOrder;
    @NotNull(message = "trail_date cannot be null")
    private LocalDateTime trialDate;
    @NotNull(message = "delivery_date cannot be null")
    private LocalDateTime deliveryDate;
    @NotNull(message = "Either outfit_type is not present or is invalid")
    private Long outfitType;
    @NotNull(message = "Either order_type is not present or is invalid")
    private OrderType orderType;
    private List<String> clothImageReferenceIds;
    private String inspiration;
    private String specialInstructions;
}
