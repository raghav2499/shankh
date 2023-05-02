package com.darzee.shankh.response;

import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.service.CustomerService;
import com.darzee.shankh.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetails {

    private CustomerService customerService = new CustomerService();

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

    CustomerDetails(CustomerDAO customerDAO) {
        if(customerDAO != null) {
            this.customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
            this.phoneNumber = customerDAO.getPhoneNumber();
            this.profilePicLink = customerService.getCustomerProfilePicLink(customerDAO.getId());
        }
    }
}
