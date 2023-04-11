package com.darzee.shankh.repo;

import com.darzee.shankh.entity.ImageReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageReferenceRepo extends JpaRepository<ImageReference, Long> {

    List<ImageReference> findAllByReferenceIdIn(List<String> referenceId);
    Optional<ImageReference> findByReferenceId(String referenceId);
}
