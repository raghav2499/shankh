package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Tailor;
import com.darzee.shankh.enums.TailorRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TailorRepo extends JpaRepository<Tailor, Long>{

    Tailor findByBoutiqueIdAndRole(Long boutiqueId, TailorRole role);

    Optional<Tailor> findByPhoneNumber(String phoneNumber);
}

