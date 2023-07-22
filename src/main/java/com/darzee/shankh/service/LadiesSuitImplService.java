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
public class LadiesSuitImplService implements OutfitTypeService {
    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void setMeasurementDetailsInObject(MeasurementRequest measurementDetails,
                                              MeasurementsDAO measurementsDAO,
                                              MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        Map<String, Double> measurementValue =
                (measurementsDAO != null && measurementsDAO.getMeasurementValue() != null)
                        ? objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class)
                        : new HashMap<>();

        if (measurementValue == null) {
            measurementValue = new HashMap<>();
        }

        if (measurementDetails.getKameezLength() != null) {
            measurementValue.put(KAMEEZ_LENGTH_MEASUREMENT_KEY, measurementDetails.getKameezLength() * multiplyingFactor);
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
            measurementValue.put(BACK_NECK_DEPTH_MEASUREMENT_KEY,
                    measurementDetails.getBackNeckDepth() * multiplyingFactor);
        }
        if (measurementDetails.getSalwarHip() != null) {
            measurementValue.put(SALWAR_HIP_MEASUREMENT_KEY, measurementDetails.getSalwarHip() * multiplyingFactor);
        }
        if (measurementDetails.getKnee() != null) {
            measurementValue.put(KNEE_MEASUREMENT_KEY, measurementDetails.getKnee() * multiplyingFactor);
        }
        if (measurementDetails.getSalwarLength() != null) {
            measurementValue.put(SALWAR_LENGTH_MEASUREMENT_KEY, measurementDetails.getSalwarLength() * multiplyingFactor);
        }
        if (measurementDetails.getAnkle() != null) {
            measurementValue.put(ANKLE_MEASUREMENT_KEY, measurementDetails.getAnkle() * multiplyingFactor);
        }
        measurementsDAO.setMeasurementValue(measurementValue);

    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue =
                objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);

        outfitMeasurementDetails.setKameezLength(measurementValue.get(KAMEEZ_LENGTH_MEASUREMENT_KEY));
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
        outfitMeasurementDetails.setSalwarHip(measurementValue.get(SALWAR_HIP_MEASUREMENT_KEY));
        outfitMeasurementDetails.setKnee(measurementValue.get(KNEE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSalwarLength(measurementValue.get(SALWAR_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setAnkle(measurementValue.get(ANKLE_MEASUREMENT_KEY));

        return outfitMeasurementDetails;
    }

    @Override
    public boolean haveMandatoryParams(MeasurementRequest measurementDetails) {
        return measurementDetails.getKameezLength() != null &&
                measurementDetails.getShoulder() != null &&
                measurementDetails.getUpperChest() != null &&
                measurementDetails.getBust() != null &&
                measurementDetails.getWaist() != null &&
                measurementDetails.getSeat() != null &&
                measurementDetails.getArmHole() != null &&
                measurementDetails.getSleeveLength() != null &&
                measurementDetails.getSleeveCircumference() != null &&
                measurementDetails.getFrontNeckDepth() != null &&
                measurementDetails.getBackNeckDepth() != null &&
                measurementDetails.getSalwarHip() != null &&
                measurementDetails.getKnee() != null &&
                measurementDetails.getSalwarLength() != null &&
                measurementDetails.getAnkle() != null;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementsDAO measurementsDAO, MeasurementScale scale) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<InnerMeasurementDetails> innerMeasurementDetails = new ArrayList<>();
        innerMeasurementDetails.add(setMeasurementDetailsInObjectTop(measurementsDAO, scale));
        innerMeasurementDetails.add(setMeasurementDetailsInObjectBottom(measurementsDAO, scale));
        overallMeasurementDetails.setInnerMeasurementDetails(innerMeasurementDetails);
        return overallMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectTop(MeasurementsDAO measurementsDAO, MeasurementScale scale) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;

        measurementDetailsResponseList.add(
                addKameezLength(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(KAMEEZ_LENGTH_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
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
        innerMeasurementDetails.setOutfitImageLink(LADIES_SUIT_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(LADIES_SUIT_KAMEEZ_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectBottom(MeasurementsDAO measurementsDAO, MeasurementScale scale) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;

        measurementDetailsResponseList.add(
                addSalwarHip(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(SALWAR_HIP_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addKnee(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(KNEE_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addSalwarLength(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(SALWAR_LENGTH_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addAnkle(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(ANKLE_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(LADIES_SUIT_LOWER_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(LADIES_SUIT_SALWAR_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.LADIES_SUIT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitType.getImageLink(), 2);
    }

    private MeasurementDetails addKameezLength(String value) {
        String imageLink = LADIES_SUIT_LENGTH_IMAGE_LINK;
        String title = LADIES_SUIT_KAMEEZ_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = LADIES_SUIT_SHOULDER_IMAGE_LINK;
        String title = LADIES_SUIT_SHOULDER_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addUpperChest(String value) {
        String imageLink = LADIES_SUIT_UPPER_CHEST_IMAGE_LINK;
        String title = LADIES_SUIT_UPPER_CHEST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBust(String value) {
        String imageLink = LADIES_SUIT_BUST_IMAGE_LINK;
        String title = LADIES_SUIT_BUST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = LADIES_SUIT_WAIST_IMAGE_LINK;
        String title = LADIES_SUIT_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeat(String value) {
        String imageLink = LADIES_SUIT_SEAT_IMAGE_LINK;
        String title = LADIES_SUIT_SEAT_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addArmHole(String value) {
        String imageLink = LADIES_SUIT_ARMHOLE_IMAGE_LINK;
        String title = LADIES_SUIT_ARMHOLE_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = LADIES_SUIT_SLEEVE_LENGTH_IMAGE_LINK;
        String title = LADIES_SUIT_SLEEVE_LENGTH_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = LADIES_SUIT_SLEEVE_CIRCUM_IMAGE_LINK;
        String title = LADIES_SUIT_SLEEVE_CIRCUM_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFrontNeckDepth(String value) {
        String imageLink = LADIES_SUIT_FRONT_NECK_DEPTH_IMAGE_LINK;
        String title = LADIES_SUIT_FRONT_NECK_DEPTH_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBackNeckDepth(String value) {
        String imageLink = LADIES_SUIT_BACK_NECK_DEPTH_IMAGE_LINK;
        String title = LADIES_SUIT_BACK_NECK_DEPTH_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSalwarHip(String value) {
        String imageLink = LADIES_SUIT_SALWAR_HIP_IMAGE_LINK;
        String title = LADIES_SUIT_SALWAR_HIP_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addKnee(String value) {
        String imageLink = LADIES_SUIT_KNEE_IMAGE_LINK;
        String title = LADIES_SUIT_KNEE_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSalwarLength(String value) {
        String imageLink = LADIES_SUIT_SALWAR_LENGTH_IMAGE_LINK;
        String title = LADIES_SUIT_SALWAR_LENGTH_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addAnkle(String value) {
        String imageLink = LADIES_SUIT_ANKLE_IMAGE_LINK;
        String title = LADIES_SUIT_ANKLE_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

}
