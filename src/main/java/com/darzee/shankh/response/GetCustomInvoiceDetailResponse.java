package com.darzee.shankh.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class GetCustomInvoiceDetailResponse {
    private String adminTailorProfilePicUrl;
    private String boutiqueName;
    private String adminTailorPhoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String gstNumber;
    private BigDecimal gstRate;
    private Boolean includeDeliveryDate;
}
