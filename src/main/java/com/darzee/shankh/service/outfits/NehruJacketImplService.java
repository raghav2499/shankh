package com.darzee.shankh.service.outfits;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.*;
import com.darzee.shankh.service.OutfitImageLinkService;
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
public class NehruJacketImplService implements OutfitTypeService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Override
    public void setMeasurementDetailsInObject(MeasurementRequest measurementDetails,
                                              MeasurementsDAO measurementsDAO,
                                              MeasurementScale scale) {
        Double multiplyingFactor = MeasurementScale.INCH.equals(scale) ? Constants.INCH_TO_CM_MULTIPLYING_FACTOR : 1;
        Map<String, Double> measurementValue = measurementsDAO.getMeasurementValue();

        if (measurementValue == null) {
            measurementValue = new HashMap<>();
        }
        if (measurementDetails.getLength() != null) {
            measurementValue.put(LENGTH_MEASUREMENT_KEY, measurementDetails.getLength() * multiplyingFactor);
        }
        if (measurementDetails.getNeck() != null) {
            measurementValue.put(NECK_MEASUREMENT_KEY, measurementDetails.getNeck() * multiplyingFactor);
        }
        if (measurementDetails.getChest() != null) {
            measurementValue.put(CHEST_MEASUREMENT_KEY, measurementDetails.getChest() * multiplyingFactor);
        }
        if (measurementDetails.getWaist() != null) {
            measurementValue.put(WAIST_MEASUREMENT_KEY, measurementDetails.getWaist() * multiplyingFactor);
        }
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue =
                (measurementsDAO != null && measurementsDAO.getMeasurementValue() != null)
                        ? objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class)
                        : new HashMap<>();

        outfitMeasurementDetails.setLength(measurementValue.get(LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setNeck(measurementValue.get(NECK_MEASUREMENT_KEY));
        outfitMeasurementDetails.setChest(measurementValue.get(CHEST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setWaist(measurementValue.get(WAIST_MEASUREMENT_KEY));

        return outfitMeasurementDetails;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementsDAO measurementsDAO,
                                                           MeasurementScale scale,
                                                           Boolean nonEmptyValuesOnly) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        if (measurementValue != null) {
            measurementDetailsResponseList.add(
                    addLength(measurementsDAO.getMeasurement(LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addNeck(measurementsDAO.getMeasurement(NECK_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addChest(measurementsDAO.getMeasurement(CHEST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addWaist(measurementsDAO.getMeasurement(WAIST_MEASUREMENT_KEY, dividingFactor)));
        }
        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }
        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(NEHRU_JACKET_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(NEHRU_JACKET_OUTFIT_TYPE_HEADING);
        overallMeasurementDetails.setInnerMeasurementDetails(Arrays.asList(innerMeasurementDetails));
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.NEHRU_JACKET;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 1);
    }

    private MeasurementDetails addLength(String value) {
        String imageLink = NEHRU_JACKET_LENGTH_IMAGE_LINK;
        String title = NEHRU_JACKET_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addNeck(String value) {
        String imageLink = NEHRU_JACKET_NECK_IMAGE_LINK;
        String title = NEHRU_JACKET_NECK_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addChest(String value) {
        String imageLink = NEHRU_JACKET_CHEST_IMAGE_LINK;
        String title = NEHRU_JACKET_CHEST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = NEHRU_JACKET_WAIST_IMAGE_LINK;
        String title = NEHRU_JACKET_WAIST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

}
