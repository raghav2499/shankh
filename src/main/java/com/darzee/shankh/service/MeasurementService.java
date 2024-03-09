package com.darzee.shankh.service;

import com.amazonaws.util.StringUtils;
import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.dao.MeasurementRevisionsDAO;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.MeasurementRevisions;
import com.darzee.shankh.entity.OrderItem;
import com.darzee.shankh.enums.FileEntityType;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private FileReferenceRepo fileReferenceRepo;

    @Autowired
    private MeasurementRevisionService measurementRevisionService;

    @Autowired
    private MeasurementRevisionsRepo measurementRevisionsRepo;

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private AmazonClient s3Client;

    public ResponseEntity getMeasurementDetailsResponse(Long customerId, Long orderItemId,
                                                        Integer outfitTypeIndex,
                                                        String scale,
                                                        Boolean nonEmptyValuesOnly) throws Exception {
        OverallMeasurementDetails measurementDetails = getMeasurementDetails(customerId, orderItemId,
                outfitTypeIndex, scale, nonEmptyValuesOnly);
        return new ResponseEntity(measurementDetails, HttpStatus.OK);
    }

    public OverallMeasurementDetails getMeasurementDetails(Long customerId, Long orderItemId,
                                                           Integer outfitTypeIndex,
                                                           String scale,
                                                           Boolean nonEmptyValuesOnly) throws Exception {
        validateGetMeasurementRequestParams(customerId, orderItemId, outfitTypeIndex, scale);

        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitTypeIndex);
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        MeasurementRevisions measurementRevision = null;
        if (orderItemId != null) {
            Optional<OrderItem> orderItem = orderItemRepo.findById(orderItemId);
            if (orderItem.isPresent()) {
                measurementRevision = orderItem.get().getMeasurementRevision();
            }
        } else if (measurementRevision == null) {
            Pageable pageable = PageRequest.of(0, 1);
            measurementRevision = measurementRevisionsRepo.findLatestByCustomerIdAndOutfitType(customerId,
                    outfitType, pageable);
        }
        MeasurementRevisionsDAO revisionsDAO = new MeasurementRevisionsDAO();
        if (measurementRevision != null) {
            revisionsDAO = mapper.measurementRevisionsToMeasurementRevisionDAO(measurementRevision);
        }
        OverallMeasurementDetails overallMeasurementDetails = null;
        MeasurementScale measurementScale = MeasurementScale.getEnumMap().get(scale);
        overallMeasurementDetails = outfitTypeService.setMeasurementDetails(revisionsDAO,
                measurementScale,
                nonEmptyValuesOnly);
        overallMeasurementDetails.setMessage(getMeasurementDetailsMessage(revisionsDAO));
        overallMeasurementDetails.setMeasurementUpdatedAt(revisionsDAO.getCreatedAt());
        return overallMeasurementDetails;
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
            revision = outfitTypeService.addMeasurementRevision(measurementRequest, customerDAO.getId(),
                    outfitType, measurementDetails.getScale());
            revision = mapper.measurementRevisionsToMeasurementRevisionDAO(
                    measurementRevisionsRepo.save(mapper.measurementRevisionsDAOToMeasurementRevision(revision)));
            measurementsDAO.setMeasurementRevision(revision);
            measurementsDAO.setCustomer(customerDAO);
            measurementsDAO.setOutfitType(outfitType);
            measurementsDAO = mapper.measurementsToMeasurementDAO(
                    measurementsRepo.save(mapper.measurementsDAOToMeasurement(measurementsDAO,
                            new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
            if (!StringUtils.isNullOrEmpty(measurementDetails.getReferenceId())) {
                objectFilesService.saveObjectFiles(Arrays.asList(measurementDetails.getReferenceId()),
                        FileEntityType.MEASUREMENT_REVISION.getEntityType(), revision.getId());
            }
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
            MeasurementRevisionData revisionData = null;
            if (!revision.getMeasurementValue().isEmpty()) {
                revisionData = new MeasurementRevisionData(revision);
            } else {
                String referenceId = objectFilesService.getMeasurementRevisionReferenceId(revision.getId());
                String measurementRevImageUrl = measurementRevisionService.getMeasurementRevisionImageLink(referenceId);
                MeasurementRevisionImageDetail measurementRevisionImageDetail = new MeasurementRevisionImageDetail(referenceId, measurementRevImageUrl);
                revisionData = new MeasurementRevisionData(revision, referenceId, measurementRevImageUrl);
            }
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

    private String getMeasurementDetailsMessage(MeasurementRevisionsDAO revisionsDAO) {
        String message = (revisionsDAO != null)
                ? "Measurement details fetched sucessfully"
                : "Measurement details not found";
        return message;
    }

    private void validateGetMeasurementRequestParams(Long customerId, Long orderItemId,
                                                     Integer outfitTypeIndex, String scale) {
        if (orderItemId == null && (customerId == null || outfitTypeIndex == null)) {
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
        CreateMeasurementResponse response = new CreateMeasurementResponse(successMessage,
                customerId, measurementDAO.getId(), measurementDAO.getMeasurementRevision().getId());
        return response;
    }


}
