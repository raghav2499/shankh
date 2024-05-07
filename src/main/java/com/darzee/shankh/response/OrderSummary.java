package com.darzee.shankh.response;

import com.darzee.shankh.dao.OrderItemDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderSummary {
    private Long orderId;
    private Long boutiqueOrderId;

    private String invoiceNo;

    private List<OrderItemSummary> orderItemSummaryList = new ArrayList<>();
    private Double totalOrderAmount;

    private Double orderAdvanceRecieved;

    public OrderSummary(Long orderId, Long boutiqueOrderId, String invoiceNo, Double totalOrderAmount,
                        Double orderAdvanceRecieved,
                        List<OrderItemDAO> orderItemDAOs) {
        this.orderId = orderId;
        this.boutiqueOrderId = boutiqueOrderId;
        this.invoiceNo = invoiceNo;
        this.totalOrderAmount = totalOrderAmount;
        this.orderAdvanceRecieved = orderAdvanceRecieved;
        for (OrderItemDAO orderItem : orderItemDAOs) {
            orderItemSummaryList.add(new OrderItemSummary(orderItem));
        }
    }
}