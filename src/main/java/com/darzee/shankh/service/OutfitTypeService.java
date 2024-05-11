package com.darzee.shankh.service;

import com.darzee.shankh.response.OutfitDetails;

import java.util.Map;

public interface OutfitTypeService {

    public OutfitDetails getOutfitDetails();

    public Map<Integer, String> getSubOutfitMap();

    public String getSubOutfitName(Integer ordinal);

    public boolean isPortfolioEligible();

    public String getTopHeading();

    public String getBottomHeading();

    public String getOutfitImageLink();
}
