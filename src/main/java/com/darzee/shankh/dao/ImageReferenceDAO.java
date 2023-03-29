package com.darzee.shankh.dao;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ImageReferenceDAO {

    private Long id;
    private String referenceId;
    private String imageName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ImageReferenceDAO(String referenceId, String imageName) {
        this.referenceId = referenceId;
        this.imageName = imageName;
    }
}
