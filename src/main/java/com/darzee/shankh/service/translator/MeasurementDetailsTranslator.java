package com.darzee.shankh.service.translator;

import com.darzee.shankh.response.InnerMeasurementDetails;
import com.darzee.shankh.service.LocalisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementDetailsTranslator {

    @Autowired
    private LocalisationService localisationService;

    public List<InnerMeasurementDetails> translate(List<InnerMeasurementDetails> measurementDetails) {
        for(InnerMeasurementDetails measurementDetail : measurementDetails) {
            measurementDetail.setOutfitTypeHeading(localisationService.translate(measurementDetail.getOutfitTypeHeading()));
            measurementDetail.getMeasurementDetailsList().forEach(detailsList -> {
                detailsList.setTitle(localisationService.translate(detailsList.getTitle()));
            });
        }
        return measurementDetails;
    }
}
