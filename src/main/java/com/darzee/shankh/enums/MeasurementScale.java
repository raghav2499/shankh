package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MeasurementScale {

    @JsonProperty("cm")
    CENTIMETER("cm"),

    @JsonProperty("inch")
    INCH("inch");
    private String scale;

    MeasurementScale(String scale) {
        this.scale = scale;
    }

    static Map<String, MeasurementScale> measurementScaleMap = getEnumMap();

    public static Map<String, MeasurementScale> getEnumMap() {
        Map<String, MeasurementScale> measurementScaleEnumMap = new HashMap<>();
        measurementScaleEnumMap.put("inch", MeasurementScale.INCH);
        measurementScaleEnumMap.put("cm", MeasurementScale.CENTIMETER);
        return measurementScaleEnumMap;
    }

    public String getScale() {
        return this.scale;
    }
}
