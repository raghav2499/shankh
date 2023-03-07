package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.entity.Measurement;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.MeasurementRepo;
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

    public ResponseEntity getMeasurementDetails(Long customerId, String outfitTypeString) throws Exception {
        OutfitType outfitType = OutfitType.getOutfitEnumMap().get(outfitTypeString);
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        Optional<Measurement> optionalMeasurement = Optional.ofNullable(measurementRepo.findByCustomerId(customerId));
        if(optionalMeasurement.isPresent()) {
            MeasurementDAO measurementDAO = mapper.measurementObjectToDAO(optionalMeasurement.get(),
                    new CycleAvoidingMappingContext());
            return new ResponseEntity(outfitTypeService.getMeasurementResponse(measurementDAO), HttpStatus.OK);
        }
         return new ResponseEntity(outfitTypeService.getMeasurementResponse(null), HttpStatus.OK);
    }
}
