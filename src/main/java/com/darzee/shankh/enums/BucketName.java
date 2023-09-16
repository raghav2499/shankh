package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BucketName {
    GENERIC,
    STATIC,
    PORTFOLIO;
}
