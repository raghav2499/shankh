package com.darzee.shankh.repo;

import com.darzee.shankh.entity.ObjectImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectImagesRepo extends JpaRepository<ObjectImages, Long> {
}