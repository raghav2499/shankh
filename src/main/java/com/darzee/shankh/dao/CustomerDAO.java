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
}
