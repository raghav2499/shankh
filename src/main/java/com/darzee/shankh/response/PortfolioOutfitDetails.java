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
    private Integer subOutfitType;
    private String title;
    private List<String> imageUrl;
    private List<String> imageReferences;
    private LocalDate creationTime;

    public PortfolioOutfitDetails(PortfolioOutfitsDAO portfolioOutfitsDAO) {
        this.subOutfitType = portfolioOutfitsDAO.getSubOutfitType();
        this.title = portfolioOutfitsDAO.getTitle();
        this.creationTime = portfolioOutfitsDAO.getCreatedAt().toLocalDate();
    }
}
