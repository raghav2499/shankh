package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class AddBoutiqueDetailsRequest {
    private Long boutiqueId;
    private Integer tailorCount;
}
