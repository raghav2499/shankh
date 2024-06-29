package com.darzee.shankh.response;

import com.darzee.shankh.constants.Constants;
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
        if (orderStitchOptionDetailMap.containsKey(Constants.TOP_ALIAS)) {
            List<OrderStitchOptionDetail> stitchOptionDetails = orderStitchOptionDetailMap.get(Constants.TOP_ALIAS);
            GroupedOrderStitchOptionDetail topStitchOptionDetails = new GroupedOrderStitchOptionDetail(Constants.TOP_ALIAS, stitchOptionDetails);
            groupedOrderStitchOptionDetails.add(topStitchOptionDetails);
        }
        if (orderStitchOptionDetailMap.containsKey(Constants.BOTTOM_ALIAS)) {
            List<OrderStitchOptionDetail> stitchOptionDetails = orderStitchOptionDetailMap.get(Constants.BOTTOM_ALIAS);
            GroupedOrderStitchOptionDetail bottomStitchOptionDetails = new GroupedOrderStitchOptionDetail(Constants.BOTTOM_ALIAS, stitchOptionDetails);
            groupedOrderStitchOptionDetails.add(bottomStitchOptionDetails);
        }
        this.response = groupedOrderStitchOptionDetails;
    }
}