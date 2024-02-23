package com.darzee.shankh.service.outfits;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementRevisionsDAO;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.*;
import com.darzee.shankh.service.MeasurementRevisionService;
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
public class ShirtImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeasurementRevisionService measurementRevisionService;

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
        if (measurementDetails.getSeat() != null) {
            measurementValue.put(SEAT_MEASUREMENT_KEY, measurementDetails.getSeat() * multiplyingFactor);
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
        if (measurementDetails.getArmHole() != null) {
            measurementValue.put(ARM_HOLE_MEASUREMENT_KEY, measurementDetails.getArmHole() * multiplyingFactor);
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
        outfitMeasurementDetails.setSeat(measurementValue.get(SEAT_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveLength(measurementValue.get(SLEEVE_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBicep(measurementValue.get(BICEP_MEASUREMENT_KEY));
        outfitMeasurementDetails.setElbowRound(measurementValue.get(ELBOW_ROUND_MEASUREMENT_KEY));
        outfitMeasurementDetails.setSleeveCircumference(measurementValue.get(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setArmHole(measurementValue.get(ARM_HOLE_MEASUREMENT_KEY));

        return outfitMeasurementDetails;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementRevisionsDAO revisionsDAO, MeasurementScale scale,
                                                           Boolean nonEmptyValuesOnly) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;

        String measurementImageLink = null;
        if (CollectionUtils.isEmpty(revisionsDAO.getMeasurementValue())) {
            measurementImageLink = measurementRevisionService.getMeasurementRevisionImageLink(revisionsDAO.getId());
        } else {
            measurementDetailsResponseList.add(
                    addShirtLength(revisionsDAO.getMeasurement(SHIRT_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addNeck(revisionsDAO.getMeasurement(NECK_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addShoulder(revisionsDAO.getMeasurement(SHOULDER_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addChest(revisionsDAO.getMeasurement(CHEST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addWaist(revisionsDAO.getMeasurement(WAIST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSeat(revisionsDAO.getMeasurement(SEAT_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSleeveLength(revisionsDAO.getMeasurement(SLEEVE_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBicep(revisionsDAO.getMeasurement(BICEP_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addElbowRound(revisionsDAO.getMeasurement(ELBOW_ROUND_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSleeveCircumference(revisionsDAO.getMeasurement(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addArmhole(revisionsDAO.getMeasurement(ARM_HOLE_MEASUREMENT_KEY, dividingFactor)));

            if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
                measurementDetailsResponseList = measurementDetailsResponseList
                        .stream()
                        .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                        .collect(Collectors.toList());
            }
        }
        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(SHIRT_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(SHIRT_OUTFIT_TYPE_HEADING);
        overallMeasurementDetails.setInnerMeasurementDetails(Arrays.asList(innerMeasurementDetails));
        overallMeasurementDetails.setMeasurementImageLink(measurementImageLink);
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.SHIRT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 1, isPortfolioEligible());
    }

    private MeasurementDetails addShirtLength(String value) {
        String imageLink = SHIRT_LENGTH_IMAGE_LINK;
        String title = SHIRT_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addNeck(String value) {
        String imageLink = SHIRT_NECK_IMAGE_LINK;
        String title = SHIRT_NECK_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = SHIRT_SHOULDER_IMAGE_LINK;
        String title = SHIRT_SHOULDER_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addChest(String value) {
        String imageLink = SHIRT_CHEST_IMAGE_LINK;
        String title = SHIRT_CHEST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = SHIRT_WAIST_IMAGE_LINK;
        String title = SHIRT_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeat(String value) {
        String imageLink = SHIRT_SEAT_IMAGE_LINK;
        String title = SHIRT_SEAT_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = SHIRT_SLEEVE_LENGTH_IMAGE_LINK;
        String title = SHIRT_SLEEVE_LENGTH_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBicep(String value) {
        String imageLink = SHIRT_BICEP_IMAGE_LINK;
        String title = SHIRT_BICEP_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addElbowRound(String value) {
        String imageLink = SHIRT_ELBOW_ROUND_IMAGE_LINK;
        String title = SHIRT_ELBOW_ROUND_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = SHIRT_SLEEVE_CIRCUM_IMAGE_LINK;
        String title = SHIRT_SLEEVE_CIRCUM_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addArmhole(String value) {
        String imageLink = SHIRT_ARMHOLE_IMAGE_LINK;
        String title = SHIRT_ARMHOLE_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(76, "Formal Shirts");
        subOutfitMap.put(77, "Casual Shirts");
        subOutfitMap.put(78, "Printed Shirts");
        subOutfitMap.put(79, "Striped Shirts");
        subOutfitMap.put(80, "Checkered Shirts");
        subOutfitMap.put(81, "Denim Shirts");
        subOutfitMap.put(82, "Polo Shirts");
        subOutfitMap.put(83, "Kurta Shirts");
        subOutfitMap.put(84, "Nehrucollar Shirts");
        return subOutfitMap;
    }

    public String getSubOutfitName(Integer ordinal) {
        return getSubOutfitMap().get(ordinal);
    }

    @Override
    public boolean isPortfolioEligible() {
        return true;
    }
}
