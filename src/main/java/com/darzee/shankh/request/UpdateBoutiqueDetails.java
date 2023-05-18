package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class UpdateBoutiqueDetails {

    private Integer boutiqueType;
    private String name;
    private Integer tailorCount;
    private List<String> boutiqueImageReferenceId;

    @Valid
    private UpdateTailorRequest tailor;
}
