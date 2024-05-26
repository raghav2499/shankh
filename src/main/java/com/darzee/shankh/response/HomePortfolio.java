package com.darzee.shankh.response;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HomePortfolio {
    private String tailorName;
    private String boutiqueName;
    private Long portfolioId;
    private String portfolioAbout;
    private String username;
    private String profileImageLink;
    private String coverImageLink;
}
