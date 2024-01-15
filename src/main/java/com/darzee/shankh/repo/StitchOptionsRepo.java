package com.darzee.shankh.repo;

import com.darzee.shankh.entity.StitchOptions;
import com.darzee.shankh.enums.OutfitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StitchOptionsRepo extends JpaRepository<StitchOptions, Long> {

    List<StitchOptions> findAllByOutfitTypeAndIsValid(OutfitType outfitType, Boolean isValid);
}
