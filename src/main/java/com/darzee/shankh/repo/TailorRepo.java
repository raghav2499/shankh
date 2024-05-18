package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Tailor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TailorRepo extends JpaRepository<Tailor, Long>{
    Optional<Tailor> findByPhoneNumber(String phoneNumber);

    Optional<Tailor> findByCountryCodeAndPhoneNumber(String countryCode, String phoneNumber);

    @Query(value = "SELECT name FROM tailor where id = :tailorId", nativeQuery = true)
    String findNameById(@Param("tailorId") Long tailorId);
}

