package com.darzee.shankh.response;

import com.darzee.shankh.dao.PriceBreakupDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceBreakupSummary {
    private Long id;
    private String component;
    private Double value;
    private Integer componentQuantity;

    public PriceBreakupSummary(PriceBreakupDAO priceBreakupDAO) {
        this.id = priceBreakupDAO.getId();
        this.component = priceBreakupDAO.getComponent();
        this.value = priceBreakupDAO.getValue();
        this.componentQuantity = priceBreakupDAO.getQuantity();
    }
}
