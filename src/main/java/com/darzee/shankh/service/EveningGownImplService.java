package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
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
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setGownLength(measurementDetails.getGownLength());
        measurementDAO.setChest(measurementDetails.getChest());
        measurementDAO.setUpperChest(measurementDetails.getUpperChest());
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setShoulder(measurementDetails.getShoulder());
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength());
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference());
        measurementDAO.setFrontNeckDepth(measurementDetails.getFrontNeckDepth());
        measurementDAO.setBackNeckDepth(measurementDetails.getBackNeckDepth());
    }
    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale, String view) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
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
        overallMeasurementDetails.setOutfitType(OutfitType.EVENING_GOWN.name());
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
