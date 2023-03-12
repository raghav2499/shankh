package com.darzee.shankh.request;

import com.darzee.shankh.enums.Language;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TailorSignUpRequest {
    private String tailorName;
    private String phoneNumber;
    private Language language;
    private BoutiqueDetails boutiqueDetails;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public class BoutiqueDetails {
        private String boutiqueReferenceId;
        private String boutiqueName;
        private String boutiqueType;
    }
}
