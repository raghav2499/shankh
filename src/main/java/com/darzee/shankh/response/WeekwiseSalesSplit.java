package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeekwiseSalesSplit {

    Date weekStartDate;
    Double totalSales;

    public WeekwiseSalesSplit(Double totalSales, LocalDate weekStartDate) {
        if(weekStartDate != null) {
            this.weekStartDate = Date.from(weekStartDate.atStartOfDay(ZoneId.of("UTC")).toInstant());
        }
        this.totalSales = totalSales;
    }
}
