package com.darzee.shankh.response;

import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetails {

    private String customerName;
    private Long customerId;
    private String phoneNumber;

    private Integer age;
    private String profilePicLink;

    public CustomerDetails(String customerName, String phoneNumber, String profilePicLink, Long customerId) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.profilePicLink = profilePicLink;
        this.customerId = customerId;
    }

    public CustomerDetails(CustomerDAO customerDAO, String customerProfilePicLink) {
        if(customerDAO != null) {
            this.customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
            this.phoneNumber = customerDAO.getPhoneNumber();
            this.profilePicLink = customerProfilePicLink;
        }
    }

    public CustomerDetails(CustomerDAO customerDAO) {
        if(customerDAO != null) {
            this.customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
            this.phoneNumber = customerDAO.getPhoneNumber();
            this.customerId = customerDAO.getId();
            this.age = customerDAO.getAge();
        }
    }
}
