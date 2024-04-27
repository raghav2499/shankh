package com.darzee.shankh.dao;

import com.darzee.shankh.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public CustomerDAO(Integer age, String countryCode, String phoneNumber, String firstName, String lastName,
                       Gender gender, BoutiqueDAO boutiqueDAO) {
        this.age = age;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.boutique = boutiqueDAO;
    }

    public boolean isPhoneNumberUpdated(String value) {
        return (value != null && !value.equals(this.phoneNumber)) || (value == null && this.phoneNumber != null);
    }

    public boolean isCountryCodeUpdated(String value) {
        return (value != null && !value.equals(this.countryCode)) || (value == null && this.countryCode != null);
    }

    public boolean isFirstNameUpdated(String value) {
        return (value != null && !value.equals(this.firstName)) || (value == null && this.firstName != null);
    }

    public boolean isLastNameUpdated(String value) {
        return (value != null && !value.equals(this.lastName)) || (value == null && this.lastName != null);
    }

    public boolean isAgeUpdated(Integer value) {
        return (value != null && !value.equals(this.age)) || (value == null && this.age != null);
    }

    public String getPhoneNumber() {
        String countryCode = Optional.ofNullable(this.getCountryCode()).orElse("");
        String phoneNumber = Optional.ofNullable(this.phoneNumber).orElse("");
        return countryCode + phoneNumber;
    }

    public String constructName() {
        return (Optional.ofNullable(this.firstName).orElse("") + " " + Optional.ofNullable(this.lastName).orElse("")).trim();
    }

}
