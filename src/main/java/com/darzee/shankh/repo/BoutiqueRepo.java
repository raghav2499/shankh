package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Boutique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoutiqueRepo extends JpaRepository<Boutique, Long> {

}
