package com.darzee.shankh.repo;

import com.darzee.shankh.entity.StitchOptions;
import com.darzee.shankh.enums.OutfitType;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StitchOptionsRepo extends JpaRepository<StitchOptions, Long> {

    List<StitchOptions> findAllByIdIn(List<Long> id);

    List<StitchOptions> findAllByOutfitTypeAndIsValidOrderById(OutfitType outfitType, Boolean isValid);

    Integer countByOutfitTypeAndIsValid(OutfitType outfitType, Boolean isValid);
}
