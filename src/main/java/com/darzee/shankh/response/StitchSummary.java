package com.darzee.shankh.response;

import com.darzee.shankh.dao.OrderStitchOptionDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class StitchSummary {
    List<OrderStitchOptionResponse> orderStitchOptionList = new ArrayList<>();

    public StitchSummary(List<OrderStitchOptionDAO> orderStitchOptionDAOs) {
        for (OrderStitchOptionDAO orderStitchOption : orderStitchOptionDAOs) {
            orderStitchOptionList.add(new OrderStitchOptionResponse(orderStitchOption));
        }
    }
}
