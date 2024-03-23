package com.darzee.shankh.service.outfits;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitImageLinkService;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.WAIST_COAT_OUTFIT_TYPE_HEADING;

@Service
public class WaistCoatImplSevice implements OutfitTypeService {
    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.WAIST_COAT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 1, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(92, "Single Breasted Waistcoat");
        subOutfitMap.put(93, "Double Breasted Waistcoat");
        subOutfitMap.put(94, "Shawl Collar Waistcoat");
        subOutfitMap.put(95, "Notch Collar Waistcoat");
        subOutfitMap.put(96, "Mandarin Collar Waistcoat");
        subOutfitMap.put(97, "Full Back Waistcoat");
        subOutfitMap.put(98, "Adjustable Back Waistcoat");
        subOutfitMap.put(99, "Patterned Waistcoat");
        subOutfitMap.put(100, "Silk Waistcoat");
        subOutfitMap.put(101, "Textured Waistcoat");

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
        return WAIST_COAT_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return "";
    }
}

