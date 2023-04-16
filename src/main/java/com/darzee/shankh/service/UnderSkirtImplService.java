package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
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
public class UnderSkirtImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        measurementDAO.setWaist(measurementDetails.getWaist()*multiplyingFactor);
        measurementDAO.setLength(measurementDetails.getLength()*multiplyingFactor);
    }

    @Override
    public boolean haveMandatoryParams(Measurements measurementDetails) {
        return measurementDetails.getWaist() != null && measurementDetails.getLength() != null;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale, String view) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        Double defaultValue = DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE;

        measurementDetailsResponseList.add(addWaist(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getWaist()).orElse(defaultValue)/dividingFactor)));
        measurementDetailsResponseList.add(addLength(CommonUtils.doubleToString(Optional.ofNullable(measurementDAO.getLength()).orElse(defaultValue)/dividingFactor)));

        overallMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        overallMeasurementDetails.setOutfitImageLink(UNDER_SKIRT_OUTFIT_IMAGE_LINK);
        overallMeasurementDetails.setOutfitTypeHeading(UNDER_SHIRT_OUTFIT_TYPE_HEADING);
        return overallMeasurementDetails;
    }
    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.UNDER_SKIRT;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), Constants.OutfitType.UNDER_SKIRT_TITLE,
                Constants.OutfitType.UNDER_SKIRT_TITLE, 1);
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
