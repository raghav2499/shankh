package com.darzee.shankh.dao;

import com.darzee.shankh.enums.ColorEnum;
import com.darzee.shankh.enums.OutfitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PortfolioOutfitsDAO {

    private Long id;

    private String title;

    private Boolean isActive;

    private OutfitType outfitType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer subOutfitType;
    private ColorEnum color;

    private PortfolioDAO portfolio;

    public PortfolioOutfitsDAO(String title, OutfitType outfitType, Integer subOutfitType, ColorEnum color,
                               PortfolioDAO portfolio) {
        this.title = title;
        this.outfitType = outfitType;
        this.subOutfitType = subOutfitType;
        this.color = color;
        this.portfolio = portfolio;
    }
}
