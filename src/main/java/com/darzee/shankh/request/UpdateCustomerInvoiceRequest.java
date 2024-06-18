package com.darzee.shankh.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class UpdateCustomerInvoiceRequest {
    private String adminTailorProfilePicUrl;

    @NotNull(message = "Boutique name is mandatory")
    @NotEmpty(message = "Boutique name can not be empty")
    @Size(min = 3, max = 50, message = "Boutique name should be between 3 and 50 characters")
    private String boutiqueName;

    @NotNull(message = "Phone number is mandatory")
    @NotEmpty(message = "Phone number can not be empty")
    @Size(min = 10, max = 13, message = "Invalid phone number")
    private String adminTailorPhoneNumber;

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

    @Size(min = 15,max = 15, message = "Enter a valid GST Number")
    private String gstNumber;

    @DecimalMin(value = "0.0", inclusive = true, message = "GST rate must be at least 0.0")
    @DecimalMax(value = "100.0", inclusive = true, message = "GST rate must be no more than 100.0")
    private BigDecimal gstRate;

    @NotNull(message = "Including delivery date decision must be specified")
    private Boolean includeDeliveryDate;
}
 