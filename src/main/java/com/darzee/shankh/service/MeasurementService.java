package com.darzee.shankh.service;

import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.Measurements;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.CustomerRepo;
import com.darzee.shankh.repo.MeasurementsRepo;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.request.MeasurementRequest;
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
    private MeasurementsRepo measurementsRepo;
    @Autowired
    private CustomerRepo customerRepo;

    public ResponseEntity getMeasurementDetails(Long customerId, Integer outfitTypeIndex, String scale)
            throws Exception {
        validateGetMeasurementRequestParams(outfitTypeIndex, scale);

        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitTypeIndex);
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        Optional<Customer> customer = customerRepo.findById(customerId);
        Optional<Measurements> measurements = measurementsRepo.findByCustomerIdAndOutfitType(customerId, outfitType);
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        if (customer.isPresent()) {
            if (measurements.isPresent()) {
                MeasurementsDAO measurementsDAO = mapper.measurementsToMeasurementDAO(measurements.get(),
                        new CycleAvoidingMappingContext());
                MeasurementScale measurementScale = MeasurementScale.getEnumMap().get(scale);
                overallMeasurementDetails = outfitTypeService.setMeasurementDetails(measurementsDAO, measurementScale);
                overallMeasurementDetails.setMessage(getMeasurementDetailsMessage(true));
                overallMeasurementDetails.setMeasurementUpdatedAt(measurementsDAO.getUpdatedAt());
                return new ResponseEntity(overallMeasurementDetails, HttpStatus.OK);
            } else {
                overallMeasurementDetails.setMessage(getMeasurementDetailsMessage(false));
                return new ResponseEntity(overallMeasurementDetails, HttpStatus.OK);
            }
        }

        overallMeasurementDetails = new OverallMeasurementDetails("customer_id is invalid");
        return new ResponseEntity(overallMeasurementDetails, HttpStatus.OK);
    }

    public ResponseEntity setMeasurementDetails(MeasurementDetails measurementDetails) throws Exception {
        MeasurementRequest measurementRequest = measurementDetails.getMeasurements();
        Optional<Customer> optionalCustomer = customerRepo.findById(measurementDetails.getCustomerId());
        if (optionalCustomer.isPresent()) {
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
            OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(measurementDetails.getOutfitType());
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
            MeasurementsDAO measurementsDAO = Optional.ofNullable(customerDAO.getOutfitMeasurement(outfitType)).orElse(new MeasurementsDAO());
            outfitTypeService.setMeasurementDetailsInObject(measurementRequest, measurementsDAO, measurementDetails.getScale());
            measurementsDAO.setCustomer(customerDAO);
            measurementsDAO.setOutfitType(outfitType);
            measurementsDAO = mapper.measurementsToMeasurementDAO(
                    measurementsRepo.save(mapper.measurementsDAOToMeasurement(measurementsDAO,
                            new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
            CreateMeasurementResponse response = generateCreateMeasurementResponse(measurementsDAO, customerDAO.getId());

            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer id is invalid");
    }

//    public MeasurementsDAO createCustomerMeasurement(CustomerDAO customerDAO) {
//        MeasurementsDAO measurementsDAO = new MeasurementsDAO(customerDAO);
//        measurementsDAO = mapper.measurementsToMeasurementDAO(
//                measurementsRepo.save(mapper.measurementsDAOToMeasurement(measurementsDAO,
//                        new CycleAvoidingMappingContext())),
//                new CycleAvoidingMappingContext());
//        return measurementsDAO;
//    }

    private String getMeasurementDetailsMessage(boolean mandatoryParamsSet) {
        String message = mandatoryParamsSet
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

    private CreateMeasurementResponse generateCreateMeasurementResponse(MeasurementsDAO measurementDAO, Long customerId) {
        String successMessage = "Measurement details saved successfully";
        CreateMeasurementResponse response = new CreateMeasurementResponse(successMessage, customerId, measurementDAO.getId());
        return response;
    }
}
