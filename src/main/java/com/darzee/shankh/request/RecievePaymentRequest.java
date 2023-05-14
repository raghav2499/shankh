package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecievePaymentRequest {

    @NotNull(message = "payment_mode is a required param")
    private Integer paymentMode;
    @NotNull(message = "payment_date is a required param")
    private LocalDate paymentDate;
    @NotNull(message = "amount is a required param")
    private Double amount;
}
