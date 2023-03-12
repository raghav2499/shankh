package com.darzee.shankh.dao;

import com.darzee.shankh.enums.Language;
import com.darzee.shankh.enums.TailorRole;
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
    private Language language;
    private TailorRole role;
    private String phoneNumber;
    private BoutiqueDAO boutique;

    public TailorDAO(String name, TailorRole role, Language language, String phoneNumber, BoutiqueDAO boutique) {
        this.name = name;
        this.role = role;
        this.language = language;
        this.boutique = boutique;
        this.phoneNumber = phoneNumber;
    }

}
