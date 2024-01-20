package com.darzee.shankh.repo;

import com.darzee.shankh.entity.MeasurementRevisions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRevisionsRepo extends JpaRepository<MeasurementRevisions, Long> {

    List<MeasurementRevisions> findAllByCustomerIdAndOutfitType(Long customerId, Integer outfitType);
}
