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
public class KurtaPyjamaImplService implements OutfitTypeService {

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

        if (measurementDetails.getKurtaLength() != null) {
            measurementValue.put(KURTA_LENGTH_MEASUREMENT_KEY, measurementDetails.getKurtaLength() * multiplyingFactor);
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
        if (measurementDetails.getPyjamaLength() != null) {
            measurementValue.put(PYJAMA_LENGTH_MEASUREMENT_KEY,
                    measurementDetails.getPyjamaLength() * multiplyingFactor);
        }
        if (measurementDetails.getPyjamaHip() != null) {
            measurementValue.put(PYJAMA_HIP_MEASUREMENT_KEY, measurementDetails.getPyjamaHip() * multiplyingFactor);
        }
        if (measurementDetails.getKnee() != null) {
            measurementValue.put(KNEE_MEASUREMENT_KEY, measurementDetails.getKnee() * multiplyingFactor);
        }
        if (measurementDetails.getAnkle() != null) {
            measurementValue.put(ANKLE_MEASUREMENT_KEY, measurementDetails.getAnkle() * multiplyingFactor);
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

        outfitMeasurementDetails.setKurtaLength(measurementValue.get(KURTA_LENGTH_MEASUREMENT_KEY));
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
        outfitMeasurementDetails.setPyjamaLength(measurementValue.get(PYJAMA_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setPyjamaHip(measurementValue.get(PYJAMA_HIP_MEASUREMENT_KEY));
        outfitMeasurementDetails.setKnee(measurementValue.get(KNEE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setAnkle(measurementValue.get(ANKLE_MEASUREMENT_KEY));

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

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.KURTA_PYJAMA;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 2, isPortfolioEligible());
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectTop(MeasurementRevisionsDAO revisionsDAO,
                                                                     MeasurementScale scale,
                                                                     Boolean nonEmptyValuesOnly) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;

        measurementDetailsResponseList.add(
                addKurtaLength(revisionsDAO.getMeasurement(KURTA_LENGTH_MEASUREMENT_KEY, dividingFactor)));
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
        measurementDetailsResponseList.add(
                addArmHole(revisionsDAO.getMeasurement(ARM_HOLE_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addSleeveLength(revisionsDAO.getMeasurement(SLEEVE_LENGTH_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addSleeveCircumference(revisionsDAO.getMeasurement(SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addFrontNeckDepth(revisionsDAO.getMeasurement(FRONT_NECK_DEPTH_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addBackNeckDepth(revisionsDAO.getMeasurement(BACK_NECK_DEPTH_MEASUREMENT_KEY, dividingFactor)));

        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }
        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(KURTA_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(MENS_KURTA_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectBottom(MeasurementRevisionsDAO revisionsDAO,
                                                                        MeasurementScale scale,
                                                                        Boolean nonEmptyValuesOnly) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;

        measurementDetailsResponseList.add(
                addPyjamaLength(revisionsDAO.getMeasurement(PYJAMA_LENGTH_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addPyjamaHip(revisionsDAO.getMeasurement(PYJAMA_HIP_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addKnee(revisionsDAO.getMeasurement(KNEE_MEASUREMENT_KEY, dividingFactor)));
        measurementDetailsResponseList.add(
                addAnkle(revisionsDAO.getMeasurement(ANKLE_MEASUREMENT_KEY, dividingFactor)));

        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(PYJAMA_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(MENS_PYJAMA_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private MeasurementDetails addKurtaLength(String value) {
        String imageLink = KURTA_LENGTH_IMAGE_LINK;
        String title = KURTA_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = KURTA_SHOULDER_IMAGE_LINK;
        String title = KURTA_SHOULDER_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addUpperChest(String value) {
        String imageLink = KURTA_UPPER_CHEST_IMAGE_LINK;
        String title = KURTA_UPPER_CHEST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBust(String value) {
        String imageLink = KURTA_BUST_IMAGE_LINK;
        String title = KURTA_BUST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = KURTA_WAIST_IMAGE_LINK;
        String title = KURTA_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeat(String value) {
        String imageLink = KURTA_SEAT_IMAGE_LINK;
        String title = KURTA_SEAT_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addArmHole(String value) {
        String imageLink = KURTA_ARMHOLE_IMAGE_LINK;
        String title = KURTA_ARMHOLE_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = KURTA_SLEEVE_LENGTH_IMAGE_LINK;
        String title = KURTA_SLEEVE_LENGTH_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = KURTA_SLEEVE_CIRCUM_IMAGE_LINK;
        String title = KURTA_SLEEVE_CIRCUM_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFrontNeckDepth(String value) {
        String imageLink = KURTA_FRONT_NECK_DEPTH_IMAGE_LINK;
        String title = KURTA_FRONT_NECK_DEPTH_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBackNeckDepth(String value) {
        String imageLink = KURTA_BACK_NECK_DEPTH_IMAGE_LINK;
        String title = KURTA_BACK_NECK_DEPTH_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addPyjamaLength(String value) {
        String imageLink = PYJAMA_LENGTH_IMAGE_LINK;
        String title = PYJAMA_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addPyjamaHip(String value) {
        String imageLink = PYJAMA_HIP_IMAGE_LINK;
        String title = PYJAMA_HIP_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addKnee(String value) {
        String imageLink = PYJAMA_KNEE_IMAGE_LINK;
        String title = PYJAMA_KNEE_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addAnkle(String value) {
        String imageLink = PYJAMA_ANKLE_IMAGE_LINK;
        String title = PYJAMA_ANKLE_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(4, "Pathani Suit");
        subOutfitMap.put(5, "Sherwani");
        subOutfitMap.put(6, "Achkan");
        subOutfitMap.put(7, "Jodhpuri Suit");
        subOutfitMap.put(8, "Indo Western Kurta Pyjama");
        subOutfitMap.put(9, "Designer Kurta Pyjama");
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
