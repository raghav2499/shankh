package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class UpdateTailorRequest {

    private String tailorName;

    @Size(min = 10, max = 10, message = "Invalid phone number")
    private String phoneNumber;
    private Integer language;

    private String tailorProfilePicReferenceId;
}
