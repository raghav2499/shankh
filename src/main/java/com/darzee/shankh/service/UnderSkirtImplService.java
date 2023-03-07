package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.response.GetMeasurementResponse;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class UnderSkirtImplService implements OutfitTypeService{
    @Override
    public void setMeasurementDetailsInObject(MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setLength(measurementDetails.getLength());
    }

    @Override
    public GetMeasurementResponse getMeasurementResponse(MeasurementDAO measurementDAO) {
        GetMeasurementResponse response = new GetMeasurementResponse();
        if(measurementDAO == null) {
            return response;
        }
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()));
        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()));
        return response;
    }
}
