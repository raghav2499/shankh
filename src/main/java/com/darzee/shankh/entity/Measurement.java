package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "measurement")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "measurement-seq", sequenceName = "measurement_seq", allocationSize = 1)
public class Measurement extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measurement-seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "waist")
    private Double waist;

    @Column(name = "seat")
    private Double seat;

    @Column(name = "calf")
    private Double calf;

    @Column(name = "ankle")
    private Double ankle;

    @Column(name = "bottom")
    private Double bottom;

    @Column(name = "length")
    private Double length;

    @Column(name = "kurta_length")
    private Double kurtaLength;

    @Column(name = "shoulder")
    private Double shoulder;

    @Column(name = "chest")
    private Double chest;

    @Column(name = "upper_chest")
    private Double upperChest;

    @Column(name = "bust")
    private Double bust;

    @Column(name = "armhole")
    private Double armHole;

    @Column(name = "sleeve_length")
    private Double sleeveLength;

    @Column(name = "sleeve_circumference")
    private Double sleeveCircumference;

    @Column(name = "neck")
    private Double neck;

    @Column(name = "front_neck_depth")
    private Double frontNeckDepth;

    @Column(name = "back_neck_depth")
    private Double backNeckDepth;

    @Column(name = "pyjama_length")
    private Double pyjamaLength;

    @Column(name = "pyjama_hip")
    private Double pyjamaHip;

    @Column(name = "knee")
    private Double knee;

    @Column(name = "fly")
    private Double fly;

    @Column(name = "blouse_length")
    private Double blouseLength;

    @Column(name = "below_bust")
    private Double belowBust;

    @Column(name = "shoulder_to_apex_point")
    private Double shoulderToApexLength;

    @Column(name = "kameez_length")
    private Double kameezLength;

    @Column(name = "salwar_length")
    private Double salwarLength;
    @Column(name = "salwar_hip")
    private Double salwarHip;

    @Column(name = "shirt_length")
    private Double shirtLength;

    @Column(name = "pant_length")
    private Double pantLength;

    @Column(name = "gown_length")
    private Double gownLength;

    @Column(name = "apex_to_apex_length")
    private Double apexToApexLength;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
