package com.darzee.shankh.service.outfits;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.*;
import com.darzee.shankh.service.OutfitTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.darzee.shankh.constants.Constants.ImageLinks.*;
import static com.darzee.shankh.constants.Constants.MeasurementKeys.LENGTH_MEASUREMENT_KEY;
import static com.darzee.shankh.constants.Constants.MeasurementKeys.WAIST_MEASUREMENT_KEY;
import static com.darzee.shankh.constants.Constants.MeasurementTitles.*;

@Service
public class UnderSkirtImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void setMeasurementDetailsInObject(MeasurementRequest measurementDetails,
                                              MeasurementsDAO measurementsDAO,
                                              MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        Map<String, Double> measurementValue = measurementsDAO.getMeasurementValue();

        if (measurementValue == null) {
            measurementValue = new HashMap<>();
        }

        if (measurementDetails.getWaist() != null) {
            measurementValue.put(WAIST_MEASUREMENT_KEY, measurementDetails.getWaist() * multiplyingFactor);
        }
        if (measurementDetails.getLength() != null) {
            measurementValue.put(LENGTH_MEASUREMENT_KEY, measurementDetails.getLength() * multiplyingFactor);
        }

        measurementsDAO.setMeasurementValue(measurementValue);
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue =
                (measurementsDAO != null && measurementsDAO.getMeasurementValue() != null)
                        ? objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class)
                        : new HashMap<>();
        outfitMeasurementDetails.setWaist(measurementValue.get(WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setLength(measurementValue.get(LENGTH_MEASUREMENT_KEY));
        return outfitMeasurementDetails;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementsDAO measurementsDAO, MeasurementScale scale) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        if(measurementValue != null) {
            measurementDetailsResponseList.add(
                    addWaist(measurementsDAO.getMeasurement(WAIST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addLength(measurementsDAO.getMeasurement(LENGTH_MEASUREMENT_KEY, dividingFactor)));
        }
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
                outfitType.getImageLink(), 1);
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
