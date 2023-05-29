package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDashboard {

    public Integer totalCustomerCount;
    public Integer newCustomersAdded;

    public CustomerDashboard(Integer totalCustomerCount, Integer newCustomersAdded) {
        this.totalCustomerCount = totalCustomerCount;
        this.newCustomersAdded = newCustomersAdded;
    }
}
