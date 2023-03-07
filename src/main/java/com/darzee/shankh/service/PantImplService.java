package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.response.GetMeasurementResponse;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class PantImplService implements OutfitTypeService{
    @Override
    public void setMeasurementDetailsInObject(MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setCalf(measurementDetails.getCalf());
        measurementDAO.setSeat(measurementDetails.getSeat());
        measurementDAO.setBottom(measurementDetails.getBottom());
        measurementDAO.setLength(measurementDetails.getLength());
        measurementDAO.setFly(measurementDetails.getFly());
    }

    @Override
    public GetMeasurementResponse getMeasurementResponse(MeasurementDAO measurementDAO) {
        GetMeasurementResponse response = new GetMeasurementResponse();
        if(measurementDAO == null) {
            return response;
        }
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()));
        response.setCalf(CommonUtils.doubleToString(measurementDAO.getCalf()));
        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()));
        response.setBottom(CommonUtils.doubleToString(measurementDAO.getBottom()));
        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()));
        response.setFly(CommonUtils.doubleToString(measurementDAO.getFly()));
        return response;
    }
}
