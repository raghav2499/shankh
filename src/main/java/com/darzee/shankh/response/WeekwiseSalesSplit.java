package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeekwiseSalesSplit {

    String weekStartDate;
    Double totalSales;

    public WeekwiseSalesSplit(Double totalSales, String weekStartDate) {
        this.weekStartDate = weekStartDate;
        this.totalSales = totalSales;
    }
}
