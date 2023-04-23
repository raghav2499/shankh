package com.darzee.shankh.response;

import com.darzee.shankh.dao.OrderAmountDAO;
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
public class PaymentDetails {
    private Double totalAmount;
    private Double advanceReceived;
    private Double balanceDue;

    PaymentDetails(OrderAmountDAO orderAmountDAO) {
        this.totalAmount = orderAmountDAO.getTotalAmount();
        this.advanceReceived = orderAmountDAO.getAmountRecieved();
        this.balanceDue = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
    }
}
