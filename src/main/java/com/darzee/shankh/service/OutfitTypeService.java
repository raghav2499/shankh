package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.response.GetMeasurementResponse;

public interface OutfitTypeService {
    public void setMeasurementDetailsInObject(MeasurementDetails measurementDetails, MeasurementDAO measurementDAO);

    public GetMeasurementResponse getMeasurementResponse(MeasurementDAO measurementDAO);
}
