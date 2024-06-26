package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MeasurementsDAO implements Serializable {
    private Long id;
    private OutfitType outfitType;
    private CustomerDAO customer;
    private MeasurementRevisionsDAO measurementRevision;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MeasurementsDAO(CustomerDAO customer) {
        this.customer = customer;
    }
    public boolean containsMeasurement(String key) {
        MeasurementRevisionsDAO revision = this.getMeasurementRevision();
        if (!CollectionUtils.isEmpty(revision.getMeasurementValue()) && revision.getMeasurementValue().containsKey(key)) {
            return true;
        }
        return false;
    }
}
