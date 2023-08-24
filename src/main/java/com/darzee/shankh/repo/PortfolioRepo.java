package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {

    Portfolio findByUsername(String name);
}
