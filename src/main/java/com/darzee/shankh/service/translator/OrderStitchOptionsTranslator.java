package com.darzee.shankh.service.translator;

import com.darzee.shankh.response.OrderStitchOptionDetail;
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
}
