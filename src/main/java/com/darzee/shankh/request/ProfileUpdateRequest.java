package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileUpdateRequest {

    private String tailorName;

    @Size(min = 10, max = 10, message = "Invalid phone number")
    private String phoneNumber;

    private String tailorProfilePicReferenceId;
}
