package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.Measurement;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.MeasurementView;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.CustomerRepo;
import com.darzee.shankh.repo.MeasurementRepo;
import com.darzee.shankh.response.OverallMeasurementDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class MeasurementService {
    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    @Autowired
    private MeasurementRepo measurementRepo;
    @Autowired
    private CustomerRepo customerRepo;

    public ResponseEntity getMeasurementDetails(Long customerId, String outfitTypeString, String scale, String view)
            throws Exception {
        validateGetMeasurementRequestParams(outfitTypeString, scale, view);

        OutfitType outfitType = OutfitType.getOutfitEnumMap().get(outfitTypeString);
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        Optional<Customer> customer = customerRepo.findById(customerId);
        if (customer.isPresent()) {
            Optional<Measurement> optionalMeasurement = Optional.ofNullable(measurementRepo.findByCustomerId(customerId));
            MeasurementDAO measurementDAO = mapper.measurementObjectToDAO(optionalMeasurement.get(),
                    new CycleAvoidingMappingContext());
            MeasurementScale measurementScale = MeasurementScale.getEnumMap().get(scale);
            OverallMeasurementDetails overallMeasurementDetails = outfitTypeService.setMeasurementDetails(measurementDAO, measurementScale, view);
            overallMeasurementDetails.setMessage("Measurement details fetched sucessfully");
            return new ResponseEntity(overallMeasurementDetails, HttpStatus.OK);
        }

        OverallMeasurementDetails response = new OverallMeasurementDetails("customer_id is invalid");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    private void validateGetMeasurementRequestParams(String outfitTypeString, String scale, String view) {
        if(!OutfitType.getOutfitEnumMap().containsKey(outfitTypeString)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Outfit Type not supported");
        } else if (scale != null && !MeasurementScale.getEnumMap().containsKey(scale)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Measurement Scale");
        } else if (view != null && !MeasurementView.getEnumMap().containsKey(view)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Measurement View");
        }
    }
}
