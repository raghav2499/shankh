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

import java.util.*;
import java.util.stream.Collectors;

import static com.darzee.shankh.constants.ImageLinks.*;
import static com.darzee.shankh.constants.MeasurementKeys.*;
import static com.darzee.shankh.constants.MeasurementTitles.*;

@Service
public class PantImplService implements OutfitTypeService {

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

        if (measurementDetails.getBottomWaist() != null) {
            measurementValue.put(BOTTOM_WAIST_MEASUREMENT_KEY, measurementDetails.getBottomWaist() * multiplyingFactor);
        }
        if (measurementDetails.getBottomSeat() != null) {
            measurementValue.put(BOTTOM_SEAT_MEASUREMENT_KEY, measurementDetails.getBottomSeat() * multiplyingFactor);
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
        if (measurementDetails.getPantLength() != null) {
            measurementValue.put(PANT_LENGTH_MEASUREMENT_KEY, measurementDetails.getPantLength() * multiplyingFactor);
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
        measurementsDAO.setMeasurementValue(measurementValue);
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue =
                (measurementsDAO != null && measurementsDAO.getMeasurementValue() != null)
                        ? objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class)
                        : new HashMap<>();

        outfitMeasurementDetails.setBottomWaist(measurementValue.get(BOTTOM_WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottomSeat(measurementValue.get(BOTTOM_SEAT_MEASUREMENT_KEY));
        outfitMeasurementDetails.setThigh(measurementValue.get(THIGH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCalf(measurementValue.get(CALF_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottom(measurementValue.get(BOTTOM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setPantLength(measurementValue.get(PANT_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setFly(measurementValue.get(FLY_MEASUREMENT_KEY));
        outfitMeasurementDetails.setInSeam(measurementValue.get(IN_SEAM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCrotch(measurementValue.get(CROTCH_MEASUREMENT_KEY));

        return outfitMeasurementDetails;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementsDAO measurementsDAO,
                                                           MeasurementScale scale,
                                                           Boolean nonEmptyValuesOnly) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        if (measurementValue != null) {
            measurementDetailsResponseList.add(
                    addWaist(measurementsDAO.getMeasurement(BOTTOM_WAIST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addSeat(measurementsDAO.getMeasurement(BOTTOM_SEAT_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addThigh(measurementsDAO.getMeasurement(THIGH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addCalf(measurementsDAO.getMeasurement(CALF_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBottom(measurementsDAO.getMeasurement(BOTTOM_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addPantLength(measurementsDAO.getMeasurement(PANT_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addFly(measurementsDAO.getMeasurement(FLY_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addInSeam(measurementsDAO.getMeasurement(IN_SEAM_MEASUREMENT_KEY, dividingFactor)));
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
        innerMeasurementDetails.setOutfitImageLink(PANTS_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(PANTS_OUTFIT_TYPE_HEADING);
        overallMeasurementDetails.setInnerMeasurementDetails(Arrays.asList(innerMeasurementDetails));
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.PANTS;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 1, isPortfolioEligible());
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = PANTS_WAIST_IMAGE_LINK;
        String title = PANTS_WAIST_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeat(String value) {
        String imageLink = PANTS_SEAT_IMAGE_LINK;
        String title = PANTS_SEAT_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addThigh(String value) {
        String imageLink = PANTS_THIGH_IMAGE_LINK;
        String title = PANTS_THIGH_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCalf(String value) {
        String imageLink = PANTS_CALF_IMAGE_LINK;
        String title = PANTS_CALF_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBottom(String value) {
        String imageLink = PANTS_BOTTOM_IMAGE_LINK;
        String title = PANTS_BOTTOM_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addPantLength(String value) {
        String imageLink = PANTS_LENGTH_IMAGE_LINK;
        String title = PANTS_LENGTH_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFly(String value) {
        String imageLink = PANTS_FLY_IMAGE_LINK;
        String title = PANTS_FLY_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addInSeam(String value) {
        String imageLink = PANTS_IN_SEAM_IMAGE_LINK;
        String title = PANTS_IN_SEAM_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCrotch(String value) {
        String imageLink = PANTS_CROTCH_IMAGE_LINK;
        String title = PANTS_CROTCH_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(51, "Formal Trousers");
        subOutfitMap.put(52, "Chinos");
        subOutfitMap.put(53, "Jeans");
        subOutfitMap.put(54, "Cargos");
        subOutfitMap.put(55, "Trackpants");
        subOutfitMap.put(56, "Joggers");
        subOutfitMap.put(57, "Formal Trousers With Ethnic Touch");
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
