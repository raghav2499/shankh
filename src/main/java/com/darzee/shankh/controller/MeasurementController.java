package com.darzee.shankh.controller;

import com.darzee.shankh.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/measurement")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMeasurementDetails(@RequestParam("customer_id") Long customerId,
                                                @RequestParam("outfit_type") String outfitType,
                                                @RequestParam(value = "scale", defaultValue = "cm") String scale,
                                                @RequestParam(value = "view", required = false) String view) throws Exception {
        return measurementService.getMeasurementDetails(customerId, outfitType, scale, view);
    }
}
