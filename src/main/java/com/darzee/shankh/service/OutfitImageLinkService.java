package com.darzee.shankh.service;

import com.darzee.shankh.enums.OutfitType;
import org.springframework.stereotype.Service;

import static com.darzee.shankh.constants.OutfitTypeLinks.*;

@Service
public class OutfitImageLinkService {

    public String getOutfitImageLink(OutfitType outfit) {
        switch(outfit) {
            case KURTA_PYJAMA:
                return OUTFIT_TYPE_KURTA_PYJAMA_LINK;
            case DRESS:
                return OUTFIT_TYPE_DRESS_LINK;
            case SAREE_BLOUSE:
                return OUTFIT_TYPE_SAREE_BLOUSE_LINK;
            case MENS_SUIT:
                return OUTFIT_TYPE_MENS_SUIT_LINK;
            case PANTS:
                return OUTFIT_TYPE_PANT_LINK;
            case EVENING_GOWN:
                return OUTFIT_TYPE_NIGHT_GOWN_LINK;
            case LADIES_SUIT:
                return OUTFIT_TYPE_LADIES_SUIT_LINK;
            case SHIRT:
                return OUTFIT_TYPE_SHIRT_LINK;
            case UNDER_SKIRT:
                return OUTFIT_TYPE_UNDER_SKIRT_LINK;
            case NEHRU_JACKET:
                return OUTFIT_TYPE_NEHRU_JACKET_LINK;
            case RIDA:
                return OUTFIT_TYPE_BURKHA_LINK;
            case WAIST_COAT:
                return OUTFIT_TYPE_WAIST_COAT_LINK;
            case LEHENGA:
                return OUTFIT_TYPE_LEHENGA_LINK;
            case SHARARA:
                return OUTFIT_TYPE_SHARARA_LINK;
            case SHERWANI:
                return OUTFIT_TYPE_SHERWANI_LINK;
            case INDO_WESTERN:
                return OUTFIT_TYPE_INDO_WESTERN_LINK;
            default:
                return null;
        }
    }
}