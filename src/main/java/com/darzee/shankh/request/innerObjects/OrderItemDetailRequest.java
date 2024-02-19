package com.darzee.shankh.request.innerObjects;

import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.request.PriceBreakUpDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemDetailRequest {

    private Long id;
    private Boolean isPriorityOrder;
    private LocalDateTime trialDate;
    private Integer itemStatus;
    private LocalDateTime deliveryDate;
    private Integer outfitType;
    private String outfitAlias;
    private Boolean isDeleted;
    private OrderType orderType;
    private Integer itemQuantity = 1;
    private List<String> clothImageReferenceIds;
    private List<String> audioReferenceIds;
    private String inspiration;
    private String specialInstructions;
    private Long measurementRevisionId;
    private List<Long> stitchOptionReferences;
    private List<PriceBreakUpDetails> priceBreakup;
}
