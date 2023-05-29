package com.darzee.shankh.repo;

import com.darzee.shankh.entity.BoutiqueLedgerSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoutiqueLedgerSnapshotRepo extends JpaRepository<BoutiqueLedgerSnapshot, Long> {

    Optional<BoutiqueLedgerSnapshot> findByBoutiqueIdAndMonthAndYear(Long boutiqueId, Integer month, Integer year);
}
