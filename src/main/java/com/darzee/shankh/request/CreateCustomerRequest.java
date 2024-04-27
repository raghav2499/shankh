package com.darzee.shankh.request;

import com.darzee.shankh.enums.Gender;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class CreateCustomerRequest {
    private Integer age;

    @NotNull(message = "Customer's phone number is mandatory ")
    @Size(min=10, max=13, message = "Invalid phone number")
    private String phoneNumber;
    private String countryCode;
    @NotNull(message = "Customer's name is mandatory")
    private String name;
    private Gender gender;
    private String customerImageReferenceId;
    @NotNull(message = "Boutique id is mandatory for creating customer")
    private Long boutiqueId;
}
