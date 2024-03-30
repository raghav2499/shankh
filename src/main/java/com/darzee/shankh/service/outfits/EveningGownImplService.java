package com.darzee.shankh.service.outfits;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitImageLinkService;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.NIGHT_GOWN_OUTFIT_TYPE_HEADING;

@Service
public class EveningGownImplService implements OutfitTypeService {

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.EVENING_GOWN;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 1, isPortfolioEligible());
    }

    @Override
    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(46, "Short Nighties");
        subOutfitMap.put(47, "Long Nighties");
        subOutfitMap.put(48, "Printed Nighties");
        subOutfitMap.put(49, "Satin Nightgowns");
        subOutfitMap.put(50, "Maternity Nightgowns");
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
        return NIGHT_GOWN_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return "";
    }
}
