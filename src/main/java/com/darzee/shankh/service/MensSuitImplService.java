package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.MeasurementView;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.request.Measurements;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.response.OutfitDetails;
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
public class MensSuitImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        measurementDAO.setShirtLength(measurementDetails.getShirtLength()*multiplyingFactor);
        measurementDAO.setNeck(measurementDetails.getNeck()*multiplyingFactor);
        measurementDAO.setShoulder(measurementDetails.getShoulder()*multiplyingFactor);
        measurementDAO.setChest(measurementDetails.getChest()*multiplyingFactor);
        measurementDAO.setWaist(measurementDetails.getWaist()*multiplyingFactor);
        measurementDAO.setSeat(measurementDetails.getSeat()*multiplyingFactor);
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength()*multiplyingFactor);
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference()*multiplyingFactor);
        measurementDAO.setCalf(measurementDetails.getCalf()*multiplyingFactor);
        measurementDAO.setBottom(measurementDetails.getBottom()*multiplyingFactor);
        measurementDAO.setPantLength(measurementDetails.getPantLength()*multiplyingFactor);
        measurementDAO.setFly(measurementDetails.getFly()*multiplyingFactor);
    }

    @Override
    public boolean haveMandatoryParams(Measurements measurementDetails) {
        return measurementDetails.getShirtLength() != null &&
                measurementDetails.getNeck() != null &&
                measurementDetails.getShoulder() != null &&
                measurementDetails.getChest() != null &&
                measurementDetails.getWaist() != null &&
                measurementDetails.getSeat() != null &&
                measurementDetails.getSleeveLength() != null &&
                measurementDetails.getSleeveCircumference() != null &&
                measurementDetails.getCalf() != null &&
                measurementDetails.getBottom() != null &&
                measurementDetails.getFly() != null &&
                measurementDetails.getPantLength() != null;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale, String viewString) {
        MeasurementView view = Optional.ofNullable(MeasurementView.getEnumMap().get(Optional.ofNullable(viewString)))
                .orElse(MeasurementView.TOP);
        OverallMeasurementDetails overallMeasurementDetails  = MeasurementView.TOP.equals(view)
                ? setMeasurementDetailsInObjectTop(measurementDAO, scale)
                : setMeasurementDetailsInObjectBottom(measurementDAO, scale);
        return overallMeasurementDetails;
    }

    private OverallMeasurementDetails setMeasurementDetailsInObjectTop(MeasurementDAO measurementDAO, MeasurementScale scale) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();

        measurementDetailsResponseList.add(addLengthUpper(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShirtLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addNeck(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getNeck()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addShoulder(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShoulder()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addChest(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getChest()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addWaistUpper(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSeatUpper(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSeat()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSleeveLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSleeveCircumference(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveCircumference()).orElse(defaultValue)/dividingFactor)));

        overallMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        overallMeasurementDetails.setOutfitImageLink(MENS_SUIT_UPPER_OUTFIT_IMAGE_LINK);
        overallMeasurementDetails.setOutfitTypeHeading(MENS_SUIT_TOP_OUTFIT_TYPE_HEADING);
        return overallMeasurementDetails;
    }
    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.MENS_SUIT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), Constants.OutfitType.MENS_SUIT_TITLE,
                Constants.OutfitType.OUTFIT_TYPE_MENS_SUIT_LINK, 2);
    }

    private OverallMeasurementDetails setMeasurementDetailsInObjectBottom(MeasurementDAO measurementDAO, MeasurementScale scale) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();

        measurementDetailsResponseList.add(addWaistLower(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSeatLower(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSeat()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addCalf(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getCalf()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addBottom(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBottom()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addLengthLower(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getPantLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addFly(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getFly()).orElse(defaultValue)/dividingFactor)));

        overallMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        overallMeasurementDetails.setOutfitImageLink(MENS_SUIT_LOWER_OUTFIT_IMAGE_LINK);
        overallMeasurementDetails.setOutfitTypeHeading(MENS_SUIT_PANTS_OUTFIT_TYPE_HEADING);
        return overallMeasurementDetails;
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
}
