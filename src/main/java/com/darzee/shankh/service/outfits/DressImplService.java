package com.darzee.shankh.service.outfits;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementRevisionsDAO;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.*;
import com.darzee.shankh.service.MeasurementService;
import com.darzee.shankh.service.OutfitImageLinkService;
import com.darzee.shankh.service.OutfitTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.darzee.shankh.constants.ImageLinks.*;
import static com.darzee.shankh.constants.MeasurementKeys.*;
import static com.darzee.shankh.constants.MeasurementTitles.*;

@Service
public class DressImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Override
    public MeasurementRevisionsDAO addMeasurementRevision(MeasurementRequest measurementDetails, Long customerId,
                                                          OutfitType outfitType, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;

        Map<String, Double> measurementValue = new HashMap<>();
        if (measurementDetails == null) {
            MeasurementRevisionsDAO revisions = new MeasurementRevisionsDAO(customerId, outfitType, measurementValue);
            return revisions;
        }

        if (measurementDetails.getLength() != null) {
            measurementValue.put(LENGTH_MEASUREMENT_KEY, measurementDetails.getLength() * multiplyingFactor);
        }
        if (measurementDetails.getShoulder() != null) {
            measurementValue.put(SHOULDER_MEASUREMENT_KEY, measurementDetails.getShoulder() * multiplyingFactor);
        }
        if (measurementDetails.getUpperChest() != null) {
            measurementValue.put(UPPER_CHEST_MEASUREMENT_KEY, measurementDetails.getUpperChest() * multiplyingFactor);
        }
        if (measurementDetails.getBust() != null) {
            measurementValue.put(BUST_MEASUREMENT_KEY, measurementDetails.getBust() * multiplyingFactor);
        }
        if (measurementDetails.getWaist() != null) {
            measurementValue.put(WAIST_MEASUREMENT_KEY, measurementDetails.getWaist() * multiplyingFactor);
        }
        if (measurementDetails.getSeat() != null) {
            measurementValue.put(SEAT_MEASUREMENT_KEY, measurementDetails.getSeat() * multiplyingFactor);
        }
        if (measurementDetails.getArmHole() != null) {
            measurementValue.put(ARM_HOLE_MEASUREMENT_KEY, measurementDetails.getArmHole() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveLength() != null) {
            measurementValue.put(SLEEVE_LENGTH_MEASUREMENT_KEY, measurementDetails.getSleeveLength() * multiplyingFactor);
        }
        if (measurementDetails.getBicep() != null) {
            measurementValue.put(BICEP_MEASUREMENT_KEY, measurementDetails.getBicep() * multiplyingFactor);
        }
        if (measurementDetails.getElbowRound() != null) {
            measurementValue.put(ELBOW_ROUND_MEASUREMENT_KEY, measurementDetails.getElbowRound() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveCircumference() != null) {
            measurementValue.put(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY,
                    measurementDetails.getSleeveCircumference() * multiplyingFactor);
        }
        if (measurementDetails.getFrontNeckDepth() != null) {
            measurementValue.put(FRONT_NECK_DEPTH_MEASUREMENT_KEY,
                    measurementDetails.getFrontNeckDepth() * multiplyingFactor);
        }
        if (measurementDetails.getBackNeckDepth() != null) {
            measurementValue.put(BACK_NECK_DEPTH_MEASUREMENT_KEY,
                    measurementDetails.getBackNeckDepth() * multiplyingFactor);
        }
        if (measurementDetails.getCrossFront() != null) {
            measurementValue.put(CROSS_FRONT_MEASUREMENT_KEY,
                    measurementDetails.getCrossFront() * multiplyingFactor);
        }
        if (measurementDetails.getCrossBack() != null) {
            measurementValue.put(CROSS_BACK_MEASUREMENT_KEY,
                    measurementDetails.getCrossBack() * multiplyingFactor);
        }
        if (measurementDetails.getDartPoint() != null) {
            measurementValue.put(DART_POINT_MEASUREMENT_KEY,
                    measurementDetails.getDartPoint() * multiplyingFactor);
        }
        MeasurementRevisionsDAO revisions = new MeasurementRevisionsDAO(customerId, outfitType, measurementValue);
        return revisions;
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue =
                (measurementsDAO != null && measurementsDAO.getMeasurementRevision() != null && measurementsDAO.getMeasurementRevision().getMeasurementValue() != null)
                        ? objectMapper.convertValue(measurementsDAO.getMeasurementRevision().getMeasurementValue(),
                        Map.class)
                        : new HashMap<>();
        outfitMeasurementDetails.setLength(measurementValue.get(LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setShoulder(measurementValue.get(SHOULDER_MEASUREMENT_KEY));
        outfitMeasurementDetails.setUpperChest(measurementValue.get(UPPER_CHEST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBust(measurementValue.get(BUST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setWaist(measurementValue.get(WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSeat(measurementValue.get(SEAT_MEASUREMENT_KEY));
        outfitMeasurementDetails.setArmHole(measurementValue.get(ARM_HOLE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveLength(measurementValue.get(SLEEVE_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBicep(measurementValue.get(BICEP_MEASUREMENT_KEY));
        outfitMeasurementDetails.setElbowRound(measurementValue.get(ELBOW_ROUND_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveCircumference(measurementValue.get(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setFrontNeckDepth(measurementValue.get(FRONT_NECK_DEPTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBackNeckDepth(measurementValue.get(BACK_NECK_DEPTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCrossFront(measurementValue.get(CROSS_FRONT_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCrossBack(measurementValue.get(CROSS_BACK_MEASUREMENT_KEY));
        outfitMeasurementDetails.setDartPoint(measurementValue.get(DART_POINT_MEASUREMENT_KEY));
        return outfitMeasurementDetails;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementRevisionsDAO revisionsDAO, MeasurementScale scale, Boolean nonEmptyValuesOnly) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        String measurementRevisionImgLink = null;

        if (CollectionUtils.isEmpty(revisionsDAO.getMeasurementValue())) {
            measurementRevisionImgLink = measurementService.getMeasurementRevisionImageLink(revisionsDAO.getId());
        } else {
            measurementDetailsResponseList.add(
                    addLength(revisionsDAO.getMeasurement(LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addShoulder(revisionsDAO.getMeasurement(SHOULDER_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addUpperChest(revisionsDAO.getMeasurement(UPPER_CHEST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBust(revisionsDAO.getMeasurement(BUST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addWaist(revisionsDAO.getMeasurement(WAIST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSeat(revisionsDAO.getMeasurement(SEAT_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(addArmHole(
                    revisionsDAO.getMeasurement(ARM_HOLE_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSleeveLength(revisionsDAO.getMeasurement(SLEEVE_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBicep(revisionsDAO.getMeasurement(BICEP_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addElbowRound(revisionsDAO.getMeasurement(ELBOW_ROUND_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSleeveCircum(revisionsDAO.getMeasurement(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addFrontNeckDepth(revisionsDAO.getMeasurement(FRONT_NECK_DEPTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBackNeckDepth(revisionsDAO.getMeasurement(BACK_NECK_DEPTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addCrossFront(revisionsDAO.getMeasurement(CROSS_FRONT_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addCrossBack(revisionsDAO.getMeasurement(CROSS_BACK_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addDartPoint(revisionsDAO.getMeasurement(DART_POINT_MEASUREMENT_KEY, dividingFactor)));

            if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
                measurementDetailsResponseList = measurementDetailsResponseList
                        .stream()
                        .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                        .collect(Collectors.toList());
            }
        }

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(DRESS_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(DRESS_OUTFIT_TYPE_HEADING);
        overallMeasurementDetails.setInnerMeasurementDetails(Arrays.asList(innerMeasurementDetails));
        overallMeasurementDetails.setMeasurementImageLink(measurementRevisionImgLink);
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.DRESS;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 1, isPortfolioEligible());
    }

    @Override
    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(1, "Short Dress");
        subOutfitMap.put(2, "Long Dress");
        subOutfitMap.put(3, "Maxi Dress");
        return subOutfitMap;
    }

    public String getSubOutfitName(Integer ordinal) {
        return getSubOutfitMap().get(ordinal);
    }

    private MeasurementDetails addLength(String value) {
        String imageLink = DRESS_LENGTH_IMAGE_LINK;
        String title = DRESS_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = DRESS_SHOULDER_IMAGE_LINK;
        String title = DRESS_SHOULDER_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addUpperChest(String value) {
        String imageLink = DRESS_UPPER_CHEST_IMAGE_LINK;
        String title = DRESS_UPPER_CHEST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBust(String value) {
        String imageLink = DRESS_BUST_IMAGE_LINK;
        String title = DRESS_BUST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = DRESS_WAIST_IMAGE_LINK;
        String title = DRESS_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeat(String value) {
        String imageLink = DRESS_SEAT_IMAGE_LINK;
        String title = DRESS_SEAT_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }


    private MeasurementDetails addArmHole(String value) {
        String imageLink = DRESS_ARMHOLE_IMAGE_LINK;
        String title = DRESS_ARMHOLE_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = DRESS_SLEEVE_LENGTH_IMAGE_LINK;
        String title = DRESS_SLEEVE_LENGTH_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBicep(String value) {
        String imageLink = DRESS_BICEP_IMAGE_LINK;
        String title = DRESS_BICEP_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }


    private MeasurementDetails addElbowRound(String value) {
        String imageLink = DRESS_ELBOW_ROUND_IMAGE_LINK;
        String title = DRESS_ELBOW_ROUND_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircum(String value) {
        String imageLink = DRESS_SLEEVE_CIRCUMFERENCE_IMAGE_LINK;
        String title = DRESS_SLEEVE_CIRCUM_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFrontNeckDepth(String value) {
        String imageLink = DRESS_FRONT_NECK_DEPTH_IMAGE_LINK;
        String title = DRESS_FRONT_NECK_DEPTH_TITLE;
        String index = "12";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBackNeckDepth(String value) {
        String imageLink = DRESS_BACK_NECK_DEPTH_IMAGE_LINK;
        String title = DRESS_BACK_NECK_DEPTH_TITLE;
        String index = "13";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCrossFront(String value) {
        String imageLink = DRESS_CROSS_FRONT_IMAGE_LINK;
        String title = DRESS_CROSS_FRONT_TITLE;
        String index = "14";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCrossBack(String value) {
        String imageLink = DRESS_CROSS_BACK_IMAGE_LINK;
        String title = DRESS_CROSS_BACK_TITLE;
        String index = "15";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addDartPoint(String value) {
        String imageLink = DRESS_DART_POINT_IMAGE_LINK;
        String title = DRESS_DART_POINT_TITLE;
        String index = "16";
        return new MeasurementDetails(imageLink, title, value, index);
    }


    @Override
    public boolean isPortfolioEligible() {
        return true;
    }
}
