package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetOrderResponse {
    private String message;
    private List<OrderDetailResponse> data;

    public GetOrderResponse(List<OrderDetailResponse> data) {
        this.message = (Collections.isEmpty(data)) ? "No data found for these filters" : "Data fetched successfully";
        this.data = data;
    }
}
