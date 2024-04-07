package com.darzee.shankh.service.outfits;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.SHIRT_OUTFIT_TYPE_HEADING;

@Service
public class ShirtImplService implements OutfitTypeService {

    @Autowired
    private AmazonClient s3Client;
    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.SHIRT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                getOutfitImageLink(), 1, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(76, "Formal Shirts");
        subOutfitMap.put(77, "Casual Shirts");
        subOutfitMap.put(78, "Printed Shirts");
        subOutfitMap.put(79, "Striped Shirts");
        subOutfitMap.put(80, "Checkered Shirts");
        subOutfitMap.put(81, "Denim Shirts");
        subOutfitMap.put(82, "Polo Shirts");
        subOutfitMap.put(83, "Kurta Shirts");
        subOutfitMap.put(84, "Nehrucollar Shirts");
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
        return SHIRT_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return "";
    }

    @Override
    public String getOutfitImageLink() {
        return s3Client.generateShortLivedUrlForOutfit("shirt.svg");
    }
}
