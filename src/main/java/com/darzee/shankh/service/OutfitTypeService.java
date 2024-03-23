package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementRevisionsDAO;
import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.response.OutfitMeasurementDetails;
import com.darzee.shankh.response.OverallMeasurementDetails;

import java.util.Map;

public interface OutfitTypeService {
    public MeasurementRevisionsDAO addMeasurementRevision(MeasurementRequest measurementDetails, Long customerId,
                                                          OutfitType outfitType, MeasurementScale scale);

    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementDAO);

    public OverallMeasurementDetails setMeasurementDetails(MeasurementRevisionsDAO revisionsDAO,
                                                           MeasurementScale scale,
                                                           Boolean nonEmptyValuesOnly);

    public OutfitDetails getOutfitDetails();

    public Map<Integer, String> getSubOutfitMap();

    public String getSubOutfitName(Integer ordinal);

    public boolean isPortfolioEligible();
}
