package com.darzee.shankh.service.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.GetOrderResponse;
import com.darzee.shankh.response.OrderStitchOptionDetail;
import com.darzee.shankh.service.LocalisationService;

@Service
public class GetOrderResponseTranslator {
    
    @Autowired
    private LocalisationService localisationService;

    public String getTranslatedMessage(String message) {
        return localisationService.translate(message);
    }

    public GetOrderResponse getTranslatedOrderDetailList(GetOrderResponse response) {
        response.setMessage(localisationService.translate(response.getMessage()));
        if(response.getData()!=null){
            response.getData().forEach(orderDetailResponse -> {
                //            orderDetailResponse.setMessage(localisationService.translate(orderDetailResponse.getMessage()));
                            if(orderDetailResponse.getOrderItemDetails()!=null){
                                orderDetailResponse.getOrderItemDetails().forEach(orderItemDetails -> {
                                    if(orderItemDetails.getOutfitAlias()!=null) {
                                        // orderItemDetails.setOutfitAlias(localisationService.translate(orderItemDetails.getOutfitAlias()));
                                    }
                                });
                            }
                            orderDetailResponse.getOrderItemDetails().forEach(orderItemDetails -> {
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
                        });
        }
        return response;
    }
}
