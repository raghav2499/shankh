package com.darzee.shankh.request.innerObjects;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class StitchDetails {
    @Valid
    @NotNull(message = "stitch option id is mandatory for stitch creation")
    private Long stitchOptionId;
    @Valid
    private List<String> values;
    
    public Long getStitchOptionId() {
        return stitchOptionId;
    }
    public List<String> getValues() {
        return values;
    }
}
