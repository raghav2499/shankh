package com.darzee.shankh.request;

import com.darzee.shankh.enums.Language;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class TailorSignUpRequest {

    @NotNull(message = "Tailor name is mandatory for app sign up")
    private String tailorName;

    @NotNull(message = "Phone number is mandotaory for app sign up")
    @Size(min=10, max=10, message = "Invalid phone number")
    private String phoneNumber;

    private Language language;

    @JsonProperty("profile_pic_url")//todo : rename this variable to profile_pic_reference_id
    private String profilePicReferenceId;

    private BoutiqueDetails boutiqueDetails;

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
}
