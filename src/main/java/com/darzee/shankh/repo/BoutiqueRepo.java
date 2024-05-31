package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Boutique;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoutiqueRepo extends JpaRepository<Boutique, Long> {

    Boutique findByBoutiqueReferenceId(String boutiqueReferenceId);

    @Query(value = "SELECT name FROM boutique where admin_tailor_id = :adminTailorId", nativeQuery = true)
    String findNameByAdminTailorId(@Param("adminTailorId") Long adminTailorId);

    @Query(value = "SELECT b.id, b.name, COUNT(o.id) AS order_count, d.device_token " +
    "FROM Boutique b " +
    "LEFT JOIN Order o ON b.id = o.boutiqueId " +
    "LEFT JOIN DeviceInfo d ON b.adminTailorId = d.tailorId " +
    "WHERE d.deviceToken IS NOT NULL " +
    "GROUP BY b.id, b.name, d.deviceToken " +
    "HAVING COUNT(o.id) = 0", nativeQuery = true)
    List<Object[]> findBoutiquesWithZeroOrdersAndDeviceToken();
}
