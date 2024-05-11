package com.darzee.shankh.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@NoArgsConstructor
public class GetOrderItemResponse {
    private String message;
    private Long totalCount;
    private List<OrderItemDetails> data;

    public GetOrderItemResponse(List<OrderItemDetails> data, Long totalCount) {
        this.message = CollectionUtils.isEmpty(data) ? "No data foud for these filters" : "Data fetched successfully";
        this.totalCount = totalCount;
        this.data = data;
    }
}
