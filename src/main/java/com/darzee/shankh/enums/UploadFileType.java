package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UploadFileType {

    PORTFOLIO("portfolio", 1);

    private String name;
    private Integer ordinal;

    public static Map<Integer, UploadFileType> uploadFileOrdinalEnumMap = getUploadFileOrdinalEnumMap();

    private static Map<Integer, UploadFileType> getUploadFileOrdinalEnumMap() {
        Map<Integer, UploadFileType> uploadFileOrdinalEnumMap = new HashMap<>();
        for(UploadFileType uploadFileType : UploadFileType.values()) {
            uploadFileOrdinalEnumMap.put(uploadFileType.ordinal, uploadFileType);
        }
        return uploadFileOrdinalEnumMap;
    }

    UploadFileType(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }
}
