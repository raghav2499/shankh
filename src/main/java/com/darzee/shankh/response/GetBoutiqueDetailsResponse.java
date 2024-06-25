package com.darzee.shankh.response;

import com.darzee.shankh.dao.AddressDAO;
import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class GetBoutiqueDetailsResponse {
    private Long boutiqueId;
    private String boutiqueName;
    private Integer tailorCount;
    private String language;
    private List<String> shopImagesRefId;
    private List<String> shopImageUrls;
    private String adminTailorName;
    private String adminTailorPhoneNumber;
    private String adminTailorRefId;
    private Integer boutiqueType;
    private String adminTailorProfilePicUrl;
    private String boutiquePhoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String gstNumber;
    private BigDecimal gstRate;
    private Boolean includeDeliveryDate;

    private String portfolioLink;


    public GetBoutiqueDetailsResponse(BoutiqueDAO boutiqueDAO, TailorDAO tailorDAO, List<String> shopImagesRefId,
                                      List<String> shopImagesUrl, String adminTailorRefId, String adminTailorImageUrl,
                                      String portfolioLink,AddressDAO addressDAO) {
        this.boutiqueId = boutiqueDAO.getId();
        this.boutiqueName = boutiqueDAO.getName();
        this.tailorCount = boutiqueDAO.getTailorCount();
        this.boutiqueType = boutiqueDAO.getBoutiqueType().getOrdinal();
        this.shopImagesRefId = shopImagesRefId;
        this.shopImageUrls = shopImagesUrl;
        if(addressDAO!=null){
            this.addressLine1 = addressDAO.getAddressLine1();
            this.addressLine2 = addressDAO.getAddressLine2();
            this.city = addressDAO.getCity();
            this.state = addressDAO.getState();
            this.country = addressDAO.getCountry();
            this.postalCode = addressDAO.getPostalCode();
        }
        this.gstNumber = boutiqueDAO.getGstNumber();
        this.gstRate = boutiqueDAO.getGstRate();
        this.includeDeliveryDate =boutiqueDAO.getIncludeDeliveryDate();
        if (tailorDAO != null) {
            this.adminTailorName = tailorDAO.getName();
            this.adminTailorPhoneNumber = tailorDAO.getPhoneNumber();
            this.adminTailorRefId = adminTailorRefId;
            this.adminTailorProfilePicUrl = adminTailorImageUrl;
            if (tailorDAO.getLanguage() != null) {
                this.language = tailorDAO.getLanguage().getNotation();
            }
            this.portfolioLink = portfolioLink;
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
