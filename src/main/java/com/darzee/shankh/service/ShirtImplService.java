package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.request.Measurements;
import com.darzee.shankh.response.*;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.darzee.shankh.constants.Constants.DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
import static com.darzee.shankh.constants.Constants.ImageLinks.*;
import static com.darzee.shankh.constants.Constants.MeasurementTitles.*;

@Service
public class ShirtImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        if (measurementDetails.getShirtLength() != null) {
            measurementDAO.setShirtLength(measurementDetails.getShirtLength() * multiplyingFactor);
        }
        if (measurementDetails.getNeck() != null) {
            measurementDAO.setNeck(measurementDetails.getNeck() * multiplyingFactor);
        }
        if (measurementDetails.getShoulder() != null) {
            measurementDAO.setShoulder(measurementDetails.getShoulder() * multiplyingFactor);
        }
        if (measurementDetails.getChest() != null) {
            measurementDAO.setChest(measurementDetails.getChest() * multiplyingFactor);
        }
        if (measurementDetails.getWaist() != null) {
            measurementDAO.setWaist(measurementDetails.getWaist() * multiplyingFactor);
        }
        if (measurementDetails.getSeat() != null) {
            measurementDAO.setSeat(measurementDetails.getSeat() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveLength() != null) {
            measurementDAO.setSleeveLength(measurementDetails.getSleeveLength() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveCircumference() != null) {
            measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference() * multiplyingFactor);
        }
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementDAO measurementDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();

        outfitMeasurementDetails.setShirtLength(measurementDAO.getShirtLength());
        outfitMeasurementDetails.setNeck(measurementDAO.getNeck());
        outfitMeasurementDetails.setShoulder(measurementDAO.getShoulder());
        outfitMeasurementDetails.setChest(measurementDAO.getChest());
        outfitMeasurementDetails.setWaist(measurementDAO.getWaist());
        outfitMeasurementDetails.setSeat(measurementDAO.getSeat());
        outfitMeasurementDetails.setSleeveLength(measurementDAO.getSleeveLength());
        outfitMeasurementDetails.setSleeveCircumference(measurementDAO.getSleeveCircumference());

        return outfitMeasurementDetails;
    }

    @Override
    public boolean haveMandatoryParams(Measurements measurementDetails) {
        return measurementDetails.getShirtLength() != null && measurementDetails.getNeck() != null
                && measurementDetails.getShoulder() != null && measurementDetails.getChest() != null
                && measurementDetails.getWaist() != null && measurementDetails.getSeat() != null
                && measurementDetails.getSleeveLength() != null && measurementDetails.getSleeveCircumference() != null;
    }

    @Override
    public boolean areMandatoryParamsSet(MeasurementDAO measurementDAO) {
        Measurements measurements = mapper.measurementDaoToMeasurement(measurementDAO);
        return haveMandatoryParams(measurements);
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;

        measurementDetailsResponseList.add(addLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShirtLength()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addNeck(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getNeck()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addShoulder(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShoulder()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addChest(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getChest()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addWaist(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addSeat(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSeat()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addSleeveLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveLength()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addSleeveCircumference(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveCircumference()).orElse(defaultValue) / dividingFactor)));

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(SHIRT_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(SHIRT_OUTFIT_TYPE_HEADING);
        overallMeasurementDetails.setInnerMeasurementDetails(Arrays.asList(innerMeasurementDetails));
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.SHIRT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                Constants.OutfitType.OUTFIT_TYPE_SHIRT_LINK, 1);
    }

    private MeasurementDetails addLength(String value) {
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

    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = SHIRT_SLEEVE_CIRCUM_IMAGE_LINK;
        String title = SHIRT_SLEEVE_CIRCUM_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }
}
