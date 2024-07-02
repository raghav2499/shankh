package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class UpdateBoutiqueDetails {

    private Integer boutiqueType;
    private String name;
    private Integer tailorCount;
    private List<String> boutiqueImageReferenceId;
    private Boolean includeDeliveryDate;

    @Size(min = 15,max = 15, message = "Enter a valid GST Number")
    private String gstNumber; 

    @DecimalMin(value = "0.0", inclusive = true, message = "GST rate must be at least 0.0")
    @DecimalMax(value = "100.0", inclusive = true, message = "GST rate must be no more than 100.0")
    private BigDecimal gstRate;

    @Size(min = 10, max = 13, message = "Invalid phone number")
    private String boutiquePhoneNumber;

    @Size(min = 2, max = 4, message = "Invalid country code")
    private String countryCode;

    @Valid
    private UpdateAdreessRequest address;

    @Valid
    private UpdateTailorRequest tailor;
}
