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
public class DressImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        if (measurementDetails.getWaist() != null) {
            measurementDAO.setWaist(measurementDetails.getWaist() * multiplyingFactor);
        }
        if (measurementDetails.getCalf() != null) {
            measurementDAO.setCalf(measurementDetails.getCalf() * multiplyingFactor);
        }
        if (measurementDetails.getSeat() != null) {
            measurementDAO.setSeat(measurementDetails.getSeat() * multiplyingFactor);
        }
        if (measurementDetails.getAnkle() != null) {
            measurementDAO.setAnkle(measurementDetails.getAnkle() * multiplyingFactor);
        }
        if (measurementDetails.getLength() != null) {
            measurementDAO.setLength(measurementDetails.getLength() * multiplyingFactor);
        }

    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementDAO measurementDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        outfitMeasurementDetails.setWaist(measurementDAO.getWaist());
        outfitMeasurementDetails.setCalf(measurementDAO.getCalf());
        outfitMeasurementDetails.setSeat(measurementDAO.getSeat());
        outfitMeasurementDetails.setAnkle(measurementDAO.getAnkle());
        outfitMeasurementDetails.setLength(measurementDAO.getLength());
        return outfitMeasurementDetails;
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
    public boolean areMandatoryParamsSet(MeasurementDAO measurementDAO) {
        Measurements measurements = mapper.measurementDaoToMeasurement(measurementDAO);
        return haveMandatoryParams(measurements);
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;
        measurementDetailsResponseList.add(addWaist(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addSeat(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getSeat()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addCalf(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getCalf()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addAnkle(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getAnkle()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getLength()).orElse(defaultValue) / dividingFactor)));

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(DRESS_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(DRESS_OUTFIT_TYPE_HEADING);
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        overallMeasurementDetails.setInnerMeasurementDetails(Arrays.asList(innerMeasurementDetails));
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.DRESS;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitType.getImageLink(), 1);
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
