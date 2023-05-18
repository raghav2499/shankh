package com.darzee.shankh.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class BoutiqueDetails {

    private String boutiqueReferenceId;

    @NotNull(message = "Boutique name is mandatory for sign up request")
    private String boutiqueName;

    @NotNull(message = "Boutique type is mandatory for sign up request")
    private String boutiqueType;

    @JsonProperty("shop_images_url")//todo: rename this variable to shop_image_reference_ids
    private List<String> shopImageReferenceIds;
}