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
            if(orderItemDetails.getMeasurementDetails()!=null&&orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails()!=null) {  measurementDetailsTranslator.getTranslatedInnerMeasurementDetailsList(orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails());
            }
        });
       return response;     
    }
}
