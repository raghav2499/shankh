package com.darzee.shankh.service.translator;


import com.darzee.shankh.service.LocalisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.OrderDetailResponse;

@Service
public class OrderDetailResponseTranslator {


    @Autowired
    private MeasurementDetailsTranslator measurementDetailsTranslator;

    @Autowired
    private OrderItemDetailsTranslator orderItemDetailsTranslator;

    @Autowired
    private LocalisationService localisationService;

    public OrderDetailResponse translate(OrderDetailResponse response ){
        response.setOrderStatus(localisationService.translate(response.getOrderStatus()));
        response.getOrderItemDetails().forEach(orderItemDetails -> {
            orderItemDetailsTranslator.translate(orderItemDetails);
        });
       return response;     
    }
}
