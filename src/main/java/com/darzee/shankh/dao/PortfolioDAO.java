package com.darzee.shankh.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
public class PortfolioDAO {

    private Long id;

    private String username;

    private Integer usernameUpdatesCounts;

    private String aboutDetails;

    private Map<String, String> socialMedia;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<PortfolioOutfitsDAO> portfolioOutfits;

    private TailorDAO tailor;

    public PortfolioDAO(String username, String aboutDetails, Map<String, String> socialMedia, TailorDAO tailor) {
        this.username = username;
        this.aboutDetails = aboutDetails;
        this.socialMedia = socialMedia;
        this.tailor = tailor;
        this.usernameUpdatesCounts = this.usernameUpdatesCounts+1;
    }
}
