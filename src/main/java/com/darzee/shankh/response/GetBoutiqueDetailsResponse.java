package com.darzee.shankh.response;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class GetBoutiqueDetailsResponse {
    private Long boutiqueId;
    private String boutiqueName;
    private Integer tailorCount;

    private Integer language;

    private List<String> shopImagesRefId;
    private List<String> shopImageUrls;
    private String adminTailorName;
    private String adminTailorPhoneNumber;

    private String adminTailorRefId;
    private Integer boutiqueType;
    private String adminTailorProfilePicUrl;


    public GetBoutiqueDetailsResponse(BoutiqueDAO boutiqueDAO, TailorDAO tailorDAO, List<String> shopImagesRefId,
                                      List<String> shopImagesUrl, String adminTailorRefId, String adminTailorImageUrl) {
        this.boutiqueId = boutiqueDAO.getId();
        this.boutiqueName = boutiqueDAO.getName();
        this.tailorCount = boutiqueDAO.getTailorCount();
        this.boutiqueType = boutiqueDAO.getBoutiqueType().getOrdinal();
        this.shopImagesRefId = shopImagesRefId;
        this.shopImageUrls = shopImagesUrl;
        if (tailorDAO != null) {
            this.adminTailorName = tailorDAO.getName();
            this.adminTailorPhoneNumber = tailorDAO.getPhoneNumber();
            this.adminTailorRefId = adminTailorRefId;
            this.adminTailorProfilePicUrl = adminTailorImageUrl;
            if (tailorDAO.getLanguage() != null) {
                this.language = tailorDAO.getLanguage().getOrdinal();
            }
        }
    }

    public GetBoutiqueDetailsResponse(BoutiqueDAO boutiqueDAO, TailorDAO tailorDAO) {
        this.boutiqueId = boutiqueDAO.getId();
        this.boutiqueName = boutiqueDAO.getName();
        this.tailorCount = boutiqueDAO.getTailorCount();
        this.boutiqueType = boutiqueDAO.getBoutiqueType().getOrdinal();
        if (tailorDAO != null) {
            this.adminTailorName = tailorDAO.getName();
            this.adminTailorPhoneNumber = tailorDAO.getPhoneNumber();
        }
    }
}
