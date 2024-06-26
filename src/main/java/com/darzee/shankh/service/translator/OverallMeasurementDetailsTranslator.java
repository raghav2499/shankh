package com.darzee.shankh.service.translator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.OverallMeasurementDetails;
import com.darzee.shankh.service.LocalisationService;


@Service
public class OverallMeasurementDetailsTranslator {
    @Autowired
    private LocalisationService localisationService;

    @Autowired
    private MeasurementDetailsTranslator measurementDetailsTranslator;

    public OverallMeasurementDetails translate(OverallMeasurementDetails measurementDetails,String message) {
measurementDetails.setMessage(localisationService.translate(message));
            measurementDetailsTranslator.getTranslatedInnerMeasurementDetailsList(measurementDetails.getInnerMeasurementDetails());
        return measurementDetails;
    }
}
