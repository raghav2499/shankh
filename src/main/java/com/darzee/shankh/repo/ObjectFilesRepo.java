package com.darzee.shankh.repo;

import com.darzee.shankh.entity.ObjectFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectFilesRepo extends JpaRepository<ObjectFiles, Long> {

    Optional<ObjectFiles> findByEntityIdAndEntityTypeAndIsValid(Long entityId, String entityType, boolean isValid);

    Optional<ObjectFiles> findByReferenceIdAndIsValid(String referenceId, Boolean isValid);

    List<ObjectFiles> findAllByEntityIdAndEntityTypeAndIsValid(Long entityId, String entityType, boolean isValid);
}