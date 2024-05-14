package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.utils.CommonUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MeasurementRevisionsDAO {

    private Long id;
    private Long customerId;
    private OutfitType outfitType;
    private Map<String, Double> measurementValue = new HashMap<>();
    private LocalDateTime createdAt;

    public MeasurementRevisionsDAO(Long customerId, OutfitType outfitType, Map<String, Double> measurementValue) {
        this.customerId = customerId;
        this.outfitType = outfitType;
        this.measurementValue = measurementValue;
    }

    public String getMeasurement(String key, Double dividingFactor) {
        if (!CollectionUtils.isEmpty(this.getMeasurementValue())
                && this.getMeasurementValue().containsKey(key)) {
            return CommonUtils.doubleToString(this.getMeasurementValue().get(key) / dividingFactor);
        }
        return "";
    }

    public Map<String, Double> getMeasurementValue(Double dividingFactor) {
        for (String key : measurementValue.keySet()) {
            Double normalisedValue = measurementValue.get(key) / dividingFactor;
            Double roundedValue = Math.round(normalisedValue * 100.0) / 100.0;
            measurementValue.put(key, roundedValue);
        }
        return measurementValue;
    }

    public Map<String, Double> getMeasurementValue() {
        if(this.measurementValue == null) {
            return new HashMap<>();
        }
        return this.measurementValue;
    }
}
