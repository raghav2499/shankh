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
        if(groupedByOutfitSide.containsKey(OutfitSide.TOP)) {
            List<StitchOptionDetail> stitchOptionDetails = groupedByOutfitSide.get(OutfitSide.TOP).stream().map(stitchOptionsDAO ->
                    new StitchOptionDetail(stitchOptionsDAO)).collect(Collectors.toList());
            GroupedStitchOptionDetails topStitchOptionDetails = new GroupedStitchOptionDetails(OutfitSide.TOP.getView(), stitchOptionDetails);
            response.add(topStitchOptionDetails);
        }
        if(groupedByOutfitSide.containsKey(OutfitSide.BOTTOM)) {
            List<StitchOptionDetail> stitchOptionDetails = groupedByOutfitSide.get(OutfitSide.BOTTOM).stream().map(stitchOptionsDAO ->
                    new StitchOptionDetail(stitchOptionsDAO)).collect(Collectors.toList());
            GroupedStitchOptionDetails bottomStitchOptionDetails = new GroupedStitchOptionDetails(OutfitSide.BOTTOM.getView(), stitchOptionDetails);
            response.add(bottomStitchOptionDetails);
        }
        this.response = response;
    }
}
