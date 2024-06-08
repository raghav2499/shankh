package com.darzee.shankh.service.translator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.OverallMeasurementDetails;
import com.darzee.shankh.service.LocalisationService;
import com.darzee.shankh.service.MeasurementService;


@Service
public class OverallMeasurementDetailsTranslator {
    @Autowired
    private LocalisationService localisationService;

    

    public OverallMeasurementDetails translate(OverallMeasurementDetails measurementDetails,String message) {
measurementDetails.setMessage(localisationService.translate(message));
        return measurementDetails;
    }
}
