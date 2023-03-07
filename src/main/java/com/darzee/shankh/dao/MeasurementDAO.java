package com.darzee.shankh.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class MeasurementDAO implements Serializable {
    private Long id;
    private Double waist;
    private Double seat;
    private Double calf;
    private Double ankle;
    private Double bottom;
    private Double length;
    private Double kurtaLength;
    private Double shoulder;
    private Double chest;
    private Double upperChest;
    private Double bust;
    private Double armHole;
    private Double sleeveLength;
    private Double sleeveCircumference;
    private Double neck;
    private Double frontNeckDepth;
    private Double backNeckDepth;
    private Double pyjamaLength;
    private Double pyjamaHip;
    private Double knee;
    private Double fly;
    private Double blouseLength;
    private Double belowBust;
    private Double shoulderToApexLength;
    private Double kameezLength;
    private Double salwarLength;
    private Double salwarHip;
    private Double gownLength;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CustomerDAO customer;
}
