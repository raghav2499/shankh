package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.OptionalDouble;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesDashboard {

    public List<WeekwiseSalesSplit> weeklySales;

    public Double maxBarGraphLength;
    public Double totalSales;

    public SalesDashboard(List<WeekwiseSalesSplit> weeklySales) {
        this.weeklySales = weeklySales;
        this.totalSales = weeklySales.stream().map(sales -> sales.getTotalSales())
                .reduce(0d, (a, b) -> a + b);
        OptionalDouble maxSales = weeklySales.stream().mapToDouble(sales -> sales.getTotalSales()).max();
        this.maxBarGraphLength = maxSales.isPresent() ? 1.1 * (maxSales.getAsDouble()) : 10;
    }
}
