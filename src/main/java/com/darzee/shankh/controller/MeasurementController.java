package com.darzee.shankh.controller;

import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/measurement")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMeasurementDetails(@RequestParam("customer_id") Long customerId,
                                                @RequestParam("outfit_type") Integer outfitTypeIndex,
                                                @RequestParam(value = "scale", defaultValue = "inch") String scale) throws Exception {
        return measurementService.getMeasurementDetails(customerId, outfitTypeIndex, scale);
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveMeasurementDetails(@RequestBody @Valid MeasurementDetails measurementDetails) throws Exception {
        return measurementService.setMeasurementDetails(measurementDetails);
    }
}
