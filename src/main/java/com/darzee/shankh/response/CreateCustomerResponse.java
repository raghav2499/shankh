package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CreateCustomerResponse extends CustomerDetails {
    public String message;

    public CreateCustomerResponse(String customerName, String phoneNumber, String profilePicLink, Long customerId,
                                  String message) {
        super(customerName, phoneNumber, profilePicLink, customerId);
        this.message = message;
    }
}
