package com.darzee.shankh.repo;

import com.darzee.shankh.entity.BoutiqueMeasurement;
import com.darzee.shankh.enums.OutfitSide;
import com.darzee.shankh.enums.OutfitType;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoutiqueMeasurementRepo extends JpaRepository<BoutiqueMeasurement, Long> {
    BoutiqueMeasurement findByBoutiqueIdAndOutfitTypeAndOutfitSide(Long boutiqueId, OutfitType outfitType,
            OutfitSide outfitSide);

    BoutiqueMeasurement findByBoutiqueIdAndOutfitType(Long boutiqueId, OutfitType outfitType);

    @Query("SELECT b FROM BoutiqueMeasurement b WHERE b.boutiqueId = :boutiqueId AND b.outfitType = :outfitType AND b.outfitSide = :outfitSide")
    BoutiqueMeasurement findUniqueByBoutiqueIdOutfitTypeOutfitSide(@Param("boutiqueId") Long boutiqueId, @Param("outfitType") OutfitType outfitType, @Param("outfitSide") OutfitSide outfitSide);


}