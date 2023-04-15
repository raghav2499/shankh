package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerDetails {
    private String customerName;

    private Long customerId;
    private String phoneNumber;

    private String profilePicLink;

    public CustomerDetails(String customerName, String phoneNumber, String profilePicLink, Long customerId) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.profilePicLink = profilePicLink;
        this.customerId = customerId;
    }
}
