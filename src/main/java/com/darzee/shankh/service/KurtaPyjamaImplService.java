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
import java.util.List;
import java.util.Optional;

import static com.darzee.shankh.constants.Constants.DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
import static com.darzee.shankh.constants.Constants.ImageLinks.*;
import static com.darzee.shankh.constants.Constants.MeasurementTitles.*;

@Service
public class KurtaPyjamaImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        if (measurementDetails.getKurtaLength() != null) {
            measurementDAO.setKurtaLength(measurementDetails.getKurtaLength() * multiplyingFactor);
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
        if (measurementDetails.getPyjamaLength() != null) {
            measurementDAO.setPyjamaLength(measurementDetails.getPyjamaLength() * multiplyingFactor);
        }
        if (measurementDetails.getPyjamaHip() != null) {
            measurementDAO.setPyjamaHip(measurementDetails.getPyjamaHip() * multiplyingFactor);
        }
        if (measurementDetails.getKnee() != null) {
            measurementDAO.setKnee(measurementDetails.getKnee() * multiplyingFactor);
        }
        if (measurementDetails.getAnkle() != null) {
            measurementDAO.setAnkle(measurementDetails.getAnkle() * multiplyingFactor);
        }
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementDAO measurementDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();

        outfitMeasurementDetails.setKurtaLength(measurementDAO.getKurtaLength());
        outfitMeasurementDetails.setShoulder(measurementDAO.getShoulder());
        outfitMeasurementDetails.setUpperChest(measurementDAO.getUpperChest());
        outfitMeasurementDetails.setBust(measurementDAO.getBust());
        outfitMeasurementDetails.setWaist(measurementDAO.getWaist());
        outfitMeasurementDetails.setSeat(measurementDAO.getSeat());
        outfitMeasurementDetails.setArmHole(measurementDAO.getArmHole());
        outfitMeasurementDetails.setSleeveLength(measurementDAO.getSleeveLength());
        outfitMeasurementDetails.setSleeveCircumference(measurementDAO.getSleeveCircumference());
        outfitMeasurementDetails.setFrontNeckDepth(measurementDAO.getFrontNeckDepth());
        outfitMeasurementDetails.setBackNeckDepth(measurementDAO.getBackNeckDepth());
        outfitMeasurementDetails.setPyjamaLength(measurementDAO.getPyjamaLength());
        outfitMeasurementDetails.setPyjamaHip(measurementDAO.getPyjamaHip());
        outfitMeasurementDetails.setKnee(measurementDAO.getKnee());
        outfitMeasurementDetails.setAnkle(measurementDAO.getAnkle());

        return outfitMeasurementDetails;
    }

    @Override
    public boolean haveMandatoryParams(Measurements measurementDetails) {
        return measurementDetails.getKurtaLength() != null &&
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
                measurementDetails.getPyjamaLength() != null &&
                measurementDetails.getPyjamaHip() != null &&
                measurementDetails.getKnee() != null &&
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
        List<InnerMeasurementDetails>  innerMeasurementDetails = new ArrayList<>();
        innerMeasurementDetails.add(setMeasurementDetailsInObjectTop(measurementDAO, scale));
        innerMeasurementDetails.add(setMeasurementDetailsInObjectBottom(measurementDAO, scale));
        overallMeasurementDetails.setInnerMeasurementDetails(innerMeasurementDetails);
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.KURTA_PYJAMA;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                Constants.OutfitType.OUTFIT_TYPE_KURTA_PYJAMA_LINK, 2);
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectTop(MeasurementDAO measurementDAO, MeasurementScale scale) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();

        measurementDetailsResponseList.add(addKurtaLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getKurtaLength()).orElse(defaultValue) / dividingFactor)));
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
        innerMeasurementDetails.setOutfitImageLink(KURTA_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(MENS_KURTA_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectBottom(MeasurementDAO measurementDAO, MeasurementScale scale) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();

        measurementDetailsResponseList.add(addPyjamaLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getPyjamaLength()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addPyjamaHip(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getPyjamaHip()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addKnee(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getKnee()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addAnkle(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getAnkle()).orElse(defaultValue) / dividingFactor)));

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(PYJAMA_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(MENS_PYJAMA_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
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
