package com.darzee.shankh.response;

import com.darzee.shankh.dao.MeasurementRevisionsDAO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementRevisionData {
    private Long customerId;
    private Integer outfitType;
    private Map<String, Double> measurementValue;
    private LocalDateTime createdAt;

    public MeasurementRevisionData(MeasurementRevisionsDAO measurementRevisions) {
        this.customerId = measurementRevisions.getCustomerId();
        this.outfitType = measurementRevisions.getOutfitType().getOrdinal();
        this.measurementValue = measurementRevisions.getMeasurementValue();
        this.createdAt = measurementRevisions.getCreatedAt();
    }
}
