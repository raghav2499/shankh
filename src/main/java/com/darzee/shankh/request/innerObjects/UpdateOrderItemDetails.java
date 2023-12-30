package com.darzee.shankh.request.innerObjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Valid
public class UpdateOrderItemDetails {
    @NotNull
    private Long id;
    private Boolean isPriorityOrder;
    private LocalDateTime trialDate;
    private LocalDateTime deliveryDate;
    private String inspiration;
    private Integer itemQuantity;
    private String specialInstructions;
    private List<String> clothImageReferenceIds;
    private List<UpdatePriceBreakupDetails> priceBreakupDetails;
}
