package com.darzee.shankh.request.innerObjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetails {

    @NotNull(message = "customer_id cannot be null")
    private Long customerId;
    @NotNull(message = "boutique_id cannot be null")
    private Long boutiqueId;
    private Long orderId;
    private List<OrderItemDetailRequest> orderItems;
}
