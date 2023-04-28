package com.darzee.shankh.request.innerObjects;

import com.darzee.shankh.enums.OrderStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateOrderDetails {

    private Boolean isPriorityOrder;

    private OrderStatus status;

    private LocalDateTime trialDate;

    private LocalDateTime deliveryDate;

    private String inspiration;

    private String specialInstructions;

    private List<String> clothImageReferenceIds;
}
