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
public class OrderAmountDetails {
    private Double totalAmount;
    private Double advanceReceived;
    private Double balanceDue;

    OrderAmountDetails(OrderAmountDAO orderAmountDAO) {
        if (orderAmountDAO != null) {
            this.totalAmount = orderAmountDAO.getTotalAmount();
            this.advanceReceived = (orderAmountDAO.getAmountRecieved() == 0) ? null : orderAmountDAO.getAmountRecieved();
            this.balanceDue = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
        }
    }
}
