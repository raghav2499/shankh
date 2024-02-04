package com.darzee.shankh.service.outfits;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementRevisionsDAO;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.*;
import com.darzee.shankh.service.OutfitImageLinkService;
import com.darzee.shankh.service.OutfitTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.darzee.shankh.constants.ImageLinks.*;
import static com.darzee.shankh.constants.MeasurementKeys.*;
import static com.darzee.shankh.constants.MeasurementTitles.*;

@Service
public class SherwaniImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Override
    public MeasurementRevisionsDAO addMeasurementRevision(MeasurementRequest measurementDetails, Long customerId, OutfitType outfitType, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
                Map<String, Double> measurementValue = new HashMap<>();
        if (measurementDetails == null) {
            MeasurementRevisionsDAO revisions = new MeasurementRevisionsDAO(customerId, outfitType, measurementValue);
            return revisions;
        }

        if (measurementDetails.getShirtLength() != null) {
            measurementValue.put(SHIRT_LENGTH_MEASUREMENT_KEY, measurementDetails.getShirtLength() * multiplyingFactor);
        }
        if (measurementDetails.getNeck() != null) {
            measurementValue.put(NECK_MEASUREMENT_KEY, measurementDetails.getNeck() * multiplyingFactor);
        }
        if (measurementDetails.getShoulder() != null) {
            measurementValue.put(SHOULDER_MEASUREMENT_KEY, measurementDetails.getShoulder() * multiplyingFactor);
        }
        if (measurementDetails.getChest() != null) {
            measurementValue.put(CHEST_MEASUREMENT_KEY, measurementDetails.getChest() * multiplyingFactor);
        }
        if (measurementDetails.getWaist() != null) {
            measurementValue.put(WAIST_MEASUREMENT_KEY, measurementDetails.getWaist() * multiplyingFactor);
        }
        if (measurementDetails.getHipCircum() != null) {
            measurementValue.put(HIP_CIRCUM_MEASUREMENT_KEY, measurementDetails.getHipCircum() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveLength() != null) {
            measurementValue.put(SLEEVE_LENGTH_MEASUREMENT_KEY, measurementDetails.getSleeveLength() * multiplyingFactor);
        }
        if (measurementDetails.getBicep() != null) {
            measurementValue.put(BICEP_MEASUREMENT_KEY,
                    measurementDetails.getBicep() * multiplyingFactor);
        }
        if (measurementDetails.getElbowRound() != null) {
            measurementValue.put(ELBOW_ROUND_MEASUREMENT_KEY,
                    measurementDetails.getElbowRound() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveCircumference() != null) {
            measurementValue.put(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY,
                    measurementDetails.getSleeveCircumference() * multiplyingFactor);
        }
        if (measurementDetails.getArmHole() != null) {
            measurementValue.put(ARM_HOLE_MEASUREMENT_KEY,
                    measurementDetails.getArmHole() * multiplyingFactor);
        }
        if (measurementDetails.getBottomWaist() != null) {
            measurementValue.put(BOTTOM_WAIST_MEASUREMENT_KEY, measurementDetails.getBottomWaist() * multiplyingFactor);
        }
        if (measurementDetails.getBottomHipCircum() != null) {
            measurementValue.put(BOTTOM_HIP_CIRCUM_MEASUREMENT_KEY, measurementDetails.getBottomHipCircum() * multiplyingFactor);
        }
        if (measurementDetails.getThigh() != null) {
            measurementValue.put(THIGH_MEASUREMENT_KEY, measurementDetails.getThigh() * multiplyingFactor);
        }
        if (measurementDetails.getCalf() != null) {
            measurementValue.put(CALF_MEASUREMENT_KEY, measurementDetails.getCalf() * multiplyingFactor);
        }
        if (measurementDetails.getBottom() != null) {
            measurementValue.put(BOTTOM_MEASUREMENT_KEY, measurementDetails.getBottom() * multiplyingFactor);
        }
        if (measurementDetails.getBottomLength() != null) {
            measurementValue.put(BOTTOM_LENGTH_MEASUREMENT_KEY, measurementDetails.getBottomLength() * multiplyingFactor);
        }
        if (measurementDetails.getFly() != null) {
            measurementValue.put(FLY_MEASUREMENT_KEY, measurementDetails.getFly() * multiplyingFactor);
        }
        if (measurementDetails.getInSeam() != null) {
            measurementValue.put(IN_SEAM_MEASUREMENT_KEY, measurementDetails.getInSeam() * multiplyingFactor);
        }
        if (measurementDetails.getCrotch() != null) {
            measurementValue.put(CROTCH_MEASUREMENT_KEY, measurementDetails.getCrotch() * multiplyingFactor);
        }
        MeasurementRevisionsDAO revisions = new MeasurementRevisionsDAO(customerId, outfitType, measurementValue);
        return revisions;
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue = (measurementsDAO != null && measurementsDAO.getMeasurementRevision() != null && measurementsDAO.getMeasurementRevision().getMeasurementValue() != null)
                ? objectMapper.convertValue(measurementsDAO.getMeasurementRevision().getMeasurementValue(), Map.class)
                : new HashMap<>();

        outfitMeasurementDetails.setShirtLength(measurementValue.get(SHIRT_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setNeck(measurementValue.get(NECK_MEASUREMENT_KEY));
        outfitMeasurementDetails.setShoulder(measurementValue.get(SHOULDER_MEASUREMENT_KEY));
        outfitMeasurementDetails.setChest(measurementValue.get(CHEST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setWaist(measurementValue.get(WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setHipCircum(measurementValue.get(HIP_CIRCUM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveLength(measurementValue.get(SLEEVE_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBicep(measurementValue.get(BICEP_MEASUREMENT_KEY));
        outfitMeasurementDetails.setElbowRound(measurementValue.get(ELBOW_ROUND_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveCircumference(measurementValue.get(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setArmHole(measurementValue.get(ARM_HOLE_MEASUREMENT_KEY));

        outfitMeasurementDetails.setBottomWaist(measurementValue.get(BOTTOM_WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottomHipCircum(measurementValue.get(BOTTOM_HIP_CIRCUM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setThigh(measurementValue.get(THIGH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCalf(measurementValue.get(CALF_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottom(measurementValue.get(BOTTOM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottomLength(measurementValue.get(BOTTOM_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setFly(measurementValue.get(FLY_MEASUREMENT_KEY));
        outfitMeasurementDetails.setInSeam(measurementValue.get(IN_SEAM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCrotch(measurementValue.get(CROTCH_MEASUREMENT_KEY));

        return outfitMeasurementDetails;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementRevisionsDAO revisionsDAO, MeasurementScale scale,
                                                           Boolean nonEmptyValuesOnly) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<InnerMeasurementDetails> innerMeasurementDetails = new ArrayList<>();
        innerMeasurementDetails.add(setMeasurementDetailsInObjectTop(revisionsDAO, scale, nonEmptyValuesOnly));
        innerMeasurementDetails.add(setMeasurementDetailsInObjectBottom(revisionsDAO, scale, nonEmptyValuesOnly));
        overallMeasurementDetails.setInnerMeasurementDetails(innerMeasurementDetails);
        return overallMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectTop(MeasurementRevisionsDAO revisionsDAO,
                                                                     MeasurementScale scale,
                                                                     Boolean nonEmptyValuesOnly) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;

        measurementDetailsResponseList.add(
                addLengthUpper(revisionsDAO.getMeasurement(SHIRT_LENGTH_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addNeck(revisionsDAO.getMeasurement(NECK_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addShoulder(revisionsDAO.getMeasurement(SHOULDER_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addChest(revisionsDAO.getMeasurement(CHEST_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addWaistUpper(revisionsDAO.getMeasurement(WAIST_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addHipCircumference(revisionsDAO.getMeasurement(HIP_CIRCUM_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addSleeveLength(revisionsDAO.getMeasurement(SLEEVE_LENGTH_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addBicep(revisionsDAO.getMeasurement(BICEP_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addElbowRound(revisionsDAO.getMeasurement(ELBOW_ROUND_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addSleeveCircumference(revisionsDAO.getMeasurement(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addArmHole(revisionsDAO.getMeasurement(ARM_HOLE_MEASUREMENT_KEY, dividingFactor)));

        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }
        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(SHERWANI_UPPER_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(SHERWANI_TOP_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.SHERWANI;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 2, isPortfolioEligible());
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectBottom(MeasurementRevisionsDAO revisionsDAO,
                                                                        MeasurementScale scale,
                                                                        Boolean nonEmptyValuesOnly) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;

        measurementDetailsResponseList.add(
                addWaistLower(revisionsDAO.getMeasurement(BOTTOM_WAIST_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addHipCircumLower(revisionsDAO.getMeasurement(BOTTOM_HIP_CIRCUM_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addThighCircum(revisionsDAO.getMeasurement(THIGH_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addCalf(revisionsDAO.getMeasurement(CALF_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addBottom(revisionsDAO.getMeasurement(BOTTOM_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addLengthLower(revisionsDAO.getMeasurement(BOTTOM_LENGTH_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addFly(revisionsDAO.getMeasurement(FLY_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addInseam(revisionsDAO.getMeasurement(IN_SEAM_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addCrotch(revisionsDAO.getMeasurement(CROTCH_MEASUREMENT_KEY, dividingFactor)));

        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(SHERWANI_LOWER_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(SHERWANI_BOTTOM_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private MeasurementDetails addLengthUpper(String value) {
        String imageLink = SHERWANI_UPPER_LENGTH_IMAGE_LINK;
        String title = SHERWANI_UPPER_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addNeck(String value) {
        String imageLink = SHERWANI_NECK_IMAGE_LINK;
        String title = SHERWANI_NECK_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = SHERWANI_SHOULDER_IMAGE_LINK;
        String title = SHERWANI_SHOULDER_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addChest(String value) {
        String imageLink = SHERWANI_CHEST_IMAGE_LINK;
        String title = SHERWANI_CHEST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaistUpper(String value) {
        String imageLink = SHERWANI_UPPER_WAIST_IMAGE_LINK;
        String title = SHERWANI_UPPER_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addHipCircumference(String value) {
        String imageLink = SHERWANI_UPPER_HIP_CIRCUM_IMAGE_LINK;
        String title = SHERWANI_UPPER_HIP_CIRCUM_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = SHERWANI_SLEEVE_IMAGE_LINK;
        String title = SHERWANI_SLEEVE_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBicep(String value) {
        String imageLink = SHERWANI_BICEP_IMAGE_LINK;
        String title = SHERWANI_BICEP_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addElbowRound(String value) {
        String imageLink = SHERWANI_ELBOW_ROUND_IMAGE_LINK;
        String title = SHERWANI_ELBOW_ROUND_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = SHERWANI_SLEEVE_CIRCUM_IMAGE_LINK;
        String title = SHERWANI_SLEEVE_CIRCUM_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addArmHole(String value) {
        String imageLink = SHERWANI_ARMHOLE_IMAGE_LINK;
        String title = SHERWANI_ARMHOLE_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaistLower(String value) {
        String imageLink = SHERWANI_LOWER_WAIST_IMAGE_LINK;
        String title = SHERWANI_LOWER_WAIST_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addHipCircumLower(String value) {
        String imageLink = SHERWANI_HIP_CIRCUM_LOWER_IMAGE_LINK;
        String title = SHERWANI_LOWER_HIP_CIRCUM_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addThighCircum(String value) {
        String imageLink = SHERWANI_THIGH_CIRCUM_IMAGE_LINK;
        String title = SHERWANI_THIGH_CIRCUM_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCalf(String value) {
        String imageLink = SHERWANI_CALF_IMAGE_LINK;
        String title = SHERWANI_CALF_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBottom(String value) {
        String imageLink = SHERWANI_BOTTOM_IMAGE_LINK;
        String title = SHERWANI_BOTTOM_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addLengthLower(String value) {
        String imageLink = SHERWANI_LOWER_LENGTH_IMAGE_LINK;
        String title = SHERWANI_LOWER_LENGTH_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFly(String value) {
        String imageLink = SHERWANI_FLY_IMAGE_LINK;
        String title = SHERWANI_FLY_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addInseam(String value) {
        String imageLink = SHERWANI_INSEAM_IMAGE_LINK;
        String title = SHERWANI_INSEAM_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCrotch(String value) {
        String imageLink = SHERWANI_CROTCH_IMAGE_LINK;
        String title = SHERWANI_CROTCH_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        return subOutfitMap;
    }

    public String getSubOutfitName(Integer ordinal) {
        return getSubOutfitMap().get(ordinal);
    }

    @Override
    public boolean isPortfolioEligible() {
        return false;
    }
}
