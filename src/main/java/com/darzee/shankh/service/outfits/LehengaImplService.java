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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.darzee.shankh.constants.ImageLinks.*;
import static com.darzee.shankh.constants.MeasurementKeys.*;
import static com.darzee.shankh.constants.MeasurementTitles.*;

@Service
public class LehengaImplService implements OutfitTypeService {

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
        if (measurementDetails.getWaistCircum() != null) {
            measurementValue.put(WAIST_CIRCUM_MEASUREMENT_KEY, measurementDetails.getWaistCircum() * multiplyingFactor);
        }
        if (measurementDetails.getHipCircum() != null) {
            measurementValue.put(HIP_CIRCUM_MEASUREMENT_KEY, measurementDetails.getHipCircum() * multiplyingFactor);
        }
        if (measurementDetails.getWaistToKnee() != null) {
            measurementValue.put(WAIST_TO_KNEE_MEASUREMENT_KEY, measurementDetails.getWaistToKnee() * multiplyingFactor);
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
        outfitMeasurementDetails.setWaistCircum(measurementValue.get(WAIST_CIRCUM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setHipCircum(measurementValue.get(HIP_CIRCUM_MEASUREMENT_KEY));
        outfitMeasurementDetails.setWaistToKnee(measurementValue.get(WAIST_TO_KNEE_MEASUREMENT_KEY));
        outfitMeasurementDetails.setLength(measurementValue.get(LENGTH_MEASUREMENT_KEY));
        return outfitMeasurementDetails;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementsDAO measurementsDAO, MeasurementScale scale, Boolean nonEmptyValuesOnly) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        if (measurementValue != null) {
            measurementDetailsResponseList.add(
                        addWaistCircum(measurementsDAO.getMeasurement(WAIST_CIRCUM_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addHipCircum(measurementsDAO.getMeasurement(HIP_CIRCUM_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addWaistToKnee(measurementsDAO.getMeasurement(WAIST_TO_KNEE_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addLength(measurementsDAO.getMeasurement(LENGTH_MEASUREMENT_KEY, dividingFactor)));
        }
        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(LEHENGA_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(LEHENGA_OUTFIT_TYPE_HEADING);
        overallMeasurementDetails.setInnerMeasurementDetails(Arrays.asList(innerMeasurementDetails));
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.LEHENGA;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitType.getImageLink(), 1);
    }

    private MeasurementDetails addWaistCircum(String value) {
        String imageLink = LEHENGA_WAIST_CIRCUM_OUTFIT_IMAGE_LINK;
        String title = LEHENGA_WAIST_CIRCUM_OUTFIT_TYPE_HEADING;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addHipCircum(String value) {
        String imageLink = LEHENGA_HIP_CIRCUM_IMAGE_LINK;
        String title = LEHENGA_HIP_CIRCUM_OUTFIT_TYPE_HEADING;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaistToKnee(String value) {
        String imageLink = LEHENGA_WAIST_TO_KNEE_IMAGE_LINK;
        String title = LEHENGA_WAIST_TO_KNEE_OUTFIT_TYPE_HEADING;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addLength(String value) {
        String imageLink = LEHENGA_LENGTH_IMAGE_LINK;
        String title = LEHENGA_LENGTH_OUTFIT_TYPE_HEADING;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }
}
