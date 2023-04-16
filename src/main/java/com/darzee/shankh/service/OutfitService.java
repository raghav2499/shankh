package com.darzee.shankh.service;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.OutfitDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutfitService {

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    public ResponseEntity getOutfitDetails() throws Exception {
        OutfitType[] outfitTypes = OutfitType.values();
        List<OutfitDetails> outfitDetailsList = new ArrayList<>(outfitTypes.length);
        for(OutfitType outfitType : outfitTypes) {
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
            outfitDetailsList.add(outfitTypeService.getOutfitDetails());
        }
        return new ResponseEntity(outfitDetailsList, HttpStatus.OK);
    }
}
