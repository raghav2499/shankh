package com.darzee.shankh.enums;

import java.util.HashMap;
import java.util.Map;

public enum FileType {

    PORTFOLIO("1"),
    AUDIO("2"),
    MEASUREMENT("3");

    private String ordinal;

    public String getOrdinal() {
        return this.ordinal;
    }

    FileType(String ordinal) {
        this.ordinal = ordinal;
    }

    public static Map<String, FileType> fileTypeMap = getFileTypeEnumOrdinalMap();

    public static Map<String, FileType> getFileTypeEnumOrdinalMap() {
        Map<String, FileType> fileTypeOrdinalMap = new HashMap<>();
        for(FileType fileType : FileType.values()) {
            fileTypeOrdinalMap.put(fileType.getOrdinal(), fileType);
        }
        return fileTypeOrdinalMap;
    }

    public static FileType getFileTypeFromOrdinal(String ordinal) {
        return getFileTypeEnumOrdinalMap().get(ordinal);
    }
}
