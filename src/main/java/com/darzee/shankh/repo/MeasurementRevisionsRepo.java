package com.darzee.shankh.repo;

import com.darzee.shankh.entity.MeasurementRevisions;
import com.darzee.shankh.enums.OutfitType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeasurementRevisionsRepo extends JpaRepository<MeasurementRevisions, Long> {

    List<MeasurementRevisions> findAllByCustomerIdAndOutfitType(Long customerId, OutfitType outfitType);
    MeasurementRevisions findByCustomerIdAndOutfitTypeOrderByIdDesc(Long customerId, OutfitType outfitType);

    @Query("SELECT mr FROM MeasurementRevisions mr WHERE mr.customerId = :customerId AND mr.outfitType = :outfitType ORDER BY mr.id DESC")
    MeasurementRevisions findLatestByCustomerIdAndOutfitType(@Param("customerId") Long customerId,
                                                             @Param("outfitType") OutfitType outfitType,
                                                             Pageable pageable);

}
