package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.request.Measurements;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.response.OverallMeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.darzee.shankh.constants.Constants.DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
import static com.darzee.shankh.constants.Constants.ImageLinks.*;
import static com.darzee.shankh.constants.Constants.MeasurementTitles.*;

@Service
public class EveningGownImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        measurementDAO.setGownLength(measurementDetails.getGownLength()*multiplyingFactor);
        measurementDAO.setShoulder(measurementDetails.getShoulder()*multiplyingFactor);
        measurementDAO.setUpperChest(measurementDetails.getUpperChest()*multiplyingFactor);
        measurementDAO.setBust(measurementDetails.getBust()*multiplyingFactor);
        measurementDAO.setWaist(measurementDetails.getWaist()*multiplyingFactor);
        measurementDAO.setArmHole(measurementDetails.getArmHole()*multiplyingFactor);
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength()*multiplyingFactor);
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference()*multiplyingFactor);
        measurementDAO.setFrontNeckDepth(measurementDetails.getFrontNeckDepth()*multiplyingFactor);
        measurementDAO.setBackNeckDepth(measurementDetails.getBackNeckDepth()*multiplyingFactor);
    }

    @Override
    public boolean haveMandatoryParams(Measurements measurementDetails) {
        return measurementDetails.getShoulder() != null &&
                measurementDetails.getUpperChest() != null &&
                measurementDetails.getBust() != null &&
                measurementDetails.getWaist() != null &&
                measurementDetails.getArmHole() != null &&
                measurementDetails.getSleeveLength() != null &&
                measurementDetails.getSleeveCircumference() != null &&
                measurementDetails.getFrontNeckDepth() != null &&
                measurementDetails.getBackNeckDepth() != null;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale, String view) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;


        measurementDetailsResponseList.add(addGownLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getGownLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addShoulder(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShoulder()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addUpperChest(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getUpperChest()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addBust(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBackNeckDepth()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addWaist(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addArmHole(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBackNeckDepth()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSleeveLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSleeveCircumference(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveCircumference()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addFrontNeckDepth(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getFrontNeckDepth()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addBackNeckDepth(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBackNeckDepth()).orElse(defaultValue)/dividingFactor)));


        overallMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        overallMeasurementDetails.setOutfitImageLink(GOWN_OUTFIT_IMAGE_LINK);
        overallMeasurementDetails.setOutfitTypeHeading(NIGHT_GOWN_OUTFIT_TYPE_HEADING);
        return overallMeasurementDetails;
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

    private MeasurementDetails addArmHole(String value) {
        String imageLink = GOWN_ARMHOLE_IMAGE_LINK;
        String title = GOWN_ARMHOLE_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = GOWN_SLEEVE_LENGTH_IMAGE_LINK;
        String title = GOWN_SLEEVE_LENGTH_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = GOWN_SLEEVE_CIRCUMFERENCE_IMAGE_LINK;
        String title = GOWN_SLEEVE_CIRCUM_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addFrontNeckDepth(String value) {
        String imageLink = GOWN_FRONT_NECK_DEPTH_IMAGE_LINK;
        String title = GOWN_FRONT_NECK_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addBackNeckDepth(String value) {
        String imageLink = GOWN_BACK_NECK_DEPTH_IMAGE_LINK;
        String title = GOWN_BACK_NECK_DEPTH_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

}
