package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class OverallMeasurementDetails {

    private String message;
    private String outfitTypeHeading;
    private String outfitImageLink;
    private List<MeasurementDetails> measurementDetailsList;

    public OverallMeasurementDetails(String message) {
        this.message = message;
    }
}
