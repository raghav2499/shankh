package com.darzee.shankh.service.outfits;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.PANTS_OUTFIT_TYPE_HEADING;

@Service
public class PantImplService implements OutfitTypeService {

    @Autowired
    private AmazonClient s3Client;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.PANTS;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                getOutfitImageLink(), 1, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(51, "Formal Trousers");
        subOutfitMap.put(52, "Chinos");
        subOutfitMap.put(53, "Jeans");
        subOutfitMap.put(54, "Cargos");
        subOutfitMap.put(55, "Trackpants");
        subOutfitMap.put(56, "Joggers");
        subOutfitMap.put(57, "Formal Trousers With Ethnic Touch");
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
        return PANTS_OUTFIT_TYPE_HEADING;
    }


    @Override
    public String getOutfitImageLink() {
        return s3Client.generateShortLivedUrlForOutfit("/pants.svg");
    }
}
