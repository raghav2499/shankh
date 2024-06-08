package com.darzee.shankh.service.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.GetStitchOptionsResponse;
import com.darzee.shankh.service.LocalisationService;

@Service
public class StitchOptionResponseTranslator {
    
    @Autowired
    private LocalisationService localisationService;
    public String getTranslatedMessage(String message) {    
        return message;
    }

    public GetStitchOptionsResponse getTranslatedStitchOptionDetailList(GetStitchOptionsResponse stitchOptionsResponse) {
        stitchOptionsResponse.getResponse().forEach(groupedStitchOptionDetails -> {
            if (groupedStitchOptionDetails != null && groupedStitchOptionDetails.getStitchOptions() != null) {
                groupedStitchOptionDetails.getStitchOptions().forEach(stitchOption -> {
                    if (stitchOption != null) {
                        stitchOption.setLabel(localisationService.translate(stitchOption.getLabel()));
                        if (stitchOption.getOptions() != null) {
                            stitchOption.getOptions().forEach(option -> {
                                if (option != null) {
                                    option.setLabel(localisationService.translate(option.getLabel()));
                                }
                            });
                        }
                    }
                });
            }
        });
        return stitchOptionsResponse;
    }
}
