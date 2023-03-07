package com.darzee.shankh.repo;

import com.darzee.shankh.entity.BoutiqueLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BoutiqueLedgerRepo  extends JpaRepository<BoutiqueLedger, Long> {

    BoutiqueLedger findByBoutiqueId(Long boutiqueId);
}
