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
public class SareeBlouseImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        measurementDAO.setBlouseLength(measurementDetails.getBlouseLength()*multiplyingFactor);
        measurementDAO.setBust(measurementDetails.getBust()*multiplyingFactor);
        measurementDAO.setUpperChest(measurementDetails.getUpperChest()*multiplyingFactor);
        measurementDAO.setBelowBust(measurementDetails.getBelowBust()*multiplyingFactor);
        measurementDAO.setShoulder(measurementDetails.getShoulder()*multiplyingFactor);
        measurementDAO.setArmHole(measurementDetails.getShoulder()*multiplyingFactor);
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength()*multiplyingFactor);
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference()*multiplyingFactor);
        measurementDAO.setFrontNeckDepth(measurementDetails.getFrontNeckDepth()*multiplyingFactor);
        measurementDAO.setShoulderToApexLength(measurementDetails.getShoulderToApexLength()*multiplyingFactor);
        measurementDAO.setApexToApexLength(measurementDetails.getApexToApexLength()*multiplyingFactor);
        measurementDAO.setBackNeckDepth(measurementDetails.getBackNeckDepth()*multiplyingFactor);
        }

    @Override
    public boolean haveMandatoryParams(Measurements measurementDetails) {
        return measurementDetails.getBlouseLength() != null && measurementDetails.getBust() != null &&
                measurementDetails.getUpperChest() != null && measurementDetails.getBelowBust() != null &&
                measurementDetails.getShoulder() != null && measurementDetails.getArmHole() != null &&
                measurementDetails.getSleeveLength() != null && measurementDetails.getSleeveCircumference() != null &&
                measurementDetails.getFrontNeckDepth() != null && measurementDetails.getShoulderToApexLength() != null &&
                measurementDetails.getApexToApexLength() != null && measurementDetails.getBackNeckDepth() != null;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale, String view) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;

        measurementDetailsResponseList.add(addBlouseLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBlouseLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addBust(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBust()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addUpperChest(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getUpperChest()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addBelowBust(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBelowBust()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addShoulder(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShoulder()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addArmHole(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShoulder()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSleeveLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSleeveCircumference(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveCircumference()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addFrontNeckDepth(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getFrontNeckDepth()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addShoulderToApexLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShoulderToApexLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addApexToApexLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getApexToApexLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addBackNeckDepth(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBackNeckDepth()).orElse(defaultValue)/dividingFactor)));

        overallMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        overallMeasurementDetails.setOutfitImageLink(BLOUSE_OUTFIT_IMAGE_LINK);
        overallMeasurementDetails.setOutfitTypeHeading(SAREE_BLOUSE_OUTFIT_TYPE_HEADING);
        return overallMeasurementDetails;
    }

    private MeasurementDetails addBlouseLength(String value) {
        String imageLink = BLOUSE_LENGTH_IMAGE_LINK;
        String title = BLOUSE_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBust(String value) {
        String imageLink = BLOUSE_BUST_IMAGE_LINK;
        String title = BLOUSE_BUST_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addUpperChest(String value) {
        String imageLink = BLOUSE_UPPOER_CHEST_IMAGE_LINK;
        String title = BLOUSE_UPPER_CHEST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBelowBust(String value) {
        String imageLink = BLOUSE_BELOW_BUST_IMAGE_LINK;
        String title = BLOUSE_BELOW_BUST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addShoulder(String value) {
        String imageLink = BLOUSE_SHOULDER_IMAGE_LINK;
        String title = BLOUSE_SHOULDER_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addArmHole(String value) {
        String imageLink = BLOUSE_ARMHOLE_IMAGE_LINK;
        String title = BLOUSE_ARMHOLE_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = BLOUSE_SLEEVE_LENGTH_IMAGE_LINK;
        String title = BLOUSE_SLEEVE_LENGTH_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = BLOUSE_SLEEVE_CICUM_IMAGE_LINK;
        String title = BLOUSE_SLEEVE_CIRCUM_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addFrontNeckDepth(String value) {
        String imageLink = BLOUSE_FRONT_NECK_DEPTH_IMAGE_LINK;
        String title = BLOUSE_FRONT_NECK_DEPTH_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addShoulderToApexLength(String value) {
        String imageLink = BLOUSE_SHOULDER_TO_APEX_IMAGE_LINK;
        String title = BLOUSE_SHOULDER_TO_APEX_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addApexToApexLength(String value) {
        String imageLink = BLOUSE_APEX_TO_APEX_IMAGE_LINK;
        String title = BLOUSE_APEX_TO_APEX_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addBackNeckDepth(String value) {
        String imageLink = BLOUSE_BACK_NECK_DEPTH_IMAGE_LINK;
        String title = BLOUSE_BACK_NECK_DEPTH_TITLE;
        String index = "12";
        return new MeasurementDetails(imageLink, title, value, index);
    }
}
