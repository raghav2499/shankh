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
public class UpdateOrderDetails {

    private Integer status;
    private LocalDateTime trialDate;

    private LocalDateTime deliveryDate;

    private String inspiration;

    private String specialInstructions;

    private List<String> clothImageReferenceIds;

    private List<String> audioReferenceIds;
    private Boolean deleteOrder;
}
