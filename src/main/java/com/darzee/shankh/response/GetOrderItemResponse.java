package com.darzee.shankh.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@NoArgsConstructor
public class GetOrderItemResponse {
    private String message;
    private List<OrderItemDetails> data;

    public GetOrderItemResponse(List<OrderItemDetails> data) {
        this.message = CollectionUtils.isEmpty(data) ? "No data foud for these filters" : "Data fetched successfully";
        this.data = data;
    }
}
