package com.darzee.shankh.dao;

import com.darzee.shankh.enums.Language;
import com.darzee.shankh.enums.TailorRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
    private PortfolioDAO portfolio;

    public TailorDAO(String name, TailorRole role, Language language, String phoneNumber, BoutiqueDAO boutique) {
        this.name = name;
        this.role = role;
        this.language = language;
        this.boutique = boutique;
        this.phoneNumber = phoneNumber;
    }

    public TailorDAO(String name, TailorRole role, Language language, String phoneNumber,
                     BoutiqueDAO boutique, PortfolioDAO portfolioDAO) {
        this.name = name;
        this.role = role;
        this.language = language;
        this.boutique = boutique;
        this.portfolio = portfolioDAO;
        this.phoneNumber = phoneNumber;
    }

    public boolean isNameUpdated(String value) {
        return value != null & !this.name.equals(value);
    }
    public boolean isPhoneNumberUpdated(String value) {
        return value != null & !this.phoneNumber.equals(value);
    }

    public boolean isLanguageUpdated(Integer value) {
        return value != null && !value.equals(this.language);
    }

}
