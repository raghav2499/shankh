package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.response.GetMeasurementResponse;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class EveningGownImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setGownLength(measurementDetails.getGownLength());
        measurementDAO.setChest(measurementDetails.getChest());
        measurementDAO.setUpperChest(measurementDetails.getUpperChest());
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setShoulder(measurementDetails.getShoulder());
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength());
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference());
        measurementDAO.setFrontNeckDepth(measurementDetails.getFrontNeckDepth());
        measurementDAO.setBackNeckDepth(measurementDetails.getBackNeckDepth());
    }

    @Override
    public GetMeasurementResponse getMeasurementResponse(MeasurementDAO measurementDAO) {
        GetMeasurementResponse response = new GetMeasurementResponse();
        if(measurementDAO == null) {
            return response;
        }
        response.setGownLength(CommonUtils.doubleToString(measurementDAO.getGownLength()));
        response.setChest(CommonUtils.doubleToString(measurementDAO.getChest()));
        response.setUpperChest(CommonUtils.doubleToString(measurementDAO.getUpperChest()));
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()));
        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()));
        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()));
        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()));
        response.setFrontNeckDepth(CommonUtils.doubleToString(measurementDAO.getFrontNeckDepth()));
        response.setBackNeckDepth(CommonUtils.doubleToString(measurementDAO.getBackNeckDepth()));
        return response;
    }
}
