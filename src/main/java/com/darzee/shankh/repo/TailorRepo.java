package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Tailor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TailorRepo extends JpaRepository<Tailor, Long>{

    Tailor findByBoutiqueIdAndIsOwner(Long boutiqueId, Boolean isOwner);
}

