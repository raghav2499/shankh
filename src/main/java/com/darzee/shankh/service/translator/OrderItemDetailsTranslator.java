package com.darzee.shankh.service.translator;

import com.darzee.shankh.response.OrderItemDetails;
import com.darzee.shankh.response.OrderStitchOptionDetail;
import com.darzee.shankh.service.LocalisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemDetailsTranslator {

    @Autowired
    private OrderStitchOptionsTranslator orderStitchOptionsTranslator;

    @Autowired
    private MeasurementDetailsTranslator measurementDetailsTranslator;

    @Autowired
    private LocalisationService localisationService;

    public OrderItemDetails translate(OrderItemDetails orderItemDetails) {
        if (orderItemDetails.getOutfitType() != null) {
            orderItemDetails.setOutfitType(localisationService.translate(orderItemDetails.getOutfitType()));
        }

        if (orderItemDetails.getMeasurementDetails() != null && orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails() != null) {
            orderItemDetails.getMeasurementDetails().setInnerMeasurementDetails(measurementDetailsTranslator.translate(orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails()));
        }

        if (orderItemDetails.getOrderItemStitchOptions() != null) {
            Map<String, List<OrderStitchOptionDetail>> newOrderStichOptions=new HashMap<>() ;
            for (String side : orderItemDetails.getOrderItemStitchOptions().keySet()) {
                List<OrderStitchOptionDetail> translatedOrderItemDetails = orderStitchOptionsTranslator.translate(orderItemDetails.getOrderItemStitchOptions().get(side));
                newOrderStichOptions.put(localisationService.translate(side), translatedOrderItemDetails);
            }
            orderItemDetails.setOrderItemStitchOptions(newOrderStichOptions);
        }

        if (orderItemDetails.getPriceBreakupSummaryList() != null) {
            orderItemDetails.getPriceBreakupSummaryList().forEach(priceBreakupSummary -> {
                priceBreakupSummary.setComponent(localisationService.translate(priceBreakupSummary.getComponent()));
            });
        }

        if (orderItemDetails.getStatus() != null) {
            orderItemDetails.setStatus(localisationService.translate(orderItemDetails.getStatus()));
        }
        return orderItemDetails;
    }
}

