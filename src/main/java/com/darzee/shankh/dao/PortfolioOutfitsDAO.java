package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.enums.SubOutfitType;

import java.time.LocalDateTime;

public class PortfolioOutfitsDAO {

    private Long id;

    private String title;

    private OutfitType outfitType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private SubOutfitType subOutfitType;

    private PortfolioDAO portfolio;
}
