package com.darzee.shankh.response;

import com.darzee.shankh.dao.OrderStitchOptionDAO;
import com.darzee.shankh.dao.StitchOptionsDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderStitchOptionDetail {
    public Long orderStitchOptionId;
    public Long stitchOptionId;
    public String type;
    public String label;
    public String outfitSide;
    public String value;

    public OrderStitchOptionDetail(OrderStitchOptionDAO orderStitchOptionDAO, StitchOptionsDAO stitchOptionsDAO) {
        this.orderStitchOptionId = orderStitchOptionDAO.getId();
        this.stitchOptionId = orderStitchOptionDAO.getStitchOptionId();
        this.type = stitchOptionsDAO.getType().getName();
        this.label = stitchOptionsDAO.getKey();
        this.outfitSide = stitchOptionsDAO.getOutfitSide().getView();
        this.value = orderStitchOptionDAO.getValues().get(0);
    }
}
