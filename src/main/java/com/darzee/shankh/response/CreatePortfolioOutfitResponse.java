package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class CreatePortfolioOutfitResponse {
    private String message;
    private Long portfolioOutfitId;
    private Integer outfitId;
    private String outfitName;
    private Integer subOutfitId;
    private String subOutfitName;

    public CreatePortfolioOutfitResponse(Long portfolioOutfitId,
                                         Integer outfitId,
                                         String outfitName,
                                         Integer subOutfitId,
                                         String subOutfitName) {
        this.portfolioOutfitId = portfolioOutfitId;
        this.outfitId = outfitId;
        this.outfitName = outfitName;
        this.subOutfitId = subOutfitId;
        this.subOutfitName = subOutfitName;
    }

    public CreatePortfolioOutfitResponse(String message, Long portfolioOutfitId) {
        this.message = message;
        this.portfolioOutfitId = portfolioOutfitId;
    }
}
