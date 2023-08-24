package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SocialMedia {

    WHATSAPP("whatsapp", 1),
    FACEBOOK("facebook", 2),
    INSTAGRAM("instagram", 3),
    TWITTER("twitter", 4);

    private String name;
    private Integer ordinal;

    SocialMedia(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }


    public static Map<Integer, SocialMedia> outfitOrdinalEnumMap = getSocialMediaOrdinalEnumMap();

    public static Map<Integer, SocialMedia> getSocialMediaOrdinalEnumMap() {
        Map<Integer, SocialMedia> socialMediaOrdinalMap = new HashMap<>();
        socialMediaOrdinalMap.put(WHATSAPP.ordinal, WHATSAPP);
        socialMediaOrdinalMap.put(FACEBOOK.ordinal, FACEBOOK);
        socialMediaOrdinalMap.put(INSTAGRAM.ordinal, INSTAGRAM);
        socialMediaOrdinalMap.put(TWITTER.ordinal, TWITTER);
        return socialMediaOrdinalMap;
    }

}
