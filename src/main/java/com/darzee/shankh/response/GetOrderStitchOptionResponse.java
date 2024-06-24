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
        if (orderStitchOptionDetailMap.containsKey("Top")) {
            List<OrderStitchOptionDetail> stitchOptionDetails = orderStitchOptionDetailMap.get("Top");
            GroupedOrderStitchOptionDetail topStitchOptionDetails = new GroupedOrderStitchOptionDetail("Top", stitchOptionDetails);
            groupedOrderStitchOptionDetails.add(topStitchOptionDetails);
        }
        if (orderStitchOptionDetailMap.containsKey("Bottom")) {
            List<OrderStitchOptionDetail> stitchOptionDetails = orderStitchOptionDetailMap.get("Bottom");
            GroupedOrderStitchOptionDetail bottomStitchOptionDetails = new GroupedOrderStitchOptionDetail("Bottom", stitchOptionDetails);
            groupedOrderStitchOptionDetails.add(bottomStitchOptionDetails);
        }
        this.response = groupedOrderStitchOptionDetails;
    }
}