package com.darzee.shankh.request.innerObjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.Valid;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Valid
public class UpdateOrderDetails {

    private Integer status;
    private Boolean deleteOrder;
}
