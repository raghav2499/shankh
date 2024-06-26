package com.darzee.shankh.service.outfits;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.INDO_WESTERN_BOTTOM_OUTFIT_TYPE_HEADING;
import static com.darzee.shankh.constants.MeasurementTitles.INDO_WESTERN_TOP_OUTFIT_TYPE_HEADING;

@Service
public class IndoWesternImplService implements OutfitTypeService {

    @Autowired
    private AmazonClient s3Client;
    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.INDO_WESTERN;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                getOutfitImageLink(), 2, isPortfolioEligible());
    }
    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        return subOutfitMap;
    }

    public String getSubOutfitName(Integer ordinal) {
        return getSubOutfitMap().get(ordinal);
    }

    @Override
    public boolean isPortfolioEligible() {
        return false;
    }

    @Override
    public String getTopHeading() {
        return INDO_WESTERN_TOP_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return INDO_WESTERN_BOTTOM_OUTFIT_TYPE_HEADING;
    }


    @Override
    public String getOutfitImageLink() {
        return s3Client.generateShortLivedUrlForOutfit("indo_western.jpg");
    }
}
