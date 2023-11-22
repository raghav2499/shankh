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
public class MensSuitImplService implements OutfitTypeService {

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
        if (measurementDetails.getSeat() != null) {
            measurementValue.put(SEAT_MEASUREMENT_KEY, measurementDetails.getSeat() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveLength() != null) {
            measurementValue.put(SLEEVE_LENGTH_MEASUREMENT_KEY, measurementDetails.getSleeveLength() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveCircumference() != null) {
            measurementValue.put(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY,
                    measurementDetails.getSleeveCircumference() * multiplyingFactor);
        }
        if (measurementDetails.getCalf() != null) {
            measurementValue.put(CALF_MEASUREMENT_KEY, measurementDetails.getCalf() * multiplyingFactor);
        }
        if (measurementDetails.getBottom() != null) {
            measurementValue.put(BOTTOM_MEASUREMENT_KEY, measurementDetails.getBottom() * multiplyingFactor);
        }
        if (measurementDetails.getPantLength() != null) {
            measurementValue.put(PANT_LENGTH_MEASUREMENT_KEY, measurementDetails.getPantLength() * multiplyingFactor);
        }
        if (measurementDetails.getFly() != null) {
            measurementValue.put(FLY_MEASUREMENT_KEY, measurementDetails.getFly() * multiplyingFactor);
        }
        if (measurementDetails.getBottomSeat() != null) {
            measurementValue.put(BOTTOM_SEAT_MEASUREMENT_KEY, measurementDetails.getBottomSeat() * multiplyingFactor);
        }
        if (measurementDetails.getBottomWaist() != null) {
            measurementValue.put(BOTTOM_WAIST_MEASUREMENT_KEY, measurementDetails.getBottomWaist() * multiplyingFactor);
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

        outfitMeasurementDetails.setShirtLength(measurementValue.get(SHIRT_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setNeck(measurementValue.get(NECK_MEASUREMENT_KEY));
        outfitMeasurementDetails.setShoulder(measurementValue.get(SHOULDER_MEASUREMENT_KEY));
        outfitMeasurementDetails.setChest(measurementValue.get(CHEST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setWaist(measurementValue.get(WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSeat(measurementValue.get(SEAT_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveLength(measurementValue.get(SLEEVE_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveCircumference(measurementValue.get(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCalf(measurementValue.get(CALF_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottom(measurementValue.get(BOTTOM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setFly(measurementValue.get(FLY_MEASUREMENT_KEY));
        outfitMeasurementDetails.setPantLength(measurementValue.get(PANT_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottomSeat(measurementValue.get(BOTTOM_SEAT_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottomWaist(measurementValue.get(BOTTOM_WAIST_MEASUREMENT_KEY));
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
                    addLengthUpper(measurementsDAO.getMeasurement(SHIRT_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addNeck(measurementsDAO.getMeasurement(NECK_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addShoulder(measurementsDAO.getMeasurement(SHOULDER_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addChest(measurementsDAO.getMeasurement(CHEST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addWaistUpper(measurementsDAO.getMeasurement(WAIST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSeatUpper(measurementsDAO.getMeasurement(SEAT_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSleeveLength(measurementsDAO.getMeasurement(SLEEVE_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSleeveCircumference(measurementsDAO.getMeasurement(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY, dividingFactor)));
        }
        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }
        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(MENS_SUIT_UPPER_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(MENS_SUIT_TOP_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.MENS_SUIT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 2);
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
                    addWaistLower(measurementsDAO.getMeasurement(BOTTOM_WAIST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSeatLower(measurementsDAO.getMeasurement(BOTTOM_SEAT_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addCalf(measurementsDAO.getMeasurement(CALF_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBottom(measurementsDAO.getMeasurement(BOTTOM_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addLengthLower(measurementsDAO.getMeasurement(PANT_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addFly(measurementsDAO.getMeasurement(FLY_MEASUREMENT_KEY, dividingFactor)));
        }
        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(MENS_SUIT_LOWER_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(MENS_SUIT_PANTS_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private MeasurementDetails addLengthUpper(String value) {
        String imageLink = MENS_SUIT_UPPER_LENGTH_IMAGE_LINK;
        String title = MENS_SUIT_UPPER_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addNeck(String value) {
        String imageLink = MENS_SUIT_NECK_IMAGE_LINK;
        String title = MENS_SUIT_NECK_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = MENS_SUIT_SHOULDER_IMAGE_LINK;
        String title = MENS_SUIT_SHOULDER_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addChest(String value) {
        String imageLink = MENS_SUIT_CHEST_IMAGE_LINK;
        String title = MENS_SUIT_CHEST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaistUpper(String value) {
        String imageLink = MENS_SUIT_UPPER_WAIST_IMAGE_LINK;
        String title = MENS_SUIT_UPPER_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeatUpper(String value) {
        String imageLink = MENS_SUIT_UPPER_SEAT_IMAGE_LINK;
        String title = MENS_SUIT_UPPER_SEAT_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = MENS_SUIT_SLEEVE_IMAGE_LINK;
        String title = MENS_SUIT_SLEEVE_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = MENS_SUIT_SLEEVE_CIRCUM_IMAGE_LINK;
        String title = MENS_SUIT_SLEEVE_CIRCUM_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaistLower(String value) {
        String imageLink = MENS_SUIT_LOWER_WAIST_IMAGE_LINK;
        String title = MENS_SUIT_LOWER_WAIST_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeatLower(String value) {
        String imageLink = MENS_SUIT_LOWER_SEAT_IMAGE_LINK;
        String title = MENS_SUIT_LOWER_SEAT_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCalf(String value) {
        String imageLink = MENS_SUIT_CALF_IMAGE_LINK;
        String title = MENS_SUIT_CALF_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBottom(String value) {
        String imageLink = MENS_SUIT_BOTTOM_IMAGE_LINK;
        String title = MENS_SUIT_BOTTOM_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addLengthLower(String value) {
        String imageLink = MENS_SUIT_LOWER_LENGTH_IMAGE_LINK;
        String title = MENS_SUIT_LOWER_LENGTH_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFly(String value) {
        String imageLink = MENS_SUIT_FLY_IMAGE_LINK;
        String title = MENS_SUIT_FLY_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(33, "Formal Business Suit");
        subOutfitMap.put(34, "Wedding Suit");
        subOutfitMap.put(35, "Bandhgala Suit");
        subOutfitMap.put(36, "Jodhpuri Suit");
        subOutfitMap.put(37, "Indo Western Suit");
        subOutfitMap.put(38, "Tuxedo");
        return subOutfitMap;
    }

    public String getSubOutfitName(Integer ordinal) {
        return getSubOutfitMap().get(ordinal);
    }
}
