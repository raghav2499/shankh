package com.darzee.shankh.service;

import com.amazonaws.util.StringUtils;
import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.dao.MeasurementRevisionsDAO;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.MeasurementRevisions;
import com.darzee.shankh.entity.Measurements;
import com.darzee.shankh.enums.FileEntityType;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.CustomerRepo;
import com.darzee.shankh.repo.MeasurementRevisionsRepo;
import com.darzee.shankh.repo.MeasurementsRepo;
import com.darzee.shankh.repo.OrderItemRepo;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.CreateMeasurementResponse;
import com.darzee.shankh.response.GetMeasurementRevisionsResponse;
import com.darzee.shankh.response.MeasurementRevisionData;
import com.darzee.shankh.response.OverallMeasurementDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

    @Autowired
    private BucketService bucketService;

    @Autowired
    private ObjectFilesService objectFilesService;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private CustomerMeasurementService customerMeasurementService;

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    @Autowired
    private MeasurementsRepo measurementsRepo;

    @Autowired
    private MeasurementRevisionsRepo measurementRevisionsRepo;

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;

    public ResponseEntity getMeasurementDetails(Long customerId, Long orderItemId,
                                                Integer outfitTypeIndex,
                                                String scale,
                                                Boolean nonEmptyValuesOnly) throws Exception {
        validateGetMeasurementRequestParams(customerId, orderItemId, outfitTypeIndex, scale);

        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitTypeIndex);
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        Optional<Customer> customer = customerRepo.findById(customerId);
        Optional<Measurements> measurements = measurementsRepo.findOneByCustomerIdAndOutfitType(customerId, outfitType);
        OverallMeasurementDetails overallMeasurementDetails = null;
        if (customer.isPresent()) {
            boolean mandatoryParamsSet = measurements.isPresent() ? true : false;
            MeasurementsDAO measurementsDAO = measurements.isPresent()
                    ? mapper.measurementsToMeasurementDAO(measurements.get(), new CycleAvoidingMappingContext())
                    : new MeasurementsDAO();
            MeasurementScale measurementScale = MeasurementScale.getEnumMap().get(scale);
            overallMeasurementDetails = outfitTypeService.setMeasurementDetails(measurementsDAO,
                    measurementScale,
                    nonEmptyValuesOnly);
            overallMeasurementDetails.setMessage(getMeasurementDetailsMessage(mandatoryParamsSet));
            overallMeasurementDetails.setMeasurementUpdatedAt(measurementsDAO.getUpdatedAt());
            return new ResponseEntity(overallMeasurementDetails, HttpStatus.OK);
        }

        overallMeasurementDetails = new OverallMeasurementDetails("customer_id is invalid");
        return new ResponseEntity(overallMeasurementDetails, HttpStatus.OK);
    }

    public ResponseEntity saveMeasurementDetails(MeasurementDetails measurementDetails) throws Exception {
        MeasurementsDAO measurementsDAO = setMeasurementDetails(measurementDetails);
        CreateMeasurementResponse response = generateCreateMeasurementResponse(measurementsDAO, measurementDetails.getCustomerId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public MeasurementsDAO setMeasurementDetails(MeasurementDetails measurementDetails) throws Exception {
        MeasurementRequest measurementRequest = measurementDetails.getMeasurements();
        Optional<Customer> optionalCustomer = customerRepo.findById(measurementDetails.getCustomerId());
        if (optionalCustomer.isPresent()) {
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
            OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(measurementDetails.getOutfitType());
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
            MeasurementsDAO measurementsDAO = customerMeasurementService.getCustomerMeasurements(customerDAO.getId(), outfitType);
            MeasurementRevisionsDAO revision = null;
            if (!StringUtils.isNullOrEmpty(measurementDetails.getReferenceId())) {
                String referenceId = measurementDetails.getReferenceId();
                revision = outfitTypeService.addMeasurementRevision(measurementRequest, customerDAO.getId(),
                        outfitType, measurementDetails.getScale());
                objectFilesService.saveObjectImages(Arrays.asList(referenceId),
                        FileEntityType.MEASUREMENT_REVISION.getEntityType(), revision.getId());
            } else {
                revision = outfitTypeService.addMeasurementRevision(measurementRequest, customerDAO.getId(),
                        outfitType, measurementDetails.getScale());
            }
            revision = mapper.measurementRevisionsToMeasurementRevisionDAO(
                    measurementRevisionsRepo.save(mapper.measurementRevisionsDAOToMeasurementRevision(revision)));
            measurementsDAO.setMeasurementRevision(revision);
            measurementsDAO.setCustomer(customerDAO);
            measurementsDAO.setOutfitType(outfitType);
            measurementsDAO = mapper.measurementsToMeasurementDAO(
                    measurementsRepo.save(mapper.measurementsDAOToMeasurement(measurementsDAO,
                            new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
            return measurementsDAO;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer id is invalid");
    }

    public ResponseEntity<GetMeasurementRevisionsResponse> getMeasurementRevisions(Long customerId, Integer outfitOrdinal) {
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitOrdinal);
        List<MeasurementRevisionsDAO> measurementRevisions = mapper.measurementRevisionsListToDAOList(
                measurementRevisionsRepo.findAllByCustomerIdAndOutfitType(customerId, outfitType));
        List<MeasurementRevisionData> data = new ArrayList<>(measurementRevisions.size());
        for (MeasurementRevisionsDAO revision : measurementRevisions) {
            MeasurementRevisionData revisionData = new MeasurementRevisionData(revision);
            data.add(revisionData);
        }
        GetMeasurementRevisionsResponse response = new GetMeasurementRevisionsResponse(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public MeasurementRevisionsDAO getMeasurementRevisionById(Long measurmentRevId) {
        MeasurementRevisionsDAO measurementRevisionsDAO = null;
        Optional<MeasurementRevisions> mRevision = measurementRevisionsRepo.findById(measurmentRevId);
        if (mRevision.isPresent()) {
            measurementRevisionsDAO = mapper.measurementRevisionsToMeasurementRevisionDAO(mRevision.get());
        }
        return measurementRevisionsDAO;
    }

    private String getMeasurementDetailsMessage(boolean mandatoryParamsSet) {
        String message = mandatoryParamsSet
                ? "Measurement details fetched sucessfully"
                : "Measurement details not found";
        return message;
    }

    private void validateGetMeasurementRequestParams(Long customerId, Long orderItemId,
                                                     Integer outfitTypeIndex, String scale) {
        if(orderItemId == null && (customerId == null || outfitTypeIndex != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either send order item id or (outfit type and customer id)");
        }
        if (outfitTypeIndex != null && !OutfitType.getOutfitOrdinalEnumMap().containsKey(outfitTypeIndex)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Outfit Type not supported");
        }
        if (scale != null && !MeasurementScale.getEnumMap().containsKey(scale)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Measurement Scale");
        }
    }

    private CreateMeasurementResponse generateCreateMeasurementResponse(MeasurementsDAO measurementDAO, Long customerId) {
        String successMessage = "Measurement details saved successfully";
        CreateMeasurementResponse response = new CreateMeasurementResponse(successMessage, customerId, measurementDAO.getId());
        return response;
    }
}
