package com.darzee.shankh.service.outfits;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.UNDER_SHIRT_OUTFIT_TYPE_HEADING;

@Service
public class UnderSkirtImplService implements OutfitTypeService {

    @Autowired
    private AmazonClient s3Client;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.UNDER_SKIRT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                getOutfitImageLink(), 1, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(85, "Cotton Petticoats");
        subOutfitMap.put(86, "Silk Petticoats");
        subOutfitMap.put(87, "Satin Petticoats");
        subOutfitMap.put(88, "Printed Petticoats");
        subOutfitMap.put(89, "Ruffled Petticoats");
        subOutfitMap.put(90, "A Line Petticoats");
        subOutfitMap.put(91, "Drawstring Petticoats");
        return subOutfitMap;
    }

    public String getSubOutfitName(Integer ordinal) {
        return getSubOutfitMap().get(ordinal);
    }

    @Override
    public boolean isPortfolioEligible() {
        return true;
    }

    @Override
    public String getTopHeading() {
        return "";
    }

    @Override
    public String getBottomHeading() {
        return UNDER_SHIRT_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getOutfitImageLink() {
        return s3Client.generateShortLivedUrlForOutfit("underskirt.svg");
    }
}
