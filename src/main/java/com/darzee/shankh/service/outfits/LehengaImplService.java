package com.darzee.shankh.service.outfits;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.LEHENGA_OUTFIT_TYPE_HEADING;

@Service
public class LehengaImplService implements OutfitTypeService {

    @Autowired
    private AmazonClient s3Client;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.LEHENGA;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                getOutfitImageLink(), 1, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();

        subOutfitMap.put(17, "A Line Lehenga");
        subOutfitMap.put(18, "Circular Lehenga");
        subOutfitMap.put(19, "Mermaid Lehenga");
        subOutfitMap.put(20, "Panelled Lehenga");
        subOutfitMap.put(21, "Jacket Lehenga");
        subOutfitMap.put(22, "Sharara Lehenga");
        subOutfitMap.put(23, "Trail Lehenga");
        subOutfitMap.put(24, "Lehenga Saree");
        subOutfitMap.put(25, "Flared Lehenga");
        subOutfitMap.put(26, "Ruffled Lehenga");
        subOutfitMap.put(27, "Straight Cut Lehenga");
        subOutfitMap.put(28, "Half Saree Lehenga");
        subOutfitMap.put(29, "Lehenga With Cape");
        subOutfitMap.put(30, "Asymmetric Lehenga");
        subOutfitMap.put(31, "Tiered Lehenga");
        subOutfitMap.put(32, "Indowestern Lehenga");
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
        return LEHENGA_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return "";
    }

    @Override
    public String getOutfitImageLink() {
        return s3Client.generateShortLivedUrlForOutfit("lehenga.svg");
    }
}
