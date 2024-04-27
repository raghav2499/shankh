package com.darzee.shankh.response;

import com.darzee.shankh.dao.StitchOptionsDAO;
import com.darzee.shankh.enums.OutfitSide;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
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
        
        List<GroupedStitchOptionDetails> response = new ArrayList<>();
        if(groupedByOutfitSide.keySet().contains(OutfitSide.TOP)){
            List<StitchOptionsDAO> paramList = groupedByOutfitSide.get(OutfitSide.TOP);
            GroupedStitchOptionDetails groupedStitchOptionDetails = new GroupedStitchOptionDetails();
            groupedStitchOptionDetails.setSide(OutfitSide.TOP.getView());
            List<StitchOptionDetail> stitchOptionDetails = paramList.stream().map(stitchOptionsDAO ->
                    new StitchOptionDetail(stitchOptionsDAO)).collect(Collectors.toList());
            groupedStitchOptionDetails.setStitchOptions(stitchOptionDetails);
            response.add(groupedStitchOptionDetails);   
        }
        if(groupedByOutfitSide.keySet().contains(OutfitSide.BOTTOM)){
            List<StitchOptionsDAO> paramList = groupedByOutfitSide.get(OutfitSide.BOTTOM);
            GroupedStitchOptionDetails groupedStitchOptionDetails = new GroupedStitchOptionDetails();
            groupedStitchOptionDetails.setSide(OutfitSide.BOTTOM.getView());
            List<StitchOptionDetail> stitchOptionDetails = paramList.stream().map(stitchOptionsDAO ->
                    new StitchOptionDetail(stitchOptionsDAO)).collect(Collectors.toList());
            groupedStitchOptionDetails.setStitchOptions(stitchOptionDetails);
            response.add(groupedStitchOptionDetails);   
        }

        this.response = response;
    }
}
