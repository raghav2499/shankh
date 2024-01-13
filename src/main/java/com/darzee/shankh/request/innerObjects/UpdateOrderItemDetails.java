package com.darzee.shankh.request.innerObjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Valid
public class UpdateOrderItemDetails {
    private Boolean isPriorityOrder;
    private LocalDateTime trialDate;
    private LocalDateTime deliveryDate;
    private String inspiration;
    private Integer itemQuantity;
    private String specialInstructions;
    private Long measurementRevisionId;
    private List<String> clothImageReferenceIds;
    private List<String> AudioReferenceIds;
    private List<UpdatePriceBreakupDetails> priceBreakupDetails;
}
