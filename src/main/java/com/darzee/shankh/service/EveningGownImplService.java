package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.*;
import com.darzee.shankh.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.darzee.shankh.constants.Constants.DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
import static com.darzee.shankh.constants.Constants.ImageLinks.*;
import static com.darzee.shankh.constants.Constants.MeasurementKeys.*;
import static com.darzee.shankh.constants.Constants.MeasurementTitles.*;

@Service
public class EveningGownImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void setMeasurementDetailsInObject(MeasurementRequest measurementDetails,
                                              MeasurementsDAO measurementsDAO,
                                              MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        Map<String, Double> measurementValue = measurementsDAO.getMeasurementValue();

        if (measurementValue == null) {
            measurementValue = new HashMap<>();
        }

        if (measurementDetails.getGownLength() != null) {
            measurementValue.put(GOWN_LENGTH_MEASUREMENT_KEY, measurementDetails.getGownLength() * multiplyingFactor);
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
        if (measurementDetails.getSleeveCircumference() != null) {
            measurementValue.put(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY,
                    measurementDetails.getSleeveCircumference() * multiplyingFactor);
        }
        if (measurementDetails.getFrontNeckDepth() != null) {
            measurementValue.put(FRONT_NECK_DEPTH_MEASUREMENT_KEY,
                    measurementDetails.getFrontNeckDepth() * multiplyingFactor);
        }
        if (measurementDetails.getBackNeckDepth() != null) {
            measurementValue.put(BACK_NECK_DEPTH_MEASUREMENT_KEY, measurementDetails.getBackNeckDepth() * multiplyingFactor);
        }
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue =
                objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);

        outfitMeasurementDetails.setGownLength(measurementValue.get(GOWN_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setShoulder(measurementValue.get(SHOULDER_MEASUREMENT_KEY));
        outfitMeasurementDetails.setUpperChest(measurementValue.get(UPPER_CHEST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBust(measurementValue.get(BUST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setWaist(measurementValue.get(WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSeat(measurementValue.get(SEAT_MEASUREMENT_KEY));
        outfitMeasurementDetails.setArmHole(measurementValue.get(ARM_HOLE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveLength(measurementValue.get(SLEEVE_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveCircumference(measurementValue.get(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setFrontNeckDepth(measurementValue.get(FRONT_NECK_DEPTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBackNeckDepth(measurementValue.get(BACK_NECK_DEPTH_MEASUREMENT_KEY));

        return outfitMeasurementDetails;
    }

    @Override
    public boolean haveMandatoryParams(MeasurementRequest measurementDetails) {
        return measurementDetails.getGownLength() != null &&
                measurementDetails.getShoulder() != null &&
                measurementDetails.getUpperChest() != null &&
                measurementDetails.getBust() != null &&
                measurementDetails.getWaist() != null &&
                measurementDetails.getSeat() != null &&
                measurementDetails.getArmHole() != null &&
                measurementDetails.getSleeveLength() != null &&
                measurementDetails.getSleeveCircumference() != null &&
                measurementDetails.getFrontNeckDepth() != null &&
                measurementDetails.getBackNeckDepth() != null;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementsDAO measurementsDAO, MeasurementScale scale) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;


        measurementDetailsResponseList.add(
                addGownLength(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(GOWN_LENGTH_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addShoulder(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(SHOULDER_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addUpperChest(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(UPPER_CHEST_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addBust(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(BUST_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addWaist(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(WAIST_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addSeat(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(SEAT_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addArmHole(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(ARM_HOLE_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addSleeveLength(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(SLEEVE_LENGTH_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addSleeveCircumference(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addFrontNeckDepth(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(FRONT_NECK_DEPTH_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addBackNeckDepth(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(BACK_NECK_DEPTH_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));


        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(GOWN_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(NIGHT_GOWN_OUTFIT_TYPE_HEADING);
        overallMeasurementDetails.setInnerMeasurementDetails(Arrays.asList(innerMeasurementDetails));
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.EVENING_GOWN;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitType.getImageLink(), 1);
    }

    private MeasurementDetails addGownLength(String value) {
        String imageLink = GOWN_LENGTH_IMAGE_LINK;
        String title = GOWN_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = GOWN_SHOULDER_IMAGE_LINK;
        String title = GOWN_SHOULDER_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addUpperChest(String value) {
        String imageLink = GOWN_UPPER_CHEST_IMAGE_LINK;
        String title = GOWN_UPPER_CHEST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBust(String value) {
        String imageLink = GOWN_BUST_IMAGE_LINK;
        String title = GOWN_BUST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = GOWN_WAIST_IMAGE_LINK;
        String title = GOWN_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeat(String value) {
        String imageLink = GOWN_SEAT_IMAGE_LINK;
        String title = GOWN_SEAT_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addArmHole(String value) {
        String imageLink = GOWN_ARMHOLE_IMAGE_LINK;
        String title = GOWN_ARMHOLE_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = GOWN_SLEEVE_LENGTH_IMAGE_LINK;
        String title = GOWN_SLEEVE_LENGTH_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = GOWN_SLEEVE_CIRCUMFERENCE_IMAGE_LINK;
        String title = GOWN_SLEEVE_CIRCUM_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFrontNeckDepth(String value) {
        String imageLink = GOWN_FRONT_NECK_DEPTH_IMAGE_LINK;
        String title = GOWN_FRONT_NECK_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBackNeckDepth(String value) {
        String imageLink = GOWN_BACK_NECK_DEPTH_IMAGE_LINK;
        String title = GOWN_BACK_NECK_DEPTH_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }

}
