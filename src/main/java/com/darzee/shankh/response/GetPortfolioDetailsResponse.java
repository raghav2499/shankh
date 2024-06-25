package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPortfolioDetailsResponse implements Serializable {
    private String message;
    private String tailorName;
    private String boutiqueName;
    private Long portfolioId;
    private Map<String, String> socialMedia;
    private String portfolioAbout;
    private String username;
    private Integer usernameUpdatesCounts;
    private String profileImageLink;
    private String coverImageLink;
    private String profileImageReferenceId;
    private String coverImageReferenceId;
}
