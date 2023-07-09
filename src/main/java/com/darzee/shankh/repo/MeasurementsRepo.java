package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Measurements;
import com.darzee.shankh.enums.OutfitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface MeasurementsRepo extends JpaRepository<Measurements, Long> {

    @Nullable
    Optional<Measurements> findByCustomerIdAndOutfitType(Long customerId, OutfitType outfitType);
}
