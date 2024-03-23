package com.darzee.shankh.service.outfits;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitImageLinkService;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.NEHRU_JACKET_OUTFIT_TYPE_HEADING;

@Service
public class NehruJacketImplService implements OutfitTypeService {

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;
    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.NEHRU_JACKET;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 1, isPortfolioEligible());
    }
    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(39, "Classic Nehru Jacket");
        subOutfitMap.put(40, "Embroidered Nehru Jacket");
        subOutfitMap.put(41, "Printed Nehru Jacket");
        subOutfitMap.put(42, "Velvet Nehru Jacket");
        subOutfitMap.put(43, "Linen Nehru Jacket");
        subOutfitMap.put(44, "Silk Nehru Jacket");
        subOutfitMap.put(45, "Textured Nehru Jacket");
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
        return NEHRU_JACKET_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return "";
    }
}
