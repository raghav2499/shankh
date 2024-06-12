package com.darzee.shankh.service.translator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.response.GetOrderResponse;
import com.darzee.shankh.service.LocalisationService;

@Service
public class GetOrderResponseTranslator {
    
    @Autowired
    private  OrderDetailResponseTranslator orderDetailResponseTranslator;

    @Autowired LocalisationService localisationService;
    

    public String getTranslatedMessage(String message) {
        return localisationService.translate(message);
    }

    public GetOrderResponse getTranslatedOrderDetailList(GetOrderResponse response) {
        response.setMessage(localisationService.translate(response.getMessage()));
        if(response.getData()!=null){
            response.getData().forEach(orderDetailResponse -> {
                            orderDetailResponseTranslator.translate(orderDetailResponse);
                        });
        }
        return response;
    }
}
