package com.darzee.shankh.request;
import com.darzee.shankh.request.innerObjects.StitchDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateStitchOptionRequest {
    private Long orderItemId;
    List<StitchDetails> stitchDetails;
}
