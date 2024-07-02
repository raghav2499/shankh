package com.darzee.shankh.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@SequenceGenerator(name = "address-seq", sequenceName = "address-seq", allocationSize = 1)
@Getter
@Setter
public class Address {
    //TODO to add custom sequence generator
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address-seq")
    @Column(name = "address_id")
    private Long id;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;
}
