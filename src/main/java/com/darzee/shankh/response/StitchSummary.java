package com.darzee.shankh.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.darzee.shankh.dao.OrderStitchOptionDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class StitchSummary {
    private Long orderItemId;
    List<OrderStitchOption> orderStitchOptionList = new ArrayList<>();

    public StitchSummary(Long orderItemId, List<OrderStitchOptionDAO> stitchOptionDAOs) {
        this.orderItemId = orderItemId;
        for(OrderStitchOptionDAO stitchOption : stitchOptionDAOs) {
            orderStitchOptionList.add(new OrderStitchOption(stitchOption));
        }
    }
}
