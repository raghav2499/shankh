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
public class PantImplService implements OutfitTypeService {

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

        if (measurementDetails.getBottomWaist() != null) {
            measurementValue.put(BOTTOM_WAIST_MEASUREMENT_KEY, measurementDetails.getBottomWaist() * multiplyingFactor);
        }
        if (measurementDetails.getBottomSeat() != null) {
            measurementValue.put(BOTTOM_SEAT_MEASUREMENT_KEY, measurementDetails.getBottomSeat() * multiplyingFactor);
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
        measurementsDAO.setMeasurementValue(measurementValue);
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue =
                objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);

        outfitMeasurementDetails.setBottomWaist(measurementValue.get(BOTTOM_WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottomSeat(measurementValue.get(BOTTOM_SEAT_MEASUREMENT_KEY));
        outfitMeasurementDetails.setCalf(measurementValue.get(CALF_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBottom(measurementValue.get(BOTTOM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setPantLength(measurementValue.get(PANT_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setFly(measurementValue.get(FLY_MEASUREMENT_KEY));

        return outfitMeasurementDetails;
    }

    @Override
    public boolean haveMandatoryParams(MeasurementRequest measurementDetails) {
        return measurementDetails.getBottomWaist() != null &&
                measurementDetails.getBottomSeat() != null &&
                measurementDetails.getCalf() != null &&
                measurementDetails.getBottom() != null &&
                measurementDetails.getPantLength() != null &&
                measurementDetails.getFly() != null;
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
                addWaist(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(BOTTOM_WAIST_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addSeat(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(BOTTOM_SEAT_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addCalf(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(CALF_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addBottom(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(BOTTOM_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addPantLength(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(PANT_LENGTH_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(
                addFly(CommonUtils.doubleToString(
                        Optional.ofNullable(measurementValue.get(FLY_MEASUREMENT_KEY)).orElse(defaultValue) / dividingFactor)));

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
                outfitType.getImageLink(), 1);
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

    private MeasurementDetails addCalf(String value) {
        String imageLink = PANTS_CALF_IMAGE_LINK;
        String title = PANTS_CALF_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBottom(String value) {
        String imageLink = PANTS_BOTTOM_IMAGE_LINK;
        String title = PANTS_BOTTOM_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addPantLength(String value) {
        String imageLink = PANTS_LENGTH_IMAGE_LINK;
        String title = PANTS_LENGTH_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFly(String value) {
        String imageLink = PANTS_FLY_IMAGE_LINK;
        String title = PANTS_FLY_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }
}
