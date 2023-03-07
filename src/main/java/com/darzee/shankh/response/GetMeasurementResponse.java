package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMeasurementResponse {
    private String waist;
    private String seat;
    private String calf;
    private String ankle;
    private String bottom;
    private String length;
    private String kurtaLength;
    private String shoulder;
    private String chest;
    private String upperChest;
    private String bust;
    private String armHole;
    private String sleeveLength;
    private String sleeveCircumference;
    private String neck;
    private String frontNeckDepth;
    private String backNeckDepth;
    private String pyjamaLength;
    private String pyjamaHip;
    private String knee;
    private String fly;
    private String blouseLength;
    private String belowBust;
    private String shoulderToApexLength;
    private String kameezLength;
    private String salwarLength;
    private String salwarHip;
    private String gownLength;
}
