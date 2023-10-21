package com.darzee.shankh.repo;

import com.darzee.shankh.entity.PortfolioOutfits;
import com.darzee.shankh.enums.OutfitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioOutfitsRepo extends JpaRepository<PortfolioOutfits, Long> {

    @Nullable
    List<PortfolioOutfits> findAllByPortfolioIdAndOutfitTypeInAndSubOutfitTypeInAndIsValid(long portfolioId,
                                                                                           List<OutfitType> outfitType,
                                                                                           List<Integer> subOutfitType,
                                                                                           Boolean isValid);
}
