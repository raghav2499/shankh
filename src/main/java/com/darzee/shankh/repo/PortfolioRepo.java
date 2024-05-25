package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {

    @Query("SELECT p FROM Portfolio p WHERE SIZE(p.portfolioOutfits) > 0 ORDER BY SIZE(p.portfolioOutfits) DESC")
    List<Portfolio> findAllWithOutfitsOrderByOutfitsDesc();
    List<Portfolio> findAllByOrderByCreatedAtDesc();
    Optional<Portfolio> findByUsername(String name);
    Optional<Portfolio> findByTailorId(Long tailorId);
}
