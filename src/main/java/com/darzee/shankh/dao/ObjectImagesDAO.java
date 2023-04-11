package com.darzee.shankh.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectImagesDAO {
    private Long id;
    private String referenceId;
    private Boolean isValid = Boolean.TRUE;
    private String entityType;
    private Long entityId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ObjectImagesDAO(String referenceId, String entityType, Long entityId) {
        this.referenceId = referenceId;
        this.entityId = entityId;
        this.entityType = entityType;
    }
}
