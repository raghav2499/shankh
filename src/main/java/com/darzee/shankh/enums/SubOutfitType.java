package com.darzee.shankh.enums;

import java.util.HashMap;
import java.util.Map;

public interface SubOutfitType {
    Map<Integer, SubOutfitType> subOutfitEnumMap = new HashMap<>();

    Map<Integer, SubOutfitType> getSubOutfitEnumMap();
}
