package com.darzee.shankh.service.outfits;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitImageLinkService;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.SHERWANI_BOTTOM_OUTFIT_TYPE_HEADING;
import static com.darzee.shankh.constants.MeasurementTitles.SHERWANI_TOP_OUTFIT_TYPE_HEADING;

@Service
public class SherwaniImplService implements OutfitTypeService {

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.SHERWANI;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 2, isPortfolioEligible());
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
        return SHERWANI_TOP_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return SHERWANI_BOTTOM_OUTFIT_TYPE_HEADING;
    }
}
