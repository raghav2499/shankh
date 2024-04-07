package com.darzee.shankh.service.outfits;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.SAREE_BLOUSE_OUTFIT_TYPE_HEADING;

@Service
public class SareeBlouseImplService implements OutfitTypeService {

    @Autowired
    private AmazonClient s3Client;
    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.SAREE_BLOUSE;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                getOutfitImageLink(), 1, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(66, "Classic Blouse");
        subOutfitMap.put(67, "Princess Cut Blouse");
        subOutfitMap.put(68, "High Neck Blouse");
        subOutfitMap.put(69, "Halter Neck Blouse");
        subOutfitMap.put(70, "Backless Blouse");
        subOutfitMap.put(71, "Jacket Blouse");
        subOutfitMap.put(72, "Peplum Blouse");
        subOutfitMap.put(73, "Sheer Blouse");
        subOutfitMap.put(74, "Designer Blouse");
        subOutfitMap.put(75, "Printed Blouse");
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
        return SAREE_BLOUSE_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return "";
    }

    @Override
    public String getOutfitImageLink() {
        return s3Client.generateShortLivedUrlForOutfit("saree+blouse.svg");
    }
}
