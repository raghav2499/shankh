package com.darzee.shankh.request.innerObjects;

import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.request.PriceBreakUpDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemDetailRequest {

    private Boolean isPriorityOrder;
    private LocalDateTime trialDate;
    @NotNull(message = "delivery_date cannot be null")
    private LocalDateTime deliveryDate;
    @NotNull(message = "outfit_type cannot be null")
    private Integer outfitType;
    @NotNull(message = "Either order_type is not present or is invalid")
    private OrderType orderType;
    private Integer itemQuantity = 1;
    private List<String> clothImageReferenceIds;
    private String inspiration;
    private String specialInstructions;
    @NotNull(message = "price_breakup cannot be null")
    private List<PriceBreakUpDetails> priceBreakup;
}
