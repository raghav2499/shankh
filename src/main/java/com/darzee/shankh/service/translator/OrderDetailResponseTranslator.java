package com.darzee.shankh.service.translator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.OrderDetailResponse;

@Service
public class OrderDetailResponseTranslator {


    @Autowired
    private MeasurementDetailsTranslator measurementDetailsTranslator;

    @Autowired
    private OrderItemDetailsTranslator orderItemDetailsTranslator;

    public String getTranslatedMessage(String message) {
        return message;
    }

    public OrderDetailResponse getTransatedOrderDetailResponseTranslator(OrderDetailResponse response ){    
        response.getOrderItemDetails().forEach(orderItemDetails -> {

            orderItemDetailsTranslator.translate(orderItemDetails);
            // orderItemDetails.setOutfitAlias(localisationService.translate(orderItemDetails.getOutfitAlias()));
            // if(orderItemDetails.getOrderItemStitchOptions()!=null){
            //     orderItemDetails.getOrderItemStitchOptions().forEach((key, value) -> {
            //                 List<OrderStitchOptionDetail> orderStitchOptionDetails = value;
            //                 orderStitchOptionDetails.forEach(orderStitchOptionDetail -> {
            //                     orderStitchOptionDetail.setLabel(localisationService.translate(orderStitchOptionDetail.getLabel()));                 orderStitchOptionDetail.setValue(localisationService.translate(orderStitchOptionDetail.getValue()));
            //                 });
            //             }
            //     );
            // }
            if(orderItemDetails.getMeasurementDetails()!=null&&orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails()!=null) {  measurementDetailsTranslator.getTranslatedInnerMeasurementDetailsList(orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails());
                // orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails().forEach(measurementDetails -> {
                //     measurementDetails.setOutfitTypeHeading(localisationService.translate(measurementDetails.getOutfitTypeHeading()));
                //     measurementDetails.getMeasurementDetailsList().forEach(innerMeasurementDetails -> {
                //         innerMeasurementDetails.setTitle(localisationService.translate(innerMeasurementDetails.getTitle()));
                //     });
                // });
            }
        });
       return response;     
    }
}
