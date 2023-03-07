package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AlphabeticalCustomerDetails {
    private Character startingLetter;
    private List<CustomerDetails> customerDetails;

    public AlphabeticalCustomerDetails(Character startingLetter, List<CustomerDetails> customerDetails) {
        this.startingLetter = startingLetter;
        this.customerDetails = customerDetails;
    }
}