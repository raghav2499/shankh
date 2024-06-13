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

    @Autowired
    private OrderStitchOptionsTranslator orderStitchOptionsTranslator;

    @Autowired
    private MeasurementDetailsTranslator measurementDetailsTranslator;
    

    public String getTranslatedMessage(String message) {
        return localisationService.translate(message);
    }

    public GetOrderResponse getTranslatedOrderDetailList(GetOrderResponse response) {
        response.setMessage(localisationService.translate(response.getMessage()));
        if(response.getData()!=null){
            response.getData().forEach(orderDetailResponse -> { 
                            orderDetailResponse.getOrderItemDetails().forEach(orderItemDetails -> { 
                                
                                if(orderItemDetails.getOrderItemStitchOptions()!=null){
                                    orderItemDetails.getOrderItemStitchOptions().forEach((key, value) -> {
                                                List<OrderStitchOptionDetail> orderStitchOptionDetails = value;                                           orderStitchOptionsTranslator.translate(orderStitchOptionDetails);
                                            }
                                    );
                                }
                               if(orderItemDetails.getMeasurementDetails()!=null&&orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails()!=null) {               measurementDetailsTranslator.getTranslatedInnerMeasurementDetailsList(orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails());
                               }
                            });
                        });
        }
        return response;
    }
}
