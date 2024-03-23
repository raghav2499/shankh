package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class OutfitDetails {
    private Integer outfitIndex;
    private String outfitName;
    private String outfitDetailsTitle;
    private String outfitLink;
    private Integer pieces;
    private boolean isPortfolioEligible;
    private boolean stitchOptionsExist;

    public OutfitDetails(Integer outfitIndex, String outfitName, String outfitDetailsTitle,
                         String outfitLink, Integer pieces, boolean isPortfolioEligible) {
        this.outfitIndex = outfitIndex;
        this.outfitName = outfitName;
        this.outfitDetailsTitle = outfitDetailsTitle;
        this.outfitLink = outfitLink;
        this.pieces = pieces;
        this.isPortfolioEligible = isPortfolioEligible;
    }
}
