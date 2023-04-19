package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.MeasurementView;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.request.Measurements;
import com.darzee.shankh.response.InnerMeasurementDetails;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.response.OverallMeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.darzee.shankh.constants.Constants.DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
import static com.darzee.shankh.constants.Constants.ImageLinks.*;
import static com.darzee.shankh.constants.Constants.MeasurementTitles.*;

@Service
public class LadiesSuitImplService implements OutfitTypeService {
    @Autowired
    private DaoEntityMapper mapper;

    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        if (measurementDetails.getKameezLength() != null) {
            measurementDAO.setKameezLength(measurementDetails.getKameezLength() * multiplyingFactor);
        }
        if (measurementDetails.getShoulder() != null) {
            measurementDAO.setShoulder(measurementDetails.getShoulder() * multiplyingFactor);
        }
        if (measurementDetails.getUpperChest() != null) {
            measurementDAO.setUpperChest(measurementDetails.getUpperChest() * multiplyingFactor);
        }
        if (measurementDetails.getBust() != null) {
            measurementDAO.setBust(measurementDetails.getBust() * multiplyingFactor);
        }
        if (measurementDetails.getWaist() != null) {
            measurementDAO.setWaist(measurementDetails.getWaist() * multiplyingFactor);
        }
        if (measurementDetails.getSeat() != null) {
            measurementDAO.setSeat(measurementDetails.getSeat() * multiplyingFactor);
        }
        if (measurementDetails.getArmHole() != null) {
            measurementDAO.setArmHole(measurementDetails.getArmHole() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveLength() != null) {
            measurementDAO.setSleeveLength(measurementDetails.getSleeveLength() * multiplyingFactor);
        }
        if (measurementDetails.getSleeveCircumference() != null) {
            measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference() * multiplyingFactor);
        }
        if (measurementDetails.getFrontNeckDepth() != null) {
            measurementDAO.setFrontNeckDepth(measurementDetails.getFrontNeckDepth() * multiplyingFactor);
        }
        if (measurementDetails.getBackNeckDepth() != null) {
            measurementDAO.setBackNeckDepth(measurementDetails.getBackNeckDepth() * multiplyingFactor);
        }
        if (measurementDetails.getSalwarHip() != null) {
            measurementDAO.setSalwarHip(measurementDetails.getSalwarHip() * multiplyingFactor);
        }
        if (measurementDetails.getKnee() != null) {
            measurementDAO.setKnee(measurementDetails.getKnee() * multiplyingFactor);
        }
        if (measurementDetails.getSalwarLength() != null) {
            measurementDAO.setSalwarLength(measurementDetails.getSalwarLength() * multiplyingFactor);
        }
        if (measurementDetails.getAnkle() != null) {
            measurementDAO.setAnkle(measurementDetails.getAnkle() * multiplyingFactor);
        }

    }

    @Override
    public boolean haveMandatoryParams(Measurements measurementDetails) {
        return measurementDetails.getKameezLength() != null &&
                measurementDetails.getShoulder() != null &&
                measurementDetails.getUpperChest() != null &&
                measurementDetails.getBust() != null &&
                measurementDetails.getWaist() != null &&
                measurementDetails.getSeat() != null &&
                measurementDetails.getArmHole() != null &&
                measurementDetails.getSleeveLength() != null &&
                measurementDetails.getSleeveCircumference() != null &&
                measurementDetails.getFrontNeckDepth() != null &&
                measurementDetails.getBackNeckDepth() != null &&
                measurementDetails.getSalwarHip() != null &&
                measurementDetails.getKnee() != null &&
                measurementDetails.getSalwarLength() != null &&
                measurementDetails.getAnkle() != null;
    }

    @Override
    public boolean areMandatoryParamsSet(MeasurementDAO measurementDAO) {
        Measurements measurements = mapper.measurementDaoToMeasurement(measurementDAO);
        return haveMandatoryParams(measurements);
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<InnerMeasurementDetails> innerMeasurementDetails = new ArrayList<>();
        innerMeasurementDetails.add(setMeasurementDetailsInObjectTop(measurementDAO, scale));
        innerMeasurementDetails.add(setMeasurementDetailsInObjectBottom(measurementDAO, scale));
        overallMeasurementDetails.setInnerMeasurementDetails(innerMeasurementDetails);
        return overallMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectTop(MeasurementDAO measurementDAO, MeasurementScale scale) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();

        measurementDetailsResponseList.add(addKameezLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getKameezLength()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addShoulder(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getShoulder()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addUpperChest(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getUpperChest()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addBust(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBust()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addWaist(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addSeat(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSeat()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addArmHole(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getArmHole()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addSleeveLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveLength()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addSleeveCircumference(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSleeveCircumference()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addFrontNeckDepth(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getFrontNeckDepth()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addBackNeckDepth(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getBackNeckDepth()).orElse(defaultValue) / dividingFactor)));

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(LADIES_SUIT_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(LADIES_SUIT_KAMEEZ_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectBottom(MeasurementDAO measurementDAO, MeasurementScale scale) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();

        measurementDetailsResponseList.add(addSalwarHip(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSalwarHip()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addKnee(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getKnee()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addSalwarLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSalwarLength()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addAnkle(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getAnkle()).orElse(defaultValue) / dividingFactor)));

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(LADIES_SUIT_LOWER_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(LADIES_SUIT_SALWAR_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.LADIES_SUIT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), Constants.OutfitType.LADIES_SUIT_TITLE,
                Constants.OutfitType.OUTFIT_TYPE_LADIES_SUIT_LINK, 2);
    }

    private MeasurementDetails addKameezLength(String value) {
        String imageLink = LADIES_SUIT_LENGTH_IMAGE_LINK;
        String title = LADIES_SUIT_KAMEEZ_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = LADIES_SUIT_SHOULDER_IMAGE_LINK;
        String title = LADIES_SUIT_SHOULDER_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addUpperChest(String value) {
        String imageLink = LADIES_SUIT_UPPER_CHEST_IMAGE_LINK;
        String title = LADIES_SUIT_UPPER_CHEST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBust(String value) {
        String imageLink = LADIES_SUIT_BUST_IMAGE_LINK;
        String title = LADIES_SUIT_BUST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = LADIES_SUIT_WAIST_IMAGE_LINK;
        String title = LADIES_SUIT_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeat(String value) {
        String imageLink = LADIES_SUIT_SEAT_IMAGE_LINK;
        String title = LADIES_SUIT_SEAT_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addArmHole(String value) {
        String imageLink = LADIES_SUIT_ARMHOLE_IMAGE_LINK;
        String title = LADIES_SUIT_ARMHOLE_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = LADIES_SUIT_SLEEVE_LENGTH_IMAGE_LINK;
        String title = LADIES_SUIT_SLEEVE_LENGTH_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircumference(String value) {
        String imageLink = LADIES_SUIT_SLEEVE_CIRCUM_IMAGE_LINK;
        String title = LADIES_SUIT_SLEEVE_CIRCUM_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFrontNeckDepth(String value) {
        String imageLink = LADIES_SUIT_FRONT_NECK_DEPTH_IMAGE_LINK;
        String title = LADIES_SUIT_FRONT_NECK_DEPTH_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBackNeckDepth(String value) {
        String imageLink = LADIES_SUIT_BACK_NECK_DEPTH_IMAGE_LINK;
        String title = LADIES_SUIT_BACK_NECK_DEPTH_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSalwarHip(String value) {
        String imageLink = LADIES_SUIT_SALWAR_HIP_IMAGE_LINK;
        String title = LADIES_SUIT_SALWAR_HIP_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addKnee(String value) {
        String imageLink = LADIES_SUIT_KNEE_IMAGE_LINK;
        String title = LADIES_SUIT_KNEE_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSalwarLength(String value) {
        String imageLink = LADIES_SUIT_SALWAR_LENGTH_IMAGE_LINK;
        String title = LADIES_SUIT_SALWAR_LENGTH_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addAnkle(String value) {
        String imageLink = LADIES_SUIT_ANKLE_IMAGE_LINK;
        String title = LADIES_SUIT_ANKLE_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

}
