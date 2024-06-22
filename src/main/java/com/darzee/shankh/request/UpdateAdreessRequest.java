package com.darzee.shankh.request;

import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class UpdateAdreessRequest {

    private Long id;

    @Size(max = 255, message = "Address line 1 must be less than 256 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must be less than 256 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must be less than 100 characters")
    private String city;

    @Size(max = 100, message = "State must be less than 100 characters")
    private String state;

    @Size(max = 100, message = "Country must be less than 100 characters")
    private String country;

    @Size(max = 20, message = "Postal code must be less than 20 characters")
    private String postalCode;
}
 