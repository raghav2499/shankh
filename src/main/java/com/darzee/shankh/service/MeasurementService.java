package com.darzee.shankh.service;

import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.Measurement;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.CustomerRepo;
import com.darzee.shankh.repo.MeasurementRepo;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.request.Measurements;
import com.darzee.shankh.response.CreateMeasurementResponse;
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

    public ResponseEntity getMeasurementDetails(Long customerId, Integer outfitTypeIndex, String scale)
            throws Exception {
        validateGetMeasurementRequestParams(outfitTypeIndex, scale);

        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitTypeIndex);
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        Optional<Customer> customer = customerRepo.findById(customerId);
        OverallMeasurementDetails overallMeasurementDetails = null;
        if (customer.isPresent()) {
            Measurement measurement = Optional.ofNullable(measurementRepo.findByCustomerId(customerId)).orElse(new Measurement());
            MeasurementDAO measurementDAO = mapper.measurementObjectToDAO(measurement,
                    new CycleAvoidingMappingContext());
            MeasurementScale measurementScale = MeasurementScale.getEnumMap().get(scale);
            overallMeasurementDetails = outfitTypeService.setMeasurementDetails(measurementDAO, measurementScale);
            overallMeasurementDetails.setMessage(getMeasurementDetailsMessage(outfitTypeService.areMandatoryParamsSet(measurementDAO)));
            overallMeasurementDetails.setMeasurementUpdatedAt(measurementDAO.getUpdatedAt());
            return new ResponseEntity(overallMeasurementDetails, HttpStatus.OK);
        }

        overallMeasurementDetails = new OverallMeasurementDetails("customer_id is invalid");
        return new ResponseEntity(overallMeasurementDetails, HttpStatus.OK);
    }

    public ResponseEntity setMeasurementDetails(MeasurementDetails measurementDetails) throws Exception {
        Measurements measurements = measurementDetails.getMeasurements();
        Optional<Customer> optionalCustomer = customerRepo.findById(measurementDetails.getCustomerId());
        if (optionalCustomer.isPresent()) {
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
            MeasurementDAO measurementDAO = Optional.ofNullable(customerDAO.getMeasurement()).orElse(new MeasurementDAO());
            OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(measurementDetails.getOutfitType());
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
            outfitTypeService.setMeasurementDetailsInObject(measurements, measurementDAO, measurementDetails.getScale());
            measurementDAO.setCustomer(customerDAO);
            measurementDAO = mapper.measurementObjectToDAO(measurementRepo.save(mapper.measurementDAOToObject(measurementDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
            CreateMeasurementResponse response = generateCreateMeasurementResponse(measurementDAO, customerDAO.getId());

            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer id is invalid");
    }

    public MeasurementDAO createCustomerMeasurement(CustomerDAO customerDAO) {
        MeasurementDAO measurementDAO = new MeasurementDAO(customerDAO);
        measurementDAO = mapper.measurementObjectToDAO(measurementRepo.save(mapper.measurementDAOToObject(measurementDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return measurementDAO;
    }

    private String getMeasurementDetailsMessage(boolean haveMandatoryParamsSet) {
        String message = haveMandatoryParamsSet
                ? "Measurement details fetched sucessfully"
                : "Measurement details not found";
        return message;
    }

    private void validateGetMeasurementRequestParams(Integer outfitTypeIndex, String scale) {
        if (!OutfitType.getOutfitOrdinalEnumMap().containsKey(outfitTypeIndex)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Outfit Type not supported");
        } else if (scale != null && !MeasurementScale.getEnumMap().containsKey(scale)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Measurement Scale");
        }
    }

    private CreateMeasurementResponse generateCreateMeasurementResponse(MeasurementDAO measurementDAO, Long customerId) {
        String successMessage = "Measurement details saved successfully";
        CreateMeasurementResponse response = new CreateMeasurementResponse(successMessage, customerId, measurementDAO.getId());
        return response;
    }
}
