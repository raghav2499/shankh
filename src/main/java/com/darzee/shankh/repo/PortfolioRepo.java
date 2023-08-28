package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByUsername(String name);
    Optional<Portfolio> findByTailorId(Long tailorId);
}
