package com.darzee.shankh.repo;

import com.darzee.shankh.entity.BoutiqueImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoutiqueImagesRepo extends JpaRepository<BoutiqueImages, Long> {
}