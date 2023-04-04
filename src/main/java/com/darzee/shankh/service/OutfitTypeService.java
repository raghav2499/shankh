package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;

public interface OutfitTypeService {
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO);

    public MeasurementDetails getMeasurementResponse(MeasurementDAO measurementDAO, MeasurementScale scale);

    public boolean haveAllRequiredMeasurements(MeasurementDAO measurementDAO);
}
