package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor

public class InvoiceDetailResponse {

    String invoiceDateTime;
    String boutiqueName;
    String customerName;
    String recieveDateTime;
    Optional<Integer> paymentMode;

    OrderSummary orderSummary;

    public InvoiceDetailResponse(String invoiceDateTime, String boutiqueName, String customerName, String recieveDateTime, OrderSummary orderSummary, Integer paymentMode

    ) {

        this.invoiceDateTime = invoiceDateTime;
        this.boutiqueName = boutiqueName;
        this.customerName = customerName;
        this.recieveDateTime = recieveDateTime;
        this.orderSummary = orderSummary;
        this.paymentMode = Optional.ofNullable(paymentMode);
    }

}
