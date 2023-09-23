package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SampleImageType {
    PORTFOLIO_COVER("portfolio_cover"),
    PORTFOLIO_PROFILE("portfolio_profile");

    String name;

    SampleImageType(String name) {
        this.name = name;
    }

    public static Map<String, SampleImageType> sampleImageNameEnumMap = getSampleImageNameEnumMap();

    private static Map<String, SampleImageType> getSampleImageNameEnumMap() {
        Map<String, SampleImageType> sampleImageOrdinalEnumMap = new HashMap<>();
        for(SampleImageType sampleImageType : SampleImageType.values()) {
            sampleImageOrdinalEnumMap.put(sampleImageType.name, sampleImageType);
        }
        return sampleImageOrdinalEnumMap;
    }
}
