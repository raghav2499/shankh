package com.darzee.shankh.request;

import com.darzee.shankh.enums.Gender;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCustomerRequest {
    private Integer age;

    @NotNull(message = "Phone number is mandotaory for app sign up")
    @Size(min=10, max=10, message = "Invalid phone number")
    private String phoneNumber;
    private String countryCode;
    private String name;
    private Gender gender;
    private String customerImageReferenceId;
    private Long boutiqueId;
}
