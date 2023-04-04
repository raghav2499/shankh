package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.entity.Measurement;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.MeasurementRepo;
import com.darzee.shankh.response.GetMeasurementResponse;
import com.darzee.shankh.response.MeasurementDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeasurementService {
    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    @Autowired
    private MeasurementRepo measurementRepo;

    public ResponseEntity getMeasurementDetails(Long customerId, String outfitTypeString, String scale)
            throws Exception {
        OutfitType outfitType = OutfitType.getOutfitEnumMap().get(outfitTypeString);
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        Optional<Measurement> optionalMeasurement = Optional.ofNullable(measurementRepo.findByCustomerId(customerId));
        if (optionalMeasurement.isPresent()) {
            MeasurementDAO measurementDAO = mapper.measurementObjectToDAO(optionalMeasurement.get(),
                    new CycleAvoidingMappingContext());
            if (outfitTypeService.haveAllRequiredMeasurements(measurementDAO)) {
                MeasurementScale measurementScale = MeasurementScale.getEnumMap().get(scale);
                MeasurementDetails measurementDetails = outfitTypeService.getMeasurementResponse(measurementDAO, measurementScale);
                GetMeasurementResponse response = new
                        GetMeasurementResponse("Measurement details fetched successfully", measurementDetails);
                return new ResponseEntity(response, HttpStatus.OK);
            }
        }
        GetMeasurementResponse response = new
                GetMeasurementResponse("Measurement details not found", null);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
