package com.darzee.shankh.dao;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeasurementParamDAO {
    private Long id;
    private String name;
    private String displayName;
    private String fileName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MeasurementParamDAO(String name, String displayName, String fileName) {
        this.name = name;
        this.displayName = displayName;
        this.fileName = fileName;
    }
}
