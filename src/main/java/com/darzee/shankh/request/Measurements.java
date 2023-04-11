package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Measurements {
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


    public Boolean isPresent() {
        return this.waist != null || this.seat != null || this.calf != null || this.ankle != null || this.bottom != null
                || this.length != null || this.kurtaLength != null || this.shoulder != null || this.chest != null
                || this.upperChest != null || this.bust != null || this.armHole != null || this.sleeveLength != null
                || this.sleeveCircumference != null || this.neck != null || this.frontNeckDepth != null
                || this.backNeckDepth != null || this.pyjamaLength != null || this.pyjamaHip != null || this.knee != null
                || this.fly != null || this.blouseLength != null || this.belowBust != null
                || this.shoulderToApexLength != null || this.kameezLength != null || this.salwarLength != null
                || this.salwarHip != null || this.gownLength != null || this.shirtLength != null || this.pantLength != null
                || this.apexToApexLength != null;

    }
}