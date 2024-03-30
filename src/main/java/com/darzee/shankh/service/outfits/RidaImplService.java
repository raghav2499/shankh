package com.darzee.shankh.service.outfits;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitImageLinkService;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.MeasurementTitles.RIDA_LENGA_OUTFIT_TYPE_HEADING;
import static com.darzee.shankh.constants.MeasurementTitles.RIDA_PARDI_OUTFIT_TYPE_HEADING;

@Service
public class RidaImplService implements OutfitTypeService {

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.RIDA;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 2, isPortfolioEligible());
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(58, "Plain Rida");
        subOutfitMap.put(59, "Embroidered Rida");
        subOutfitMap.put(60, "Printed Rida");
        subOutfitMap.put(61, "Lace Border Rida");
        subOutfitMap.put(62, "Patchwork Rida");
        subOutfitMap.put(63, "Traditional Rida");
        subOutfitMap.put(64, "Formal Rida");
        subOutfitMap.put(65, "Eid Special Rida");
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
        return RIDA_PARDI_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return RIDA_LENGA_OUTFIT_TYPE_HEADING;
    }
}
