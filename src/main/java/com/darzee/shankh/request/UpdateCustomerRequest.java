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
public class UpdateCustomerRequest {
    private Integer age;

    @Size(min=10, max=13, message = "Invalid phone number")
    private String phoneNumber;

    private String name;

    @NotNull(message = "Boutique id is mandatory for updating customer")
    private Long boutiqueId;

    private String customerImageReferenceId;
}