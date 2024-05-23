package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {

    @Query("SELECT p FROM Portfolio p LEFT JOIN p.portfolioOutfits po GROUP BY p ORDER BY COUNT(po) DESC")
    List<Portfolio> findAllOrderByOutfitsDesc();
    List<Portfolio> findAllByOrderByCreatedAtDesc();
    Optional<Portfolio> findByUsername(String name);
    Optional<Portfolio> findByTailorId(Long tailorId);
}
