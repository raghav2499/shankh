package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeekwiseSalesSplit {

    Date weekStartDate;
    Double totalSales;

    public WeekwiseSalesSplit(Double totalSales, Date weekStartDate) {
        this.weekStartDate = weekStartDate;
        this.totalSales = totalSales;
    }
}
