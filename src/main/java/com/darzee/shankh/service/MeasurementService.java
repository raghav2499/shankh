package com.darzee.shankh.service;

import com.amazonaws.util.StringUtils;
import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.dao.MeasurementParamDAO;
import com.darzee.shankh.dao.MeasurementRevisionsDAO;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.entity.*;
import com.darzee.shankh.enums.FileEntityType;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitSide;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.MeasurementDetailsRequest;
import com.darzee.shankh.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static com.darzee.shankh.config.BucketConfig.getOutfitsFolder;
import static com.darzee.shankh.config.BucketConfig.getStaticBucket;

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
    private BoutiqueMeasurementRepo boutiqueMeasurementRepo;

    @Autowired
    private MeasurementParamRepo measurementParamRepo;

    @Autowired
    private AmazonClient s3Client;
    @Autowired
    private BoutiqueRepo boutiqueRepo;

    public ResponseEntity getMeasurementDetailsResponse(Long customerId, Long orderItemId,
                                                        Integer outfitTypeIndex,
                                                        String scale,
                                                        Boolean nonEmptyValuesOnly) throws Exception {
        OverallMeasurementDetails measurementDetails = getMeasurementDetails(customerId, orderItemId,
                outfitTypeIndex, scale, nonEmptyValuesOnly);
        measurementDetails.setMessage(getMeasurementDetailsMessage(measurementDetails));
        return new ResponseEntity(measurementDetails, HttpStatus.OK);
    }

    public OverallMeasurementDetails getMeasurementDetails(Long customerId, Long orderItemId,
                                                           Integer outfitTypeIndex,
                                                           String scale,
                                                           Boolean nonEmptyValuesOnly) throws Exception {
        validateGetMeasurementRequestParams(customerId, orderItemId, outfitTypeIndex, scale);
        Long boutiqueId = customerRepo.findById(customerId).get().getBoutique().getId();
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitTypeIndex);
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        MeasurementRevisions measurementRevision = null;
        if (orderItemId != null) {
            Optional<OrderItem> orderItem = orderItemRepo.findById(orderItemId);
            if (orderItem.isPresent()) {
                measurementRevision = orderItem.get().getMeasurementRevision();
            }
        }
        if (measurementRevision == null) {
            measurementRevision = measurementRevisionsRepo.findTopByCustomerIdAndOutfitTypeOrderByIdDesc(customerId,
                    outfitType);
        }
        MeasurementRevisionsDAO revisionsDAO = new MeasurementRevisionsDAO();
        if (measurementRevision != null) {
            revisionsDAO = mapper.measurementRevisionsToMeasurementRevisionDAO(measurementRevision);
        }
        OverallMeasurementDetails overallMeasurementDetails = null;
        MeasurementScale measurementScale = MeasurementScale.getEnumMap().get(scale);
        overallMeasurementDetails = setMeasurementDetails(revisionsDAO, measurementScale, boutiqueId,
                nonEmptyValuesOnly);
        return overallMeasurementDetails;
    }

    public ResponseEntity saveMeasurementDetails(MeasurementDetailsRequest measurementDetails) throws Exception {
        MeasurementsDAO measurementsDAO = setMeasurementDetails(measurementDetails);
        CreateMeasurementResponse response = generateCreateMeasurementResponse(measurementsDAO, measurementDetails.getCustomerId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public MeasurementsDAO setMeasurementDetails(MeasurementDetailsRequest measurementRequest) throws Exception {
        Optional<Customer> optionalCustomer = customerRepo.findById(measurementRequest.getCustomerId());
        if (optionalCustomer.isPresent()) {
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
            OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(measurementRequest.getOutfitType());
            MeasurementsDAO measurementsDAO = customerMeasurementService.getCustomerMeasurements(customerDAO.getId(), outfitType);
            MeasurementRevisionsDAO revision = null;
            revision = addMeasurementRevision(measurementRequest, customerDAO.getId(),
                    outfitType, measurementRequest.getScale());
            revision = mapper.measurementRevisionsToMeasurementRevisionDAO(
                    measurementRevisionsRepo.save(mapper.measurementRevisionsDAOToMeasurementRevision(revision)));
            measurementsDAO.setMeasurementRevision(revision);
            measurementsDAO.setCustomer(customerDAO);
            measurementsDAO.setOutfitType(outfitType);
            measurementsDAO = mapper.measurementsToMeasurementDAO(
                    measurementsRepo.save(mapper.measurementsDAOToMeasurement(measurementsDAO,
                            new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
            if (!StringUtils.isNullOrEmpty(measurementRequest.getReferenceId())) {
                objectFilesService.saveObjectFiles(Arrays.asList(measurementRequest.getReferenceId()),
                        FileEntityType.MEASUREMENT_REVISION.getEntityType(), revision.getId());
            }
            return measurementsDAO;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer id is invalid");
    }

    public ResponseEntity<GetMeasurementRevisionsResponse> getMeasurementRevisions(Long customerId,
                                                                                   Integer outfitOrdinal,
                                                                                   String scale) {
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitOrdinal);
        MeasurementScale measurementScale = MeasurementScale.getEnumMap().get(scale);
        Double dividingFactor = MeasurementScale.INCH.equals(measurementScale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        List<MeasurementRevisionsDAO> measurementRevisions = mapper.measurementRevisionsListToDAOList(
                measurementRevisionsRepo.findAllByCustomerIdAndOutfitTypeOrderByCreatedAtDesc(customerId, outfitType));
        List<MeasurementRevisionData> data = new ArrayList<>(measurementRevisions.size());
        for (MeasurementRevisionsDAO revision : measurementRevisions) {
            MeasurementRevisionData revisionData = null;
            if (!CollectionUtils.isEmpty(revision.getMeasurementValue())) {
                revisionData = new MeasurementRevisionData(revision, dividingFactor);
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

    private String getMeasurementDetailsMessage(OverallMeasurementDetails measurementDetails) {
        String message = (!CollectionUtils.isEmpty(measurementDetails.getInnerMeasurementDetails()))
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

    private MeasurementRevisionsDAO addMeasurementRevision(MeasurementDetailsRequest measurementRequest, Long customerId,
                                                           OutfitType outfitType, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        Map<String, Double> measurementValue = new HashMap<>();
        if (measurementRequest == null) {
            MeasurementRevisionsDAO revisions = new MeasurementRevisionsDAO(customerId, outfitType, measurementValue);
            return revisions;
        }
        CustomerDAO customer = mapper.customerObjectToDao(customerRepo.findById(customerId).get(),
                new CycleAvoidingMappingContext());
        Long boutiqueId = customer.getBoutique().getId();
        Map<OutfitSide, List<String>> boutiqueMeasurementParams = getBoutiqueMeasurementParams(boutiqueId, outfitType);
        List<String> boutiqueParams = boutiqueMeasurementParams.values().stream().flatMap(List::stream)
                .collect(Collectors.toList());
        Map<String, Double> customerMeasurementDetails = measurementRequest.getMeasurements();
        for (String boutiqueParam : boutiqueParams) {
            if (customerMeasurementDetails.containsKey(boutiqueParam)) {
                measurementValue.put(boutiqueParam, customerMeasurementDetails.get(boutiqueParam) * multiplyingFactor);
            }
        }
        MeasurementRevisionsDAO revisions = new MeasurementRevisionsDAO(customerId, outfitType, measurementValue);
        return revisions;
    }

    private OverallMeasurementDetails setMeasurementDetails(MeasurementRevisionsDAO revisionsDAO, MeasurementScale scale,
                                                            Long boutiqueId, Boolean nonEmptyValuesOnly) throws Exception {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        List<InnerMeasurementDetails> innerMeasurementDetailsList = new ArrayList<>();
        String measurementRevisionImgLink = null;

        if (CollectionUtils.isEmpty(revisionsDAO.getMeasurementValue())
                && measurementRevisionService.measurementRevisionImageExists(revisionsDAO.getId())) {
            measurementRevisionImgLink = measurementRevisionService.getMeasurementRevisionImageLink(revisionsDAO.getId());
        } else {
            Map<OutfitSide, List<String>> boutiqueMeasurementParams = getBoutiqueMeasurementParams(boutiqueId, revisionsDAO.getOutfitType());
            List<String> eligibleMeasurementParams = boutiqueMeasurementParams.values().stream().flatMap(List::stream)
                    .collect(Collectors.toList());
            List<MeasurementParam> measurementParamDetails = measurementParamRepo.findAllByNameIn(eligibleMeasurementParams);

            Map<String, MeasurementParam> paramDetailMap = measurementParamDetails.stream()
                    .collect(Collectors.toMap(MeasurementParam::getName,
                            measurementParam -> measurementParam));
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(revisionsDAO.getOutfitType());
            for (Map.Entry<OutfitSide, List<String>> outfitSideParamList : boutiqueMeasurementParams.entrySet()) {
                InnerMeasurementDetails innerMeasurementDetails = null;
                List<String> paramList = outfitSideParamList.getValue();
                List<MeasurementDetails> measurementDetails =
                        getMeasurementDetailsList(revisionsDAO.getMeasurementValue(), dividingFactor,
                                paramList, paramDetailMap, nonEmptyValuesOnly);
                if (OutfitSide.TOP.equals(outfitSideParamList.getKey())) {
                    String heading = outfitTypeService.getTopHeading();
                    String imageLink = getTopMeasurementParamImageLink();
                    innerMeasurementDetails = new InnerMeasurementDetails(heading, measurementDetails, imageLink);
                } else {
                    String heading = outfitTypeService.getBottomHeading();
                    String imageLink = getBottomMeasurementParamImageLink();
                    innerMeasurementDetails = new InnerMeasurementDetails(heading, measurementDetails, imageLink);
                }
                innerMeasurementDetailsList.add(innerMeasurementDetails);
            }
        }
        overallMeasurementDetails.setInnerMeasurementDetails(innerMeasurementDetailsList);
        overallMeasurementDetails.setMeasurementImageLink(measurementRevisionImgLink);
        overallMeasurementDetails.setMeasurementUpdatedAt(revisionsDAO.getCreatedAt());
        overallMeasurementDetails.setMeasurementRevisionId(revisionsDAO.getId());
        return overallMeasurementDetails;
    }

    private Map<OutfitSide, List<String>> getBoutiqueMeasurementParams(Long boutiqueId, OutfitType outfitType) {
        Long defaultBoutiqueId = 0L;
        Map<OutfitSide, List<String>> boutiqueMeasurement = new HashMap<>();
        if (outfitType.getPieces() == 2) {
            BoutiqueMeasurement topOutfitMeasurement =
                    boutiqueMeasurementRepo.findByBoutiqueIdAndOutfitTypeAndOutfitSide(boutiqueId, outfitType, OutfitSide.TOP);
            if (topOutfitMeasurement == null) {
                topOutfitMeasurement = boutiqueMeasurementRepo.findByBoutiqueIdAndOutfitTypeAndOutfitSide(defaultBoutiqueId,
                        outfitType, OutfitSide.TOP);
            }
            BoutiqueMeasurement bottomOutfitMeasurement =
                    boutiqueMeasurementRepo.findByBoutiqueIdAndOutfitTypeAndOutfitSide(boutiqueId, outfitType, OutfitSide.BOTTOM);
            if (bottomOutfitMeasurement == null) {
                bottomOutfitMeasurement = boutiqueMeasurementRepo.findByBoutiqueIdAndOutfitTypeAndOutfitSide(defaultBoutiqueId,
                        outfitType, OutfitSide.BOTTOM);
            }
            boutiqueMeasurement.put(OutfitSide.TOP, topOutfitMeasurement.getParam());
            boutiqueMeasurement.put(OutfitSide.BOTTOM, bottomOutfitMeasurement.getParam());
        } else {
            BoutiqueMeasurement outfitMeasurement =
                    boutiqueMeasurementRepo.findByBoutiqueIdAndOutfitType(boutiqueId, outfitType);
            if (outfitMeasurement == null) {
                outfitMeasurement = boutiqueMeasurementRepo.findByBoutiqueIdAndOutfitType(defaultBoutiqueId, outfitType);
            }
            boutiqueMeasurement.put(outfitMeasurement.getOutfitSide(), outfitMeasurement.getParam());
        }
        return boutiqueMeasurement;
    }

    private List<MeasurementDetails> getMeasurementDetailsList(Map<String, Double> measurementValue,
                                                               Double dividingFactor,
                                                               List<String> params,
                                                               Map<String, MeasurementParam> paramDetailMap,
                                                               Boolean nonEmptyValuesOnly) {
        List<MeasurementDetails> measurementDetailsList = new ArrayList<>();
        int idx = 0;
        for (String param : params) {
            String value = String.valueOf(measurementValue.get(param) / dividingFactor);
            MeasurementParam measurementParam = getMeasurementParams(paramDetailMap, param);
            String imageLink = getMeasurementParamImageLink(measurementParam.getFileName());
            MeasurementDetails measurementDetails = new MeasurementDetails(imageLink,
                    measurementParam.getDisplayName(), value, String.valueOf(idx));
            idx++;
            measurementDetailsList.add(measurementDetails);
        }
        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsList = measurementDetailsList
                    .stream()
                    .filter(measurement -> org.apache.commons.lang3.StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }
        return measurementDetailsList;
    }

    private MeasurementParam getMeasurementParams(Map<String, MeasurementParam> paramDetailMap, String param) {
        if (paramDetailMap.containsKey(param)) {
            return paramDetailMap.get(param);
        }
        MeasurementParam measurementDetails = new MeasurementParam(param, param, "");
        return measurementDetails;

    }

    private String getMeasurementParamImageLink(String fileName) {
        return "https://s3.amazonaws.com/" + getOutfitsFolder() + "/" + fileName + ".svg";
    }

    private String getTopMeasurementParamImageLink() {
        return "https://s3.amazonaws.com/" + getStaticBucket() + "/top.svg";
    }

    private String getBottomMeasurementParamImageLink() {
        return "https://s3.amazonaws.com/" + getStaticBucket() + "/bottom.svg";
    }
}