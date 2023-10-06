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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.darzee.shankh.constants.ImageLinks.*;
import static com.darzee.shankh.constants.MeasurementKeys.*;
import static com.darzee.shankh.constants.MeasurementTitles.*;

@Service
public class RidaImplService implements OutfitTypeService {

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
        if (measurementDetails.getAboveHead() != null) {
            measurementValue.put(ABOVE_HEAD_MEASUREMENT_KEY, measurementDetails.getAboveHead() * multiplyingFactor);
        }
        if (measurementDetails.getPardiShoulder() != null) {
            measurementValue.put(PARDI_SHOULDER_MEASUREMENT_KEY, measurementDetails.getPardiShoulder() * multiplyingFactor);
        }
        if (measurementDetails.getAroundShoulder() != null) {
            measurementValue.put(AROUND_SHOULDER_MEASUREMENT_KEY, measurementDetails.getAroundShoulder() * multiplyingFactor);
        }
        if (measurementDetails.getPardiLength() != null) {
            measurementValue.put(PARDI_LENGTH_MEASUREMENT_KEY, measurementDetails.getPardiLength() * multiplyingFactor);
        }
        if (measurementDetails.getPardiGher() != null) {
            measurementValue.put(PARDI_GHER_MEASUREMENT_KEY, measurementDetails.getPardiGher() * multiplyingFactor);
        }
        if (measurementDetails.getKas() != null) {
            measurementValue.put(KAS_MEASUREMENT_KEY, measurementDetails.getKas() * multiplyingFactor);
        }
        if (measurementDetails.getLengaShoulder() != null) {
            measurementValue.put(LENGA_SHOULDER_MEASUREMENT_KEY, measurementDetails.getLengaShoulder() * multiplyingFactor);
        }
        if (measurementDetails.getBust() != null) {
            measurementValue.put(BUST_MEASUREMENT_KEY, measurementDetails.getBust() * multiplyingFactor);
        }
        if (measurementDetails.getWaist() != null) {
            measurementValue.put(WAIST_MEASUREMENT_KEY, measurementDetails.getWaist() * multiplyingFactor);
        }
        if (measurementDetails.getHip() != null) {
            measurementValue.put(HIP_MEASUREMENT_KEY,
                    measurementDetails.getHip() * multiplyingFactor);
        }
        if (measurementDetails.getLengaLength() != null) {
            measurementValue.put(LENGA_LENGTH_MEASUREMENT_KEY,
                    measurementDetails.getLengaLength() * multiplyingFactor);
        }
        if (measurementDetails.getLengaGher() != null) {
            measurementValue.put(LENGA_GHER_MEASUREMENT_KEY,
                    measurementDetails.getLengaGher() * multiplyingFactor);
        }
    }

    @Override
    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementsDAO) {
        OutfitMeasurementDetails outfitMeasurementDetails = new OutfitMeasurementDetails();
        Map<String, Double> measurementValue =
                (measurementsDAO != null && measurementsDAO.getMeasurementValue() != null)
                        ? objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class)
                        : new HashMap<>();

        outfitMeasurementDetails.setAboveHead(measurementValue.get(ABOVE_HEAD_MEASUREMENT_KEY));
        outfitMeasurementDetails.setPardiShoulder(measurementValue.get(PARDI_SHOULDER_MEASUREMENT_KEY));
        outfitMeasurementDetails.setAroundShoulder(measurementValue.get(AROUND_SHOULDER_MEASUREMENT_KEY));
        outfitMeasurementDetails.setPardiLength(measurementValue.get(PARDI_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setPardiGher(measurementValue.get(PARDI_GHER_MEASUREMENT_KEY));
        outfitMeasurementDetails.setKas(measurementValue.get(KAS_MEASUREMENT_KEY));
        outfitMeasurementDetails.setLengaShoulder(measurementValue.get(LENGA_SHOULDER_MEASUREMENT_KEY));
        outfitMeasurementDetails.setBust(measurementValue.get(BUST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setWaist(measurementValue.get(WAIST_MEASUREMENT_KEY));
        outfitMeasurementDetails.setHip(measurementValue.get(HIP_MEASUREMENT_KEY));
        outfitMeasurementDetails.setLengaLength(measurementValue.get(LENGA_LENGTH_MEASUREMENT_KEY));
        outfitMeasurementDetails.setLengaGher(measurementValue.get(LENGA_GHER_MEASUREMENT_KEY));

        return outfitMeasurementDetails;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementsDAO measurementsDAO,
                                                           MeasurementScale scale,
                                                           Boolean nonEmptyValuesOnly) {
        OverallMeasurementDetails overallMeasurementDetails = new OverallMeasurementDetails();
        List<InnerMeasurementDetails> innerMeasurementDetails = new ArrayList<>();
        innerMeasurementDetails.add(setMeasurementDetailsInObjectTop(measurementsDAO, scale, nonEmptyValuesOnly));
        innerMeasurementDetails.add(setMeasurementDetailsInObjectBottom(measurementsDAO, scale, nonEmptyValuesOnly));
        overallMeasurementDetails.setInnerMeasurementDetails(innerMeasurementDetails);
        return overallMeasurementDetails;
    }

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.RIDA;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 2);
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectTop(MeasurementsDAO measurementsDAO,
                                                                     MeasurementScale scale,
                                                                     Boolean nonEmptyValuesOnly) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        if (measurementValue != null) {
            measurementDetailsResponseList.add(
                    addAboveHead(measurementsDAO.getMeasurement(ABOVE_HEAD_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addPardiShoulder(measurementsDAO.getMeasurement(PARDI_SHOULDER_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addAroundShoulder(measurementsDAO.getMeasurement(AROUND_SHOULDER_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addPardiLength(measurementsDAO.getMeasurement(PARDI_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addPardiGher(measurementsDAO.getMeasurement(PARDI_GHER_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addKas(measurementsDAO.getMeasurement(KAS_MEASUREMENT_KEY, dividingFactor)));
        }
        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }
        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(PARDI_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(RIDA_PARDI_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private InnerMeasurementDetails setMeasurementDetailsInObjectBottom(MeasurementsDAO measurementsDAO,
                                                                        MeasurementScale scale,
                                                                        Boolean nonEmptyValuesOnly) {
        InnerMeasurementDetails innerMeasurementDetails = new InnerMeasurementDetails();
        Map<String, Double> measurementValue = objectMapper.convertValue(measurementsDAO.getMeasurementValue(), Map.class);
        List<MeasurementDetails> measurementDetailsResponseList = new ArrayList<>();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_DIVIDING_FACTOR : 1;
        if (measurementValue != null) {
            measurementDetailsResponseList.add(
                    addLengaShoulder(measurementsDAO.getMeasurement(LENGA_SHOULDER_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addBust(measurementsDAO.getMeasurement(BUST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addWaist(measurementsDAO.getMeasurement(WAIST_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addHips(measurementsDAO.getMeasurement(HIP_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addLengaLength(measurementsDAO.getMeasurement(LENGA_LENGTH_MEASUREMENT_KEY, dividingFactor)));
            measurementDetailsResponseList.add(
                    addLengaGher(measurementsDAO.getMeasurement(LENGA_GHER_MEASUREMENT_KEY, dividingFactor)));
        }
        if (Boolean.TRUE.equals(nonEmptyValuesOnly)) {
            measurementDetailsResponseList = measurementDetailsResponseList
                    .stream()
                    .filter(measurement -> StringUtils.isNotEmpty(measurement.getValue()))
                    .collect(Collectors.toList());
        }

        innerMeasurementDetails.setMeasurementDetailsList(measurementDetailsResponseList);
        innerMeasurementDetails.setOutfitImageLink(LENGA_OUTFIT_IMAGE_LINK);
        innerMeasurementDetails.setOutfitTypeHeading(RIDA_LENGA_OUTFIT_TYPE_HEADING);
        return innerMeasurementDetails;
    }

    private MeasurementDetails addAboveHead(String value) {
        String imageLink = RIDA_ABOVE_HEAD_IMAGE_LINK;
        String title = RIDA_ABOVE_HEAD_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addPardiShoulder(String value) {
        String imageLink = RIDA_PARDI_SHOULDER_IMAGE_LINK;
        String title = RIDA_PARDI_SHOULDER_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addAroundShoulder(String value) {
        String imageLink = RIDA_AROUND_SHOULDER_IMAGE_LINK;
        String title = RIDA_AROUND_SHOULDER_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addPardiLength(String value) {
        String imageLink = RIDA_PARDI_LENGTH_IMAGE_LINK;
        String title = RIDA_PARDI_LENGTH_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addPardiGher(String value) {
        String imageLink = RIDA_PARDI_GHER_IMAGE_LINK;
        String title = RIDA_PARDI_GHER_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addKas(String value) {
        String imageLink = RIDA_KAS_IMAGE_LINK;
        String title = RIDA_KAS_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addLengaShoulder(String value) {
        String imageLink = RIDA_LENGA_SHOULDER_IMAGE_LINK;
        String title = RIDA_LENGA_SHOULDER_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBust(String value) {
        String imageLink = RIDA_BUST_IMAGE_LINK;
        String title = RIDA_BUST_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = RIDA_WAIST_IMAGE_LINK;
        String title = RIDA_WAIST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addHips(String value) {
        String imageLink = RIDA_HIPS_IMAGE_LINK;
        String title = RIDA_HIP_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addLengaLength(String value) {
        String imageLink = RIDA_LENGA_LENGTH_IMAGE_LINK;
        String title = RIDA_LENGA_LENGTH_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addLengaGher(String value) {
        String imageLink = RIDA_LENGA_GHER_IMAGE_LINK;
        String title = RIDA_LENGA_GHER_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(58, "Plain Rida");
        subOutfitMap.put(59, "Embroidered Rida");
        subOutfitMap.put(60, "Printed Rida");
        subOutfitMap.put(61, "Lace Border Rida");
        subOutfitMap.put(62, "Patchwork Rida");
        subOutfitMap.put(63, "Traditional Rida");
        subOutfitMap.put(64, "Formal Rida");
        subOutfitMap.put(65, "Eid Special Rida");
        return subOutfitMap;
    }
}
