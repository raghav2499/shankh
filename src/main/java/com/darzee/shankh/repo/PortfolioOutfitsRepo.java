package com.darzee.shankh.repo;

import com.darzee.shankh.entity.PortfolioOutfits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioOutfitsRepo extends JpaRepository<PortfolioOutfits, Long> {
}
