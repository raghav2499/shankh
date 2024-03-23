package com.darzee.shankh.dao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class OrderStitchOptionDAO {
    private Long id;
    private Long stitchOptionId;
    private List<String> values;
    private Long orderItemId;

    public OrderStitchOptionDAO(Long stitchOptionId, List<String> values) {
        this.stitchOptionId = stitchOptionId;
        this.values = values;
    }
}
