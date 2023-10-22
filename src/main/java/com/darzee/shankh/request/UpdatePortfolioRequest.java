package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePortfolioRequest {
    private String username;
    private String about;
    private List<String> socialMedia;
    private String coverImageReference;
    private String profileImageReference;
}
