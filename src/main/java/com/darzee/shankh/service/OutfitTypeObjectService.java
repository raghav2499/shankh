package com.darzee.shankh.service;

import com.darzee.shankh.enums.OutfitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutfitTypeObjectService {

    @Autowired
    private KurtaPyjamaImplService kurtaPyjamaImplService;

    @Autowired
    private SareeBlouseImplService sareeBlouseImplService;

    @Autowired
    private EveningGownImplService eveningGownImplService;

    @Autowired
    private MensSuitImplService mensSuitImplService;

    @Autowired
    private LadiesSuitImplService ladiesSuitImplService;

    @Autowired
    private DressImplService dressImplService;

    @Autowired
    private ShirtImplService shirtImplService;

    @Autowired
    private PantImplService pantImplService;

    @Autowired
    private UnderSkirtImplService underSkirtImplService;

    public OutfitTypeService getOutfitTypeObject(OutfitType outfitType) throws Exception {
        switch(outfitType) {
            case KURTA_PYJAMA:
                return kurtaPyjamaImplService;
            case SAREE_BLOUSE:
                return sareeBlouseImplService;
            case EVENING_GOWN:
                return eveningGownImplService;
            case MENS_SUIT:
                return mensSuitImplService;
            case LADIES_SUIT:
                return ladiesSuitImplService;
            case DRESS:
                return dressImplService;
            case SHIRT:
                return shirtImplService;
            case PANTS:
                return pantImplService;
            case UNDER_SKIRT:
                return underSkirtImplService;
            default:
                throw new Exception("Outfit Type not supported");
        }
    }
}
