package com.darzee.shankh.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class TailorLoginRequest {

    @NotNull
    @Size(min=10, max=13, message = "Invalid phone number")
    private String phoneNumber;

    @NotNull(message = "Country code is mandatory for app login")
    @Size(min=1, max=3, message = "Invalid country code")
    private String countryCode;
}
