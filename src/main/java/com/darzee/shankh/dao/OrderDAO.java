package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OrderStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDAO {
    private Long id;
    private String invoiceNo;
    private OrderStatus orderStatus = OrderStatus.DRAFTED;

    private Boolean isDeleted = Boolean.FALSE;
    private BoutiqueDAO boutique;
    private OrderAmountDAO orderAmount;
    private CustomerDAO customer;
    private List<OrderItemDAO> orderItems;
//    private List<PaymentDAO> payment;

    public OrderDAO(String invoiceNo, BoutiqueDAO boutique, CustomerDAO customer) {
        this.invoiceNo = invoiceNo;
        this.boutique = boutique;
        this.customer = customer;
    }

    public Map<Long, OrderItemDAO> getOrderItemDAOMap() {
        Map<Long, OrderItemDAO> orderItemDAOMap = new HashMap<>();
        for (OrderItemDAO orderItem : orderItems) {
            orderItemDAOMap.put(orderItem.getId(), orderItem);
        }
        return orderItemDAOMap;
    }

    public List<OrderItemDAO> getNonDeletedItems() {
        if (!CollectionUtils.isEmpty(this.orderItems)) {
            return this.orderItems.stream().filter(item -> !Boolean.TRUE.equals(item.getIsDeleted()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();

    }

    public boolean validateMandatoryOrderFields() {
        if (this.boutique != null && this.customer != null) {
            return true;
        }
        return false;
    }

    public Double getPriceBreakupSum() {
        Double priceBreakupSum = 0d;
        if(Boolean.TRUE.equals(isDeleted)) {
            return priceBreakupSum;
        }
        priceBreakupSum = getNonDeletedItems().stream()
                .map(item -> item.getPriceBreakup()).flatMap(List::stream)
                .mapToDouble(pb -> pb.getValue()).sum();
        return priceBreakupSum;
    }

}