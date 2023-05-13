package com.darzee.shankh.dao;

import com.darzee.shankh.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CustomerDAO {
    private Long id;
    private Integer age;
    private String phoneNumber;
    private String countryCode = "+91";
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoutiqueDAO boutique;
    private MeasurementDAO measurement;
    private List<OrderDAO> orders;

    public CustomerDAO(Integer age, String phoneNumber, String firstName, String lastName, Gender gender,
                       BoutiqueDAO boutiqueDAO) {
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.boutique = boutiqueDAO;
    }

    public boolean isPhoneNumberUpdated(String value) {
        return value != null && !this.phoneNumber.equals(value);
    }

    public boolean isFirstNameUpdated(String value) {
        return value != null && !this.firstName.equals(value);
    }
    public boolean isLastNameUpdated(String value) {
        return value != null && !this.lastName.equals(value);
    }

    public boolean isAgeUpdated(Integer value) {
        return value != null && !this.age.equals(value);
    }


}
