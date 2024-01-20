package com.darzee.shankh.request.innerObjects;

import com.darzee.shankh.enums.OrderType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Valid
public class UpdateOrderItemDetailRequest {
    private Boolean isPriorityOrder;
    private LocalDateTime trialDate;
    private LocalDateTime deliveryDate;
    private Integer outfitType;
    private OrderType orderType;
    private Integer itemQuantity;
    private List<String> clothImageReferenceIds;
    private List<String> audioReferenceIds;
    private String inspiration;
    private String specialInstructions;
    private Long measurementRevisionId;
    private List<Long> stitchOptionReferences;
    @Valid
    private List<UpdatePriceBreakupDetails> priceBreakup;
}
