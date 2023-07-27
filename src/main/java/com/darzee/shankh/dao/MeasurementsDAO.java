package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MeasurementsDAO implements Serializable {
    ObjectMapper objectMapper = new ObjectMapper();

    private Long id;

    private OutfitType outfitType;

    private Map<String, Double> measurementValue = new HashMap<>();
    private CustomerDAO customer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public MeasurementsDAO(CustomerDAO customer) {
        this.customer = customer;
    }

    public String getMeasurement(String key, Double dividingFactor) {
        if (!CollectionUtils.isEmpty(this.measurementValue) && this.measurementValue.containsKey(key)) {
            return CommonUtils.doubleToString(this.measurementValue.get(key) / dividingFactor);
        }
        return "";
    }

    public boolean containsMeasurement(String key) {
        if (!CollectionUtils.isEmpty(this.measurementValue) && this.measurementValue.containsKey(key)) {
            return true;
        }
        return false;
    }
}
