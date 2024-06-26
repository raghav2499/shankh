package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OverallMeasurementDetails {

    private String message;
    private List<InnerMeasurementDetails> innerMeasurementDetails = new ArrayList<>();
    private String measurementImageLink;
    private LocalDateTime measurementUpdatedAt;
    private Long measurementRevisionId;


    public OverallMeasurementDetails(String message) {
        this.message = message;
    }
}
