package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetCustomersResponse {

    List<CustomerDetails> boutiqueCustomerDetails;

    public GetCustomersResponse(List<CustomerDetails> boutiqueCustomerDetails) {
        this.boutiqueCustomerDetails = boutiqueCustomerDetails;
    }
}
