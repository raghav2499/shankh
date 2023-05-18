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

    private List<String> shopImageReferences;
    private String adminTailorName;
    private String adminTailorPhoneNumber;
    private Integer boutiqueType;
    private String adminTailorProfilePicReferenceId;


    public GetBoutiqueDetailsResponse(BoutiqueDAO boutiqueDAO, TailorDAO tailorDAO,
                                      List<String> shopImageReferences) {
        this.boutiqueId = boutiqueDAO.getId();
        this.boutiqueName = boutiqueDAO.getName();
        this.tailorCount = boutiqueDAO.getTailorCount();
        this.boutiqueType = boutiqueDAO.getBoutiqueType().getOrdinal();
        this.shopImageReferences = shopImageReferences;
        if (tailorDAO != null) {
            this.adminTailorName = tailorDAO.getName();
            this.adminTailorPhoneNumber = tailorDAO.getPhoneNumber();
            this.adminTailorProfilePicReferenceId = tailorDAO.getProfilePicReferenceId();
        }
    }
}
