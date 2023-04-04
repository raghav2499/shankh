package com.darzee.shankh.response;

import com.darzee.shankh.enums.Gender;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetCustomerResponse {

    private Integer age;

    private String phoneNumber;

    private String countryCode;

    private String email;

    private String firstName;

    private String middleName;

    private String lastName;

    private Gender gender;

    private String imageUrl;
}
