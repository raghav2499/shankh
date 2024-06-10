package com.darzee.shankh.service.translator;

import com.darzee.shankh.response.GetOrderStitchOptionResponse;
import com.darzee.shankh.response.OrderStitchOptionDetail;
import com.darzee.shankh.response.StitchOptionDetail;
import com.darzee.shankh.service.LocalisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStitchOptionsTranslator {

    @Autowired
    private LocalisationService localisationService;

    public List<OrderStitchOptionDetail> translate(List<OrderStitchOptionDetail> orderStitchOptionDetails) {
        for(OrderStitchOptionDetail orderStitchOptionDetail : orderStitchOptionDetails) {
            orderStitchOptionDetail.setLabel(localisationService.translate(orderStitchOptionDetail.getLabel()));
            orderStitchOptionDetail.setValue(localisationService.translate(orderStitchOptionDetail.getValue()));
        }
        return orderStitchOptionDetails;
    }

    public GetOrderStitchOptionResponse translateGetOrderStitchOptionResponse(GetOrderStitchOptionResponse  response) {
        response.getResponse().forEach(
                groupedOrderStitchOptionDetail1 -> groupedOrderStitchOptionDetail1.getStitchOptions().forEach(
                        stitchOption-> {
                stitchOption.setLabel(localisationService.translate(stitchOption.getLabel()));
                stitchOption.setValue(localisationService.translate(stitchOption.getValue())); 
                        }
                ) 
        );
        return response;
    }

    public List<StitchOptionDetail> translateStichOptionList(List<StitchOptionDetail> stitchOptionDetails ){

        stitchOptionDetails.forEach(stitchOption -> {
            if (stitchOption != null) {
                stitchOption.setLabel(localisationService.translate(stitchOption.getLabel()));
                if (stitchOption.getOptions() != null) {
                    stitchOption.getOptions().forEach(option -> {
                        if (option != null) {
                            option.setLabel(localisationService.translate(option.getLabel()));
                        }
                    });
                }
            }
        });
        return stitchOptionDetails;
    }
}

