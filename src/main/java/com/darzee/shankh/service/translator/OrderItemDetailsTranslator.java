package com.darzee.shankh.service.translator;

import com.darzee.shankh.response.OrderItemDetails;
import com.darzee.shankh.service.LocalisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemDetailsTranslator {

    @Autowired
    private LocalisationService localisationService;

    public OrderItemDetails translate(OrderItemDetails orderItemDetails) {
        orderItemDetails.setOutfitAlias(localisationService.translate(orderItemDetails.getOutfitAlias()));

        if (orderItemDetails.getMeasurementDetails() != null) {
            orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails().forEach(innerMeasurementDetails -> {
                innerMeasurementDetails.setOutfitTypeHeading(localisationService.translate(innerMeasurementDetails.getOutfitTypeHeading()));
                innerMeasurementDetails.getMeasurementDetailsList().forEach(measurementDetails -> {
                    measurementDetails.setTitle(localisationService.translate(measurementDetails.getTitle()));
                });
            });
        }

        if (orderItemDetails.getOrderItemStitchOptions() != null) {
            orderItemDetails.getOrderItemStitchOptions().forEach((key, stitchOptionDetails) -> {
                stitchOptionDetails.forEach(stitchOptionDetail -> {
                    stitchOptionDetail.setLabel(localisationService.translate(stitchOptionDetail.getLabel()));
                    stitchOptionDetail.setValue(localisationService.translate(stitchOptionDetail.getValue()));
                });
            });
        }

        return orderItemDetails;
    }
}

