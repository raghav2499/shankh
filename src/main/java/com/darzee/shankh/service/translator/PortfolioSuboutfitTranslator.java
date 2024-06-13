package com.darzee.shankh.service.translator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.SubOutfitTypeDetailResponse;
import com.darzee.shankh.service.LocalisationService;

@Service
public class PortfolioSuboutfitTranslator {

    @Autowired
    private LocalisationService localisationService;

   public SubOutfitTypeDetailResponse translate(SubOutfitTypeDetailResponse response){
        response.setMessage(localisationService.translate(response.getMessage()));
        response.getSubOutfits().forEach(subOutfit -> subOutfit.setName(localisationService.translate(subOutfit.getName())));
        return response;
    }
    
}
