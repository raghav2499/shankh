package com.darzee.shankh.controller;

import com.darzee.shankh.response.SubOutfitTypeDetailResponse;
import com.darzee.shankh.service.OutfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/outfit")
public class OutfitController {

    @Autowired
    private OutfitService outfitService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOutfitDetails(@RequestParam(value = "gender", required = false) String gender) throws Exception {
        return outfitService.getOutfitDetails(gender);
    }

    @GetMapping(value = "/sub_outfit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubOutfitTypeDetailResponse> getSubOutfits(@RequestParam("outfit_type") Integer outfitType) throws Exception {
        return outfitService.getSubOutfits(outfitType);
    }
}
