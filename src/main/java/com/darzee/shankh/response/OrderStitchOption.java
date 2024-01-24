package com.darzee.shankh.response;

import com.darzee.shankh.dao.OrderItemDAO;
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
public class OrderStitchOption {
    private Long stitchOptionId;
    private List<String> values;

    public OrderStitchOption(OrderStitchOptionDAO orderStitchOptionDAO) {
        this.stitchOptionId = orderStitchOptionDAO.getStitchOptionId();
        this.values = orderStitchOptionDAO.getValues();
    }
}

