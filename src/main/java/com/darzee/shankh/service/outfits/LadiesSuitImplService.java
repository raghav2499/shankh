package com.darzee.shankh.service.outfits;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.LADIES_SUIT_KAMEEZ_OUTFIT_TYPE_HEADING;
import static com.darzee.shankh.constants.MeasurementTitles.LADIES_SUIT_SALWAR_OUTFIT_TYPE_HEADING;

@Service
public class LadiesSuitImplService implements OutfitTypeService {

    @Autowired
    private AmazonClient s3Client;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.LADIES_SUIT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                getOutfitImageLink(), 2, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(10, "Salwar Kameez");
        subOutfitMap.put(11, "Anarkali Suit");
        subOutfitMap.put(12, "Churidar Suit");
        subOutfitMap.put(13, "Patiala Suit");
        subOutfitMap.put(14, "Palazzo Suit");
        subOutfitMap.put(15, "Sharara Suit");
        subOutfitMap.put(16, "Lehenga Suit");
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
        return LADIES_SUIT_KAMEEZ_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return LADIES_SUIT_SALWAR_OUTFIT_TYPE_HEADING;
    }


    @Override
    public String getOutfitImageLink() {
        return s3Client.generateShortLivedUrlForOutfit("womenssuit.jpg");
    }
}
