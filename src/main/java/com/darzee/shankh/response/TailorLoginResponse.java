package com.darzee.shankh.response;

import com.darzee.shankh.request.TailorLoginRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class TailorLoginResponse {

    private String token;
    private Long tailorId;
    private String tailorName;
    private String tailorProfilePicLink;
    private Long boutiqueId;
    private String message;

    public TailorLoginResponse(String message) {
        this.message = message;
    }
}
