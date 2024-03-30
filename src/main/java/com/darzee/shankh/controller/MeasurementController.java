package com.darzee.shankh.controller;

import com.darzee.shankh.request.MeasurementDetailsRequest;
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
    @CrossOrigin
    public ResponseEntity getMeasurementDetails(@RequestParam(value = "customer_id", required = false) Long customerId,
                                                @RequestParam(value = "outfit_type", required = false) Integer outfitTypeIndex,
                                                @RequestParam(value = "order_item_id", required = false) Long orderItemId,
                                                @RequestParam(value = "scale", defaultValue = "inch") String scale,
                                                @RequestParam(value = "non_empty_values_only", required = false)
                                                Boolean nonEmptyValuesOnly)
            throws Exception {
        return measurementService.getMeasurementDetailsResponse(customerId, orderItemId, outfitTypeIndex, scale, nonEmptyValuesOnly);
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity saveMeasurementDetails(@RequestBody @Valid MeasurementDetailsRequest measurementDetails) throws Exception {
        return measurementService.saveMeasurementDetails(measurementDetails);
    }

    @GetMapping(value = "/revisions", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getMeasurementRevisions(@RequestParam("customer_id") Long customerId,
                                                  @RequestParam("outfit_type") Integer outfitTypeIndex,
                                                  @RequestParam(value = "scale", defaultValue = "inch") String scale) {
        return measurementService.getMeasurementRevisions(customerId, outfitTypeIndex, scale);
    }
}
