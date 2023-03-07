package com.darzee.shankh.request;

import com.darzee.shankh.enums.Gender;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCustomerRequest {
    private Integer age;
    private String phoneNumber;
    private String countryCode;
    private String name;
    private Gender gender;
    private Long boutiqueId;
}
