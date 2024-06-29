package com.darzee.shankh.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDAO {
    private Long id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    public Boolean isAddressLine1Updated(String value) {
        return this.addressLine1 == null || value != null && !this.addressLine1.equals(value);
    }

    public Boolean isAddressLine2Updated(String value) {
        return this.addressLine2 == null || value != null && !this.addressLine2.equals(value);
    }

    public Boolean isCityUpdated(String value) {
        return this.city == null || value != null && !this.city.equals(value);
    }       

    public Boolean isStateUpdated(String value) {
        return this.state==null|| value != null && !this.state.equals(value);
    }

    public Boolean isCountryUpdated(String value) {
        return this.country == null || value != null && !this.country.equals(value);
    }

    public Boolean isPostalCodeUpdated(String value) {
        return  this.postalCode == null || value != null && !this.postalCode.equals(value);
    }
}
