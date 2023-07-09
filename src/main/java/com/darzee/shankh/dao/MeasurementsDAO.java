package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OutfitType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MeasurementsDAO implements Serializable {
    ObjectMapper objectMapper = new ObjectMapper();

    private Long id;

    private OutfitType outfitType;

    private Map<String, Double> measurementValue;
    private CustomerDAO customer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public MeasurementsDAO(CustomerDAO customer) {
        this.customer = customer;
    }
}
