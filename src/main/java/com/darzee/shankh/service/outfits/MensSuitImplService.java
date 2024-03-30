package com.darzee.shankh.service.outfits;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.MENS_SUIT_PANTS_OUTFIT_TYPE_HEADING;
import static com.darzee.shankh.constants.MeasurementTitles.MENS_SUIT_TOP_OUTFIT_TYPE_HEADING;

@Service
public class MensSuitImplService implements OutfitTypeService {

    @Autowired
    private AmazonClient s3Client;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.MENS_SUIT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                getOutfitImageLink(), 2, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(33, "Formal Business Suit");
        subOutfitMap.put(34, "Wedding Suit");
        subOutfitMap.put(35, "Bandhgala Suit");
        subOutfitMap.put(36, "Jodhpuri Suit");
        subOutfitMap.put(37, "Indo Western Suit");
        subOutfitMap.put(38, "Tuxedo");
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
        return MENS_SUIT_TOP_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return MENS_SUIT_PANTS_OUTFIT_TYPE_HEADING;
    }


    @Override
    public String getOutfitImageLink() {
        return s3Client.generateShortLivedUrlForOutfit("/mens+suit.svg");
    }
}
