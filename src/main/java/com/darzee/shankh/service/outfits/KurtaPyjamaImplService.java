package com.darzee.shankh.service.outfits;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.MENS_KURTA_OUTFIT_TYPE_HEADING;
import static com.darzee.shankh.constants.MeasurementTitles.MENS_PYJAMA_OUTFIT_TYPE_HEADING;

@Service
public class KurtaPyjamaImplService implements OutfitTypeService {

    @Autowired
    private AmazonClient s3Client;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.KURTA_PYJAMA;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                getOutfitImageLink(), 2, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(4, "Pathani Suit");
        subOutfitMap.put(5, "Sherwani");
        subOutfitMap.put(6, "Achkan");
        subOutfitMap.put(7, "Jodhpuri Suit");
        subOutfitMap.put(8, "Indo Western Kurta Pyjama");
        subOutfitMap.put(9, "Designer Kurta Pyjama");
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
        return MENS_KURTA_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return MENS_PYJAMA_OUTFIT_TYPE_HEADING;
    }


    @Override
    public String getOutfitImageLink() {
        return s3Client.generateShortLivedUrlForOutfit("Mens'+kurta.jpg");
    }
}
