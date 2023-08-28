package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Boutique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoutiqueRepo extends JpaRepository<Boutique, Long> {

    Boutique findByBoutiqueReferenceId(String boutiqueReferenceId);

    @Query(value = "SELECT name FROM boutique where admin_tailor_id = :adminTailorId", nativeQuery = true)
    String findNameByAdminTailorId(@Param("adminTailorId") Long adminTailorId);
}
