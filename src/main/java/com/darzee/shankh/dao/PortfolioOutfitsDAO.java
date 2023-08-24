package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OutfitType;

import java.time.LocalDateTime;

public class PortfolioOutfitsDAO {

    private Long id;

    private String title;

    private OutfitType outfitType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer subOutfitType;

    private PortfolioDAO portfolio;
}
