package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MeasurementView {

    @JsonProperty("top")
    TOP("top"),

    @JsonProperty("bottom")
    BOTTOM("bottom");

    private String view;

    MeasurementView(String view) {
        this.view = view;
    }

    static Map<String, MeasurementView> measurementViewMap = getEnumMap();

    public static Map<String, MeasurementView> getEnumMap() {
        Map<String, MeasurementView> measurementViewEnumMap = new HashMap<>();
        measurementViewEnumMap.put(MeasurementView.TOP.view, MeasurementView.TOP);
        measurementViewEnumMap.put(MeasurementView.BOTTOM.view, MeasurementView.BOTTOM);
        return measurementViewEnumMap;
    }
}
