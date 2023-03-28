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
    private String url;
    private Boolean isValid;
    private BoutiqueDAO boutique;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoutiqueImagesDAO(String url, Boolean isValid, BoutiqueDAO boutique) {
        this.url = url;
        this.isValid = isValid;
        this.boutique = boutique;
    }
}
