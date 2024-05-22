package com.darzee.shankh.response;

import com.darzee.shankh.dao.StitchOptionsDAO;
import com.darzee.shankh.enums.OutfitSide;
import com.darzee.shankh.service.LocalisationService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetStitchOptionsResponse {
    
    @Autowired
    LocalisationService localisationService;

    private List<GroupedStitchOptionDetails> response;

    public GetStitchOptionsResponse(List<StitchOptionsDAO> stitchOptions) {
        Map<OutfitSide, List<StitchOptionsDAO>> groupedByOutfitSide =
                stitchOptions.stream().collect(Collectors.groupingBy(StitchOptionsDAO::getOutfitSide));
        List<GroupedStitchOptionDetails> response = new ArrayList<>();
        if(groupedByOutfitSide.containsKey(OutfitSide.TOP)) {
            List<StitchOptionDetail> stitchOptionDetails = groupedByOutfitSide.get(OutfitSide.TOP).stream().map(stitchOptionsDAO ->{
                StitchOptionDetail stitchOptionDetail=   new StitchOptionDetail(stitchOptionsDAO);                                  stitchOptionDetail.setLabel(localisationService.translate(stitchOptionDetail.getLabel()));
                stitchOptionDetail.getOptions().forEach(optionDetail -> optionDetail.setLabel(localisationService.translate(optionDetail.getLabel())));
                return stitchOptionDetail;
            } ).collect(Collectors.toList());

            GroupedStitchOptionDetails topStitchOptionDetails = new GroupedStitchOptionDetails(
               localisationService.translate(OutfitSide.TOP.getView()) , stitchOptionDetails);
            response.add(topStitchOptionDetails);
        }
        if(groupedByOutfitSide.containsKey(OutfitSide.BOTTOM)) {
            List<StitchOptionDetail> stitchOptionDetails = groupedByOutfitSide.get(OutfitSide.BOTTOM).stream().map(stitchOptionsDAO ->{
                StitchOptionDetail stitchOptionDetail=   new StitchOptionDetail(stitchOptionsDAO);                                  stitchOptionDetail.setLabel(localisationService.translate(stitchOptionDetail.getLabel()));
                stitchOptionDetail.getOptions().forEach(optionDetail -> optionDetail.setLabel(localisationService.translate(optionDetail.getLabel())));
                return stitchOptionDetail;
            } ).collect(Collectors.toList());
            GroupedStitchOptionDetails bottomStitchOptionDetails = new GroupedStitchOptionDetails( 
               localisationService.translate(OutfitSide.BOTTOM.getView()), stitchOptionDetails);
            response.add(bottomStitchOptionDetails);
        }
        this.response = response;
    }
}
