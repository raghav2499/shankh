package com.darzee.shankh.service.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.LocalisationService;

@Service
public class OutfitTranslator {

    @Autowired
    private LocalisationService localizationService;
    
    public String getTranslatedMessage(String message) {
        return message;
    }

    public List<OutfitDetails> getTranslatedOutfitDetailsList(List<OutfitDetails> outfitDetailsList) {
        for (OutfitDetails outfitDetails : outfitDetailsList) {
            outfitDetails.setOutfitDetailsTitle(localizationService.translate(outfitDetails.getOutfitDetailsTitle()));
            outfitDetails.setOutfitName(localizationService.translate(outfitDetails.getOutfitName()));
        }
        return outfitDetailsList;
    }
}
