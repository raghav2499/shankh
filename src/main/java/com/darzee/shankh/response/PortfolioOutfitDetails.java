package com.darzee.shankh.response;

import com.darzee.shankh.dao.PortfolioOutfitsDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioOutfitDetails {

    private Long id;
    private Integer subOutfitType;
    private String subOutfitName;
    private String title;
    private Integer color;
    private List<String> imageUrl;
    private List<String> imageReferences;
    private LocalDate creationTime;

    public PortfolioOutfitDetails(PortfolioOutfitsDAO portfolioOutfitsDAO, String subOutfitName) {
        this.id = portfolioOutfitsDAO.getId();
        this.subOutfitType = portfolioOutfitsDAO.getSubOutfitType();
        this.subOutfitName = subOutfitName;
        this.title = portfolioOutfitsDAO.getTitle();
        this.creationTime = portfolioOutfitsDAO.getCreatedAt().toLocalDate();
        if (portfolioOutfitsDAO.getColor() != null) {
            this.color = portfolioOutfitsDAO.getColor().getOrdinal();
        }
    }
}

