package com.darzee.shankh.dao;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TailorDAO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer pay;
    private Boolean isOwner;
    private BoutiqueDAO boutique;

    public TailorDAO(String name, Integer pay, Boolean isOwner, BoutiqueDAO boutique) {
        this.name = name;
        this.pay = pay;
        this.isOwner = isOwner;
        this.boutique = boutique;
    }

}
