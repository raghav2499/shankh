package com.darzee.shankh.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MeasurementRevisionsDAO {

    private Long id;
    private Long customerId;
    private Integer outfitType;
    private Map<String, Double> measurementValue;
    private LocalDateTime createdAt;

    public MeasurementRevisionsDAO(Long customerId, Integer outfitType, Map<String, Double> measurementValue) {
        this.customerId = customerId;
        this.outfitType = outfitType;
        this.measurementValue = measurementValue;
    }
}
