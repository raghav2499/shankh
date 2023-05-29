package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesDashboard {

    public List<Double> weeklySales;
    public Double totalSales;

    public SalesDashboard(List<Double> weeklySales) {
        this.weeklySales = weeklySales;
        this.totalSales = weeklySales.stream().reduce(0d, (a, b) -> a + b);
    }
}
