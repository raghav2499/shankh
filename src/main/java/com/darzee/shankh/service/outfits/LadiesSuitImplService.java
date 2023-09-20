package com.darzee.shankh.service.outfits;

import com.darzee.shankh.constants.Constants;
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
public class LadiesSuitImplService implements OutfitTypeService {
    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Override
    public void setMeasurementDetailsInObject(MeasurementRequest measurementDetails,
                                              MeasurementsDAO measurementsDAO,
                                              MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        Map<String, Double> measurementValue = measurementsDAO.getMeasurementValue();

        if (measurementValue == null) {
            measurementValue = new HashMap<>();
        }

        if (measurementDetails.getKameezLength() != null) {
            measurementValue.put(KAMEEZ_LENGTH_MEASUREMENT_KEY, measurementDetails.getKameezLength() * multiplyingFactor);
        }
        if (measurementDetails.getUpperChest() != null) {
            measurementValue.put(UPPER_CHEST_MEASUREMENT_KEY, measurementDetails.getUpperChest() * multiplyingFactor);
        }
        if (measurementDetails.getBust() != null) {
            measurementValue.put(BUST_MEASUREMENT_KEY, measurementDetails.getBust() * multiplyingFactor);
        }
        if (measurementDetails.getBelowBust() != null) {
            measurementValue.put(BELOW_BUST_MEASUREMENT_KEY, measurementDetails.getBelowBust() * multiplyingFactor);
        }
        if (measurementDetails.getWaist() != null) {
            measurementValue.put(WAIST_MEASUREMENT_KEY, measurementDetails.getWaist() * multiplyingFactor);
        }
        if (measurementDetails.getHipCircum() != null) {
            measurementValue.put(HIP_CIRCUM_MEASUREMENT_KEY, measurementDetails.getHipCircum() * multiplyingFactor);
        }
        if (measurementDetails.getShoulder() != null) {
            measurementValue.put(SHOULDER_MEASUREMENT_KEY, measurementDetails.getShoulder() * multiplyingFactor);
        }
        if (measurementDetails.getArmHole() != null) {
            measurementValue.put(ARM_HOLE_MEASUREMENT_KEY, measurementDetails.getArmHole() * multiplyingFactor);
        }
        if (measurementDetails.getBicep() != null) {
            measurementValue.put(BICEP_MEASUREMENT_KEY, measurementDetails.getBicep() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveLength() != null) {
            measurementValue.put(SLEEVE_LENGTH_MEASUREMENT_KEY, measurementDetails.getSleeveLength() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveCircumference() != null) {
            measurementValue.put(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY,
                    measurementDetails.getSleeveCircumference() * multiplyingFactor);
        }
        if (measurementDetails.getCrossFront() != null) {
            measurementValue.put(CROSS_FRONT_MEASUREMENT_KEY,
                    measurementDetails.getCrossFront() * multiplyingFactor);
        }
        if (measurementDetails.getCrossBack() != null) {
            measurementValue.put(CROSS_BACK_MEASUREMENT_KEY,
                    measurementDetails.getCrossBack() * multiplyingFactor);
        }
        if (measurementDetails.getFrontNeckDepth() != null) {
            measurementValue.put(FRONT_NECK_DEPTH_MEASUREMENT_KEY,
                    measurementDetails.getFrontNeckDepth() * multiplyingFactor);
        }
        if (measurementDetails.getBackNeckDepth() != null) {
            measurementValue.put(BACK_NECK_DEPTH_MEASUREMENT_KEY,
                    measurementDetails.getBackNeckDepth() * multiplyingFactor);
        }
        if (measurementDetails.getSalwarLength() != null) {
            measurementValue.put(SALWAR_LENGTH_MEASUREMENT_KEY, measurementDetails.getSalwarLength() * multiplyingFactor);
        }
        if (measurementDetails.getBottomWaist() != null) {
            measurementValue.put(BOTTOM_WAIST_MEASUREMENT_KEY, measurementDetails.getBottomWaist() * multiplyingFactor);
        }
        if (measurementDetails.getThigh() != null) {
            measurementValue.put(THIGH_MEASUREMENT_KEY, measurementDetails.getThigh() * multiplyingFactor);
        }
        if (measurementDetails.getKnee() != null) {
            measurementValue.put(KNEE_MEASUREMENT_KEY, measurementDetails.getKnee() * multiplyingFactor);
        }
        if (measurementDetails.getCalf() != null) {
            measurementValue.put(CALF_MEASUREMENT_KEY, measurementDetails.getCalf() * multiplyingFactor);
        }
        if (measurementDetails.getMohri() != null) {
            measurementValue.put(MOHRI_MEASUREMENT_KEY, measurementDetails.getMohri() * multiplyingFactor);
        }
        if (measurementDetails.getCrotch() != null) {
            measurementValue.put(CROTCH_MEASUREMENT_KEY, measurementDetails.getCrotch() * multiplyingFactor);
        }
        measurementsDAO.setMeasurementValue(measurementValue);

    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue =
                (measurementsDAO != null && measurementsDAO.getMeasurementValue() != null)
                        ? objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class)
                        : new HashMap<>();

        outfitMeasurementDetails.setKameezLength(measurementValue.get(KAMEEZ_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setUpperChest(measurementValue.get(UPPER_CHEST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBust(measurementValue.get(BUST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBelowBust(measurementValue.get(BELOW_BUST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setWaist(measurementValue.get(WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setHipCircum(measurementValue.get(HIP_CIRCUM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setShoulder(measurementValue.get(SHOULDER_MEASUREMENT_KEY));
        outfitMeasurementDetails.setArmHole(measurementValue.get(ARM_HOLE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBicep(measurementValue.get(BICEP_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveLength(measurementValue.get(SLEEVE_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveCircumference(measurementValue.get(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCrossFront(measurementValue.get(CROSS_FRONT_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCrossBack(measurementValue.get(CROSS_BACK_MEASUREMENT_KEY));
        outfitMeasurementDetails.setFrontNeckDepth(measurementValue.get(FRONT_NECK_DEPTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBackNeckDepth(measurementValue.get(BACK_NECK_DEPTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSalwarLength(measurementValue.get(SALWAR_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottomWaist(measurementValue.get(BOTTOM_WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setThigh(measurementValue.get(THIGH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setKnee(measurementValue.get(KNEE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCalf(measurementValue.get(CALF_MEASUREMENT_KEY));
        outfitMeasurementDetails.setMohri(measurementValue.get(MOHRI_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCrotch(measurementValue.get(CROTCH_MEASUREMENT_KEY));

        return outfitMeasurementDetails;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementsDAO measurementsDAO,
                                                           MeasurementScale scale,
                                                           Boolean nonEmptyValuesOnly) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<InnerMeasurementDetails> innerMeasurementDetails = new ArrayList<>();
        innerMeasurementDetails.add(setMeasurementDetailsInObjectTop(measurementsDAO, scale, nonEmptyValuesOnly));
        innerMeasurementDetails.add(setMeasurementDetailsInObjectBottom(measurementsDAO, scale, nonEmptyValuesOnly));
        overallMeasurementDetails.setInnerMeasurementDetails(innerMeasurementDetails);
        return overallMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectTop(MeasurementsDAO measurementsDAO,
                                                                     MeasurementScale scale,
                                                                     Boolean nonEmptyValuesOnly) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;

        if (measurementValue != null) {
            measurementDetailsResponseList.add(
                    addKameezLength(measurementsDAO.getMeasurement(KAMEEZ_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addUpperChest(measurementsDAO.getMeasurement(UPPER_CHEST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBust(measurementsDAO.getMeasurement(BUST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBelowBust(measurementsDAO.getMeasurement(BELOW_BUST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addWaist(measurementsDAO.getMeasurement(WAIST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addHipCircum(measurementsDAO.getMeasurement(HIP_CIRCUM_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addShoulder(measurementsDAO.getMeasurement(SHOULDER_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addArmHole(measurementsDAO.getMeasurement(ARM_HOLE_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBicep(measurementsDAO.getMeasurement(BICEP_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSleeveLength(measurementsDAO.getMeasurement(SLEEVE_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSleeveCircumference(measurementsDAO.getMeasurement(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addCrossFront(measurementsDAO.getMeasurement(CROSS_FRONT_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addCrossBack(measurementsDAO.getMeasurement(CROSS_BACK_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addFrontNeckDepth(measurementsDAO.getMeasurement(FRONT_NECK_DEPTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBackNeckDepth(measurementsDAO.getMeasurement(BACK_NECK_DEPTH_MEASUREMENT_KEY, dividingFactor)));
        }
        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(LADIES_SUIT_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(LADIES_SUIT_KAMEEZ_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectBottom(MeasurementsDAO measurementsDAO,
                                                                        MeasurementScale scale,
                                                                        Boolean nonEmptyValuesOnly) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        if (measurementValue != null) {
            measurementDetailsResponseList.add(
                    addSalwarLength(measurementsDAO.getMeasurement(SALWAR_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBottomWaist(measurementsDAO.getMeasurement(BOTTOM_WAIST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addThighCircum(measurementsDAO.getMeasurement(THIGH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addKnee(measurementsDAO.getMeasurement(KNEE_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addCalf(measurementsDAO.getMeasurement(CALF_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addMohri(measurementsDAO.getMeasurement(MOHRI_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addCrotch(measurementsDAO.getMeasurement(CROTCH_MEASUREMENT_KEY, dividingFactor)));
        }
        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }
        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(LADIES_SUIT_LOWER_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(LADIES_SUIT_SALWAR_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.LADIES_SUIT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 2);
    }

    private MeasurementDetails addKameezLength(String value) {
        String imageLink = LADIES_SUIT_LENGTH_IMAGE_LINK;
        String title = LADIES_SUIT_KAMEEZ_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addUpperChest(String value) {
        String imageLink = LADIES_SUIT_UPPER_CHEST_IMAGE_LINK;
        String title = LADIES_SUIT_UPPER_CHEST_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBust(String value) {
        String imageLink = LADIES_SUIT_BUST_IMAGE_LINK;
        String title = LADIES_SUIT_BUST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBelowBust(String value) {
        String imageLink = LADIES_SUIT_BELOW_BUST_IMAGE_LINK;
        String title = LADIES_SUIT_BELOW_BUST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = LADIES_SUIT_WAIST_IMAGE_LINK;
        String title = LADIES_SUIT_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addHipCircum(String value) {
        String imageLink = LADIES_SUIT_HIP_CIRCUM_IMAGE_LINK;
        String title = LADIES_SUIT_HIP_CIRCUM_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = LADIES_SUIT_SHOULDER_IMAGE_LINK;
        String title = LADIES_SUIT_SHOULDER_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addArmHole(String value) {
        String imageLink = LADIES_SUIT_ARMHOLE_IMAGE_LINK;
        String title = LADIES_SUIT_ARMHOLE_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addBicep(String value) {
        String imageLink = LADIES_SUIT_BICEP_IMAGE_LINK;
        String title = LADIES_SUIT_BICEP_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = LADIES_SUIT_SLEEVE_LENGTH_IMAGE_LINK;
        String title = LADIES_SUIT_SLEEVE_LENGTH_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = LADIES_SUIT_SLEEVE_CIRCUM_IMAGE_LINK;
        String title = LADIES_SUIT_SLEEVE_CIRCUM_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCrossFront(String value) {
        String imageLink = LADIES_SUIT_FRONT_CROSS_IMAGE_LINK;
        String title = LADIES_SUIT_FRONT_CROSS_TITLE;
        String index = "12";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCrossBack(String value) {
        String imageLink = LADIES_SUIT_BACK_CROSS_IMAGE_LINK;
        String title = LADIES_SUIT_BACK_CROSS_TITLE;
        String index = "13";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addFrontNeckDepth(String value) {
        String imageLink = LADIES_SUIT_FRONT_NECK_DEPTH_IMAGE_LINK;
        String title = LADIES_SUIT_FRONT_NECK_DEPTH_TITLE;
        String index = "14";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBackNeckDepth(String value) {
        String imageLink = LADIES_SUIT_BACK_NECK_DEPTH_IMAGE_LINK;
        String title = LADIES_SUIT_BACK_NECK_DEPTH_TITLE;
        String index = "15";
        return new MeasurementDetails(imageLink, title, value, index);
    }


    private MeasurementDetails addSalwarLength(String value) {
        String imageLink = LADIES_SUIT_SALWAR_LENGTH_IMAGE_LINK;
        String title = LADIES_SUIT_SALWAR_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addBottomWaist(String value) {
        String imageLink = LADIES_SUIT_SALWAR_WAIST_IMAGE_LINK;
        String title = LADIES_SUIT_SALWAR_WAIST_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addThighCircum(String value) {
        String imageLink = LADIES_SUIT_SALWAR_THIGH_IMAGE_LINK;
        String title = LADIES_SUIT_SALWAR_THIGH_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addKnee(String value) {
        String imageLink = LADIES_SUIT_KNEE_IMAGE_LINK;
        String title = LADIES_SUIT_KNEE_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCalf(String value) {
        String imageLink = LADIES_SUIT_CALF_IMAGE_LINK;
        String title = LADIES_SUIT_CALF_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addMohri(String value) {
        String imageLink = LADIES_SUIT_MOHRI_IMAGE_LINK;
        String title = LADIES_SUIT_MOHRI_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCrotch(String value) {
        String imageLink = LADIES_SUIT_CROTCH_IMAGE_LINK;
        String title = LADIES_SUIT_CROTCH_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }


    private MeasurementDetails addAnkle(String value) {
        String imageLink = LADIES_SUIT_ANKLE_IMAGE_LINK;
        String title = LADIES_SUIT_ANKLE_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(10, "salwar_kameez");
        subOutfitMap.put(11, "anarkali_suit");
        subOutfitMap.put(12, "churidar_suit");
        subOutfitMap.put(13, "patiala_suit");
        subOutfitMap.put(14, "palazzo_suit");
        subOutfitMap.put(15, "sharara_suit");
        subOutfitMap.put(16, "lehenga_suit");
        return subOutfitMap;
    }
}
