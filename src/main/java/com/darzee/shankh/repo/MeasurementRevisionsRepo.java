package com.darzee.shankh.repo;

import com.darzee.shankh.entity.MeasurementRevisions;
import com.darzee.shankh.enums.OutfitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRevisionsRepo extends JpaRepository<MeasurementRevisions, Long> {

    List<MeasurementRevisions> findAllByCustomerIdAndOutfitTypeOrderByCreatedAtDesc(Long customerId, OutfitType outfitType);
    MeasurementRevisions findTopByCustomerIdAndOutfitTypeOrderByIdDesc(Long customerId, OutfitType outfitType);

}
