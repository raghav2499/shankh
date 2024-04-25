package com.darzee.shankh.response;

import com.darzee.shankh.dao.StitchOptionsDAO;
import com.darzee.shankh.enums.OutfitSide;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetStitchOptionsResponse {
    private List<GroupedStitchOptionDetails> response;

    public GetStitchOptionsResponse(List<StitchOptionsDAO> stitchOptions) {
        Map<OutfitSide, List<StitchOptionsDAO>> groupedByOutfitSide =
                stitchOptions.stream().collect(Collectors.groupingBy(StitchOptionsDAO::getOutfitSide));

        List<GroupedStitchOptionDetails> response = groupedByOutfitSide.entrySet().stream().map(entry -> {
            GroupedStitchOptionDetails groupedStitchOptionDetails = new GroupedStitchOptionDetails();
            groupedStitchOptionDetails.setSide(entry.getKey().getView());
            List<StitchOptionDetail> stitchOptionDetails = entry.getValue().stream().map(stitchOptionsDAO ->
                    new StitchOptionDetail(stitchOptionsDAO)).collect(Collectors.toList());
            groupedStitchOptionDetails.setStitchOptions(stitchOptionDetails);
            return groupedStitchOptionDetails;

        }).collect(Collectors.toList());

        if(response.size() > 1 && response.get(0).getSide().equals("Bottom") ){
            GroupedStitchOptionDetails firstElement = response.get(0);
            response.set(0, response.get(1));
            response.set(1, firstElement); 
        }

        this.response = response;
    }
}
