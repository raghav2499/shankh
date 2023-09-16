package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SampleImageType {
    PORTFOLIO_COVER,
    PORTFOLIO_PROFILE;
}
