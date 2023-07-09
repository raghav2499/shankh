package com.darzee.shankh.dao;

import com.darzee.shankh.enums.Gender;
import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
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
    private List<OrderDAO> orders;
    private List<MeasurementsDAO> measurements;

    public CustomerDAO(Integer age, String phoneNumber, String firstName, String lastName, Gender gender, BoutiqueDAO boutiqueDAO) {
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.boutique = boutiqueDAO;
    }

    public boolean isPhoneNumberUpdated(String value) {
        return (value != null && !value.equals(this.phoneNumber)) || (value == null && this.phoneNumber != null);
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

    public MeasurementsDAO getOutfitMeasurement(OutfitType outfitType) {
        Optional<MeasurementsDAO> optionalMeasurement = this.getMeasurements()
                        .stream()
                        .filter(measurement -> outfitType.equals(measurement.getOutfitType()))
                .findFirst();
        if(optionalMeasurement.isPresent()) {
            return optionalMeasurement.get();
        }
        return null;
    }

}
