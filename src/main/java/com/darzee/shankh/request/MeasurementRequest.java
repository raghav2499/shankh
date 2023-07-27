package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MeasurementRequest {
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
    private Double shirtLength;
    private Double pantLength;
    private Double apexToApexLength;
    private Double bottomWaist;
    private Double bottomSeat;
    private Double aboveHead;
    private Double aroundShoulder;
    private Double pardiGher;
    private Double lengaGher;
    private Double kas;
    private Double hip;
    private Double pardiShoulder;
    private Double lengaShoulder;
    private Double pardiLength;
    private Double lengaLength;
    private Double bicep;
    private Double elbowRound;
    private Double crossFront;
    private Double crossBack;
    private Double inSeam;
    private Double crotch;
}