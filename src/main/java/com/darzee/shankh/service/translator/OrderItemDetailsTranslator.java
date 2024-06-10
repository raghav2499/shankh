package com.darzee.shankh.service.translator;

import com.darzee.shankh.response.OrderItemDetails;
import com.darzee.shankh.response.OrderStitchOptionDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
  
@Service
public class OrderItemDetailsTranslator {

    @Autowired
    private OrderStitchOptionsTranslator orderStitchOptionsTranslator;

    @Autowired
    private MeasurementDetailsTranslator measurementDetailsTranslator;

    public OrderItemDetails translate(OrderItemDetails orderItemDetails) {
    
        if (orderItemDetails.getMeasurementDetails() != null) {
            orderItemDetails.getMeasurementDetails().setInnerMeasurementDetails(
                    measurementDetailsTranslator.translate(orderItemDetails.getMeasurementDetails().getInnerMeasurementDetails()));
        }

        if (orderItemDetails.getOrderItemStitchOptions() != null) {
            for (String side : orderItemDetails.getOrderItemStitchOptions().keySet()) {
                List<OrderStitchOptionDetail> translatedOrderItemDetails =
                        orderStitchOptionsTranslator.translate(orderItemDetails.getOrderItemStitchOptions().get(side));
                orderItemDetails.getOrderItemStitchOptions().put(side, translatedOrderItemDetails);
            }
        }

        return orderItemDetails;
    }
}

