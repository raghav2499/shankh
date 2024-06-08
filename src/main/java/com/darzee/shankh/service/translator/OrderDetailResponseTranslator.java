package com.darzee.shankh.service.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.OrderDetailResponse;
import com.darzee.shankh.response.OrderStitchOptionDetail;
import com.darzee.shankh.service.LocalisationService;

@Service
public class OrderDetailResponseTranslator {

    @Autowired
    private LocalisationService localisationService;

    public String getTranslatedMessage(String message) {
        return message;
    }

    public OrderDetailResponse getTransatedOrderDetailResponseTranslator(OrderDetailResponse response ){    
        response.getOrderItemDetails().forEach(orderItemDetails -> {
            // orderItemDetails.setOutfitAlias(localisationService.translate(orderItemDetails.getOutfitAlias()));
            if(orderItemDetails.getOrderItemStitchOptions()!=null){
                orderItemDetails.getOrderItemStitchOptions().forEach((key, value) -> {
                            List<OrderStitchOptionDetail> orderStitchOptionDetails = value;
                            orderStitchOptionDetails.forEach(orderStitchOptionDetail -> {
                                orderStitchOptionDetail.setLabel(localisationService.translate(orderStitchOptionDetail.getLabel()));                 orderStitchOptionDetail.setValue(localisationService.translate(orderStitchOptionDetail.getValue()));
                            });
                        }
                );
            }
            if(orderItemDetails.getMeasurementDetails()!=null&&orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails()!=null) {
                orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails().forEach(measurementDetails -> {
                    measurementDetails.setOutfitTypeHeading(localisationService.translate(measurementDetails.getOutfitTypeHeading()));
                    measurementDetails.getMeasurementDetailsList().forEach(innerMeasurementDetails -> {
                        innerMeasurementDetails.setTitle(localisationService.translate(innerMeasurementDetails.getTitle()));
                    });
                });
            }
        });
       return response;     
    }
}
