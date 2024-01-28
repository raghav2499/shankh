package com.darzee.shankh.response;

import com.darzee.shankh.dao.OrderStitchOptionDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderStitchOptionResponse {

    private Long id;
    private Long stitchOptionId;
    private Long orderStitchOptionId;
    private List<String> values;

    public OrderStitchOptionResponse(OrderStitchOptionDAO orderStitchOption) {
        this.id = orderStitchOption.getId();
        this.orderStitchOptionId = orderStitchOption.getId();
        this.stitchOptionId = orderStitchOption.getStitchOptionId();
        this.values = orderStitchOption.getValues();
    }
}

