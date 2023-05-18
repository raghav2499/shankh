package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class UpdateBoutiqueDetails {

    private Integer boutiqueType;
    private String name;
    private Integer tailorCount;

    @Valid
    private UpdateTailorRequest tailor;
}
