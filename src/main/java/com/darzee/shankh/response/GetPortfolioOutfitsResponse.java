package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GetPortfolioOutfitsResponse {
    private String message = "Details fetched successfully";

    @JsonProperty(value = "outfit_details")
    private List<OutfitTypeGroupedPortfolioDetails> outfitTypeGroupedPortfolioDetails;
}
