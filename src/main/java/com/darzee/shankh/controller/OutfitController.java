package com.darzee.shankh.controller;

import com.darzee.shankh.response.GetStitchOptionsResponse;
import com.darzee.shankh.response.SubOutfitTypeDetailResponse;
import com.darzee.shankh.service.OutfitService;
import com.darzee.shankh.service.StitchOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/outfit")
public class OutfitController {

    @Autowired
    private OutfitService outfitService;

    @Autowired
    private StitchOptionService stitchOptionsService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getOutfitDetails(@RequestParam(value = "gender", required = false) String gender) throws Exception {
        return outfitService.getOutfitDetails(gender);
    }

    @GetMapping(value = "/sub_outfit", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<SubOutfitTypeDetailResponse> getSubOutfits(@RequestParam("outfit_type") Integer outfitType) throws Exception {
        return outfitService.getSubOutfits(outfitType);
    }

    @GetMapping(value = "/{outfit_type}/stitch_options", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<GetStitchOptionsResponse> getStitchOptions(@PathVariable("outfit_type") Integer outfitType) {
        GetStitchOptionsResponse response = stitchOptionsService.getStitchOptions(outfitType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
