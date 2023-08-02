package com.darzee.shankh.service;

import com.darzee.shankh.enums.Gender;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutfitService {

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    public ResponseEntity getOutfitDetails(String gender) throws Exception {
        List<OutfitType> outfitTypes = getOutfitTypes(gender);
        List<OutfitDetails> outfitDetailsList = new ArrayList<>(outfitTypes.size());
        for (OutfitType outfitType : outfitTypes) {
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
            outfitDetailsList.add(outfitTypeService.getOutfitDetails());
        }
        return new ResponseEntity(outfitDetailsList, HttpStatus.OK);
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
}
