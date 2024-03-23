package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.utils.CommonUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MeasurementRevisionsDAO {

    private Long id;
    private Long customerId;
    private OutfitType outfitType;
    private Map<String, Double> measurementValue;
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

}
