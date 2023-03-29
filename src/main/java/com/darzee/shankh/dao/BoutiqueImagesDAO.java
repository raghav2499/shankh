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
public class BoutiqueImagesDAO {
    private Long id;
    private String referenceId;
    private Boolean isValid;
    private BoutiqueDAO boutique;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoutiqueImagesDAO(String referenceId, Boolean isValid, BoutiqueDAO boutique) {
        this.referenceId = referenceId;
        this.isValid = isValid;
        this.boutique = boutique;
    }
}
