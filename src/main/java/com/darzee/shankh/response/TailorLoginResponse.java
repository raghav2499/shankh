package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class TailorLoginResponse {

    private String token;
    private Long tailorId;
    private String tailorName;
    private String tailorImageReferenceId;
    private Long boutiqueId;
    private String message;

    public TailorLoginResponse(String message) {
        this.message = message;
    }
}
