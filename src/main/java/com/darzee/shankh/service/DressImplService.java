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
public class DressImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        measurementDAO.setWaist(measurementDetails.getWaist()*multiplyingFactor);
        measurementDAO.setCalf(measurementDetails.getCalf()*multiplyingFactor);
        measurementDAO.setSeat(measurementDetails.getSeat()*multiplyingFactor);
        measurementDAO.setAnkle(measurementDetails.getAnkle()*multiplyingFactor);
        measurementDAO.setLength(measurementDetails.getLength()*multiplyingFactor);
    }

    @Override
    public boolean haveMandatoryParams(Measurements measurementDetails) {
        return measurementDetails.getWaist() != null &&
                measurementDetails.getCalf() != null &&
                measurementDetails.getSeat() != null &&
                measurementDetails.getAnkle() != null &&
                measurementDetails.getLength() != null;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale, String view) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        measurementDetailsResponseList.add(addWaist(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSeat(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSeat()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addCalf(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getCalf()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addAnkle(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getAnkle()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getLength()).orElse(defaultValue)/dividingFactor)));

        overallMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        overallMeasurementDetails.setOutfitImageLink(DRESS_OUTFIT_IMAGE_LINK);
        overallMeasurementDetails.setOutfitTypeHeading(DRESS_OUTFIT_TYPE_HEADING);
        return overallMeasurementDetails;
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = DRESS_WAIST_IMAGE_LINK;
        String title = DRESS_WAIST_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeat(String value) {
        String imageLink = DRESS_SEAT_IMAGE_LINK;
        String title = DRESS_SEAT_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addCalf(String value) {
        String imageLink = DRESS_CALF_IMAGE_LINK;
        String title = DRESS_CALF_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addAnkle(String value) {
        String imageLink = DRESS_ANKLE_IMAGE_LINK;
        String title = DRESS_ANKLE_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }
    private MeasurementDetails addLength(String value) {
        String imageLink = DRESS_LENGTH_IMAGE_LINK;
        String title = DRESS_LENGTH_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

}
