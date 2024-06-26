package com.darzee.shankh.service;

import com.darzee.shankh.enums.Gender;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.repo.StitchOptionsRepo;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.response.PortfolioSubOutfit;
import com.darzee.shankh.response.SubOutfitTypeDetailResponse;
import com.darzee.shankh.service.translator.OutfitTranslator;
import com.darzee.shankh.service.translator.PortfolioSuboutfitTranslator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OutfitService {

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    @Autowired
    private StitchOptionsRepo stitchOptionsRepo;

    @Autowired
    private OutfitTranslator outfitTranslator;

    @Autowired
    private PortfolioSuboutfitTranslator portfolioSuboutfitTranslator;

    public ResponseEntity getOutfitDetails(String gender) throws Exception {
        List<OutfitType> outfitTypes = getOutfitTypes(gender);
        List<OutfitDetails> outfitDetailsList = new ArrayList<>(outfitTypes.size());
        for (OutfitType outfitType : outfitTypes) {
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
            OutfitDetails outfitDetails = outfitTypeService.getOutfitDetails();
            boolean stitchOptionsExist = (stitchOptionsRepo.countByOutfitTypeAndIsValid(outfitType, Boolean.TRUE) > 0);
            outfitDetails.setStitchOptionsExist(stitchOptionsExist);
            outfitDetailsList.add(outfitDetails);
        }
        outfitDetailsList = outfitTranslator.getTranslatedOutfitDetailsList(outfitDetailsList);
        return new ResponseEntity(outfitDetailsList, HttpStatus.OK);
    }

    public ResponseEntity<SubOutfitTypeDetailResponse> getSubOutfits(Integer outfitTypeOrdinal) throws Exception {
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitTypeOrdinal);
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        Map<Integer, String> subOutfitDetails = outfitTypeService.getSubOutfitMap();
        List<PortfolioSubOutfit> subOutfits = subOutfitDetails.entrySet().stream()
                .map(subOutfit -> new PortfolioSubOutfit(subOutfit.getKey(), subOutfit.getValue()))
                .collect(Collectors.toList());
        String successMessage = "Details fetched successfully";
        SubOutfitTypeDetailResponse response = new SubOutfitTypeDetailResponse(successMessage, subOutfits);
        response = portfolioSuboutfitTranslator.translate(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<OutfitType> getOutfitTypes(String genderString) {
        Gender gender = Gender.getGenderEnumMap().get(genderString);
        List<OutfitType> outfitTypes = Arrays.asList(OutfitType.values());
        if (Gender.MALE.equals(gender)) {
            outfitTypes = outfitTypes.stream()
                    .filter(outfitType -> outfitType.getGenderList().contains(Gender.MALE))
                    .collect(Collectors.toList());
        } else if (Gender.FEMALE.equals(gender)) {
            outfitTypes = outfitTypes.stream()
                    .filter(outfitType -> outfitType.getGenderList().contains(Gender.FEMALE))
                    .collect(Collectors.toList());
        }
        return outfitTypes;
    }

    public Map<Integer, String> getSubOutfitMap(OutfitType outfitType) throws Exception {
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        Map<Integer, String> subOutfitMap = outfitTypeService.getSubOutfitMap();
        return subOutfitMap;
    }

    public String getSubOutfitName(OutfitType outfitType, Integer subOutfitIdx) throws Exception {
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        String subOutfitString = outfitTypeService.getSubOutfitMap().get(subOutfitIdx);
        return subOutfitString;
    }
}
