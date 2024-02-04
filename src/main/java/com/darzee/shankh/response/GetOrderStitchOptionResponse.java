package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetOrderStitchOptionResponse {
    private List<GroupedOrderStitchOptionDetail> response;

    public GetOrderStitchOptionResponse(Map<String, List<OrderStitchOptionDetail>> orderStitchOptionDetailMap) {
        List<GroupedOrderStitchOptionDetail> groupedOrderStitchOptionDetails = new ArrayList<>();
        for(Map.Entry<String, List<OrderStitchOptionDetail>> orderStitchOption : orderStitchOptionDetailMap.entrySet()) {
            GroupedOrderStitchOptionDetail groupedOrderStitchOptionDetail =
                    new GroupedOrderStitchOptionDetail(orderStitchOption.getKey(), orderStitchOption.getValue());
            groupedOrderStitchOptionDetails.add(groupedOrderStitchOptionDetail);
        }
        this.response = groupedOrderStitchOptionDetails;
    }
}