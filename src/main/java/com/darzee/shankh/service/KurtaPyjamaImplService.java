package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.MeasurementView;
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
public class KurtaPyjamaImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {

        measurementDAO.setKurtaLength(measurementDetails.getKurtaLength());
        measurementDAO.setShoulder(measurementDetails.getShoulder());
        measurementDAO.setUpperChest(measurementDetails.getUpperChest());
        measurementDAO.setBust(measurementDetails.getBust());
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setSeat(measurementDetails.getSeat());
        measurementDAO.setArmHole(measurementDetails.getArmHole()) ;
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength());
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference());
        measurementDAO.setFrontNeckDepth(measurementDetails.getFrontNeckDepth());
        measurementDAO.setBackNeckDepth(measurementDetails.getBackNeckDepth());
        measurementDAO.setPyjamaLength(measurementDetails.getPyjamaLength()) ;
        measurementDAO.setPyjamaHip(measurementDetails.getPyjamaHip());
        measurementDAO.setKnee(measurementDetails.getKnee());
        measurementDAO.setAnkle(measurementDetails.getAnkle());
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
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();

        measurementDetailsResponseList.add(addKurtaLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getKurtaLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addShoulder(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShoulder()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addUpperChest(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getUpperChest()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addBust(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBust()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addWaist(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSeat(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSeat()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addArmHole(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getArmHole()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSleeveLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addSleeveCircumference(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveCircumference()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addFrontNeckDepth(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getFrontNeckDepth()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addBackNeckDepth(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBackNeckDepth()).orElse(defaultValue)/dividingFactor)));

        overallMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        overallMeasurementDetails.setOutfitImageLink(KURTA_OUTFIT_IMAGE_LINK);
        overallMeasurementDetails.setOutfitType(OutfitType.KURTA_PYJAMA.name());
        return overallMeasurementDetails;
    }

    private OverallMeasurementDetails setMeasurementDetailsInObjectBottom(MeasurementDAO measurementDAO, MeasurementScale scale) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();

        measurementDetailsResponseList.add(addPyjamaLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getPyjamaLength()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addPyjamaHip(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getPyjamaHip()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addKnee(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getKnee()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addAnkle(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getAnkle()).orElse(defaultValue)/dividingFactor)));

        overallMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        overallMeasurementDetails.setOutfitImageLink(PYJAMA_OUTFIT_IMAGE_LINK);
        overallMeasurementDetails.setOutfitType(OutfitType.KURTA_PYJAMA.name());
        return overallMeasurementDetails;
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


}
