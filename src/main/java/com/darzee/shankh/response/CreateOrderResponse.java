package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateOrderResponse {

    private String message;
    private OrderSummary orderSummary;

    public CreateOrderResponse(String message, String invoiceNo, String outfitType, String trialDate, String deliveryDate,
                               String totalOrderAmount, String orderAdvanceRecieved) {
        this.message = message;
        this.orderSummary = new OrderSummary(invoiceNo, outfitType, trialDate, deliveryDate, totalOrderAmount,
                orderAdvanceRecieved);
    }

    public CreateOrderResponse(String message) {
        this.message = message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class OrderSummary {
        private String invoiceNo;

        private String outfitType;

        private String trialDate;

        private String deliveryDate;

        private String totalOrderAmount;

        private String orderAdvanceRecieved;
    }
}
