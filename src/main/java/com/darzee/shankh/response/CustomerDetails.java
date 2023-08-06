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

    private String referenceId;
    private String profilePicLink;

    private String gender;

    private Double revenue;

    public CustomerDetails(String customerName,
                           String phoneNumber,
                           String profilePicLink,
                           Long customerId,
                           String gender) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.profilePicLink = profilePicLink;
        this.customerId = customerId;
        this.gender = gender;
    }

    public CustomerDetails(CustomerDAO customerDAO, String customerProfilePicLink) {
        if(customerDAO != null) {
            this.customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
            this.phoneNumber = customerDAO.getPhoneNumber();
            this.profilePicLink = customerProfilePicLink;
            this.age = customerDAO.getAge();
            this.gender = customerDAO.getGender().getString();
            this.customerId = customerDAO.getId();
        }
    }

    public CustomerDetails(CustomerDAO customerDAO, String referenceId, String customerProfilePicLink, Double revenue) {
        if(customerDAO != null) {
            this.customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
            this.phoneNumber = customerDAO.getPhoneNumber();
            this.referenceId = referenceId;
            this.profilePicLink = customerProfilePicLink;
            this.age = customerDAO.getAge();
            this.gender = customerDAO.getGender().getString();
            this.customerId = customerDAO.getId();
            this.revenue = revenue;
        }
    }

    public CustomerDetails(CustomerDAO customerDAO) {
        if(customerDAO != null) {
            this.customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
            this.phoneNumber = customerDAO.getPhoneNumber();
            this.customerId = customerDAO.getId();
            this.age = customerDAO.getAge();
            this.gender = customerDAO.getGender().getString();
        }
    }
}
