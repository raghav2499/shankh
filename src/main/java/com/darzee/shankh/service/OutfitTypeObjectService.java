package com.darzee.shankh.service;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.service.outfits.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private NehruJacketImplService nehruJacketImplService;

    @Autowired
    private RidaImplService ridaImplService;

    @Autowired
    private WaistCoatImplSevice waistCoatImplSevice;

    @Autowired
    private LehengaImplService lehengaImplService;

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
            case NEHRU_JACKET:
                return nehruJacketImplService;
            case RIDA:
                return ridaImplService;
            case WAIST_COAT:
                return waistCoatImplSevice;
            case LEHENGA:
                return lehengaImplService;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Outfit Type not supported");
        }
    }
}
