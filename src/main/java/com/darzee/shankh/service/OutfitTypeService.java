package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.request.MeasurementRequest;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.response.OutfitMeasurementDetails;
import com.darzee.shankh.response.OverallMeasurementDetails;

import java.util.Map;

public interface OutfitTypeService {
    public void setMeasurementDetailsInObject(MeasurementRequest measurementDetails,
                                              MeasurementsDAO measurementDAO,
                                              MeasurementScale scale);

    public OutfitMeasurementDetails extractMeasurementDetails(MeasurementsDAO measurementDAO);

    public OverallMeasurementDetails setMeasurementDetails(MeasurementsDAO measurementDAO,
                                                           MeasurementScale scale,
                                                           Boolean nonEmptyValuesOnly);

    public OutfitDetails getOutfitDetails();

    public Map<Integer, String> getSubOutfitMap();

    public String getSubOutfitName(Integer ordinal);
}
