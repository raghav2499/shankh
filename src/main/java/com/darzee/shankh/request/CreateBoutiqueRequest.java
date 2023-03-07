package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateBoutiqueRequest {

    private String tailorName;
    private String boutiqueName;
    private String boutiqueType;
    private Integer tailorCount;

}
