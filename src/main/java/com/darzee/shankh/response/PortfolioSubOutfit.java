package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class PortfolioSubOutfit {
    private Integer id;
    private String name;

    public PortfolioSubOutfit(Integer subOutfitType, String title){
        this.id = subOutfitType;
        this.name = title;
    }
}
