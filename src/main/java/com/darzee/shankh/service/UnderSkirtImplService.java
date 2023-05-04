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
public class UnderSkirtImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        if (measurementDetails.getWaist() != null) {
            measurementDAO.setWaist(measurementDetails.getWaist() * multiplyingFactor);
        }
        if (measurementDetails.getLength() != null) {
            measurementDAO.setLength(measurementDetails.getLength() * multiplyingFactor);
        }
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementDAO measurementDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        outfitMeasurementDetails.setWaist(measurementDAO.getWaist());
        outfitMeasurementDetails.setLength(measurementDAO.getLength());
        return outfitMeasurementDetails;
    }

    @Override
    public boolean haveMandatoryParams(Measurements measurementDetails) {
        return measurementDetails.getWaist() != null && measurementDetails.getLength() != null;
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

        measurementDetailsResponseList.add(addWaist(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue) / dividingFactor)));
        measurementDetailsResponseList.add(addLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getLength()).orElse(defaultValue) / dividingFactor)));

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(UNDER_SKIRT_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(UNDER_SHIRT_OUTFIT_TYPE_HEADING);
        overallMeasurementDetails.setInnerMeasurementDetails(Arrays.asList(innerMeasurementDetails));
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.UNDER_SKIRT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                Constants.OutfitType.OUTFIT_TYPE_UNDER_SKIRT_LINK, 1);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = UNDER_SKIRT_WAIST_IMAGE_LINK;
        String title = UNDER_SKIRT_WAIST_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addLength(String value) {
        String imageLink = UNDER_SKIRT_LENGTH_IMAGE_LINK;
        String title = UNDER_SKIRT_LENGTH_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

}
