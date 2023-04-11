package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.request.Measurements;
import com.darzee.shankh.response.OverallMeasurementDetails;

public interface OutfitTypeService {
    public void setMeasurementDetailsInObject(Measurements measurementDetails, MeasurementDAO measurementDAO, MeasurementScale scale);

    public boolean haveMandatoryParams(Measurements measurementDetails);
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale, String view);
}
