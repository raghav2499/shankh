package com.darzee.shankh.service.translator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.GetStitchOptionsResponse;
import com.darzee.shankh.service.LocalisationService;

@Service
public class StitchOptionResponseTranslator {
    
    @Autowired
    private LocalisationService localisationService;

    @Autowired
    OrderStitchOptionsTranslator orderStitchOptionsTranslator;
    public String getTranslatedMessage(String message) {    
        return message;
    }

    public GetStitchOptionsResponse getTranslatedStitchOptionDetailList(GetStitchOptionsResponse stitchOptionsResponse) {
        stitchOptionsResponse.getResponse().forEach(groupedStitchOptionDetails -> {
            if (groupedStitchOptionDetails != null && groupedStitchOptionDetails.getStitchOptions() != null) {        orderStitchOptionsTranslator.translateStichOptionList(groupedStitchOptionDetails.getStitchOptions());
            }
        });
        return stitchOptionsResponse;
    }
}
