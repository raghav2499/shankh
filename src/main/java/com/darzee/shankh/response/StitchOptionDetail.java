package com.darzee.shankh.response;

import com.darzee.shankh.dao.StitchOptionsDAO;
import com.darzee.shankh.enums.StitchOptionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StitchOptionDetail {
    private Long id;
    private String type;
    private String label;
    private List<OptionDetail> options;
    private String minValue;
    private String maxValue;

    public StitchOptionDetail(StitchOptionsDAO stitchOptionsDAO) {
        this.id = stitchOptionsDAO.getId();
        this.type = stitchOptionsDAO.getType().getName();
        this.label = stitchOptionsDAO.getKey();
        if (StitchOptionType.COUNTER.getName().equals(this.type)) {
            this.minValue = stitchOptionsDAO.getValue().get(0);
            this.maxValue = stitchOptionsDAO.getValue().get(stitchOptionsDAO.getValue().size() - 1);
        } else {
            List<OptionDetail> optionDetails = new ArrayList<>();
            for (String value : stitchOptionsDAO.getValue()) {
                optionDetails.add(new OptionDetail(value));
            }
            this.options = optionDetails;
        }
    }
}
