package com.darzee.shankh.request;

import com.darzee.shankh.request.innerObjects.OrderDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderCreationRequest {

    // This interface represents the validation group for creating an order.
    public interface CreateOrder {
    }

    //This interface represents the validation group for confirming an order.
    public interface ConfirmOrder {
    }

    @Valid
    @NotNull(message = "order_details are mandatory for order creation", groups = {CreateOrder.class})
    private OrderDetails orderDetails;
}

