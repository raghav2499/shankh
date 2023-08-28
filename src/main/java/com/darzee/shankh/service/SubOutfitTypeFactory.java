package com.darzee.shankh.service;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.enums.SubOutfitType;
import com.darzee.shankh.enums.sub_outfits.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SubOutfitTypeFactory {

    public SubOutfitType getSubOutfit(OutfitType outfitType, Integer subOutfitOrdinal) {
        SubOutfitType subOutfitType = null;
        switch (outfitType) {
            case DRESS:
                subOutfitType = DressSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case KURTA_PYJAMA:
                subOutfitType = KurtaPyjamaSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case LADIES_SUIT:
                subOutfitType = LadiesSuitSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case LEHENGA:
                subOutfitType = LehengaSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case MENS_SUIT:
                subOutfitType = MensSuitSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case NEHRU_JACKET:
                subOutfitType = NehruJacketSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case EVENING_GOWN:
                subOutfitType = NightGownSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case PANTS:
                subOutfitType = PantsSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case RIDA:
                subOutfitType = RidaSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case SAREE_BLOUSE:
                subOutfitType = SareeBlouseSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case SHIRT:
                subOutfitType = ShirtSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case UNDER_SKIRT:
                subOutfitType = UnderSkirtSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            case WAIST_COAT:
                subOutfitType = WaistcoatSubOutfits.getSubOutfitEnumMap().get(subOutfitOrdinal);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Outfit Type");
        }
        return subOutfitType;
    }
}
