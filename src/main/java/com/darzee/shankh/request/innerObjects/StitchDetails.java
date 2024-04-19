package com.darzee.shankh.request.innerObjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StitchDetails {

    @NotNull(message = "ID is mandatory to create stitch options")
    private Long stitchOptionId;
    @NotNull(message = "Values are mandatory to create stitch options")
    private List<String> values;

    public Long getStitchOptionId() {
        return stitchOptionId;
    }

    public List<String> getValues() {
        return values;
    }
}
