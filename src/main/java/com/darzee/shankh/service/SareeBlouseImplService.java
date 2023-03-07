package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.response.GetMeasurementResponse;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class SareeBlouseImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setBlouseLength(measurementDetails.getBlouseLength());
        measurementDAO.setUpperChest(measurementDetails.getUpperChest());
        measurementDAO.setBust(measurementDetails.getBust());
        measurementDAO.setBelowBust(measurementDetails.getBelowBust());
        measurementDAO.setShoulder(measurementDetails.getShoulder());
        measurementDAO.setArmHole(measurementDetails.getShoulder());
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength());
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference());
        measurementDAO.setFrontNeckDepth(measurementDetails.getFrontNeckDepth());
        measurementDAO.setBackNeckDepth(measurementDetails.getBackNeckDepth());
        measurementDAO.setShoulderToApexLength(measurementDetails.getShoulderToApexLength());
    }

    @Override
    public GetMeasurementResponse getMeasurementResponse(MeasurementDAO measurementDAO) {
        GetMeasurementResponse response = new GetMeasurementResponse();
        if(measurementDAO == null) {
            return response;
        }
        response.setBlouseLength(CommonUtils.doubleToString(measurementDAO.getBlouseLength()));
        response.setUpperChest(CommonUtils.doubleToString(measurementDAO.getUpperChest()));
        response.setBust(CommonUtils.doubleToString(measurementDAO.getBust()));
        response.setBelowBust(CommonUtils.doubleToString(measurementDAO.getBelowBust()));
        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()));
        response.setArmHole(CommonUtils.doubleToString(measurementDAO.getShoulder()));
        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()));
        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()));
        response.setFrontNeckDepth(CommonUtils.doubleToString(measurementDAO.getFrontNeckDepth()));
        response.setBackNeckDepth(CommonUtils.doubleToString(measurementDAO.getBackNeckDepth()));
        response.setShoulderToApexLength(CommonUtils.doubleToString(measurementDAO.getShoulderToApexLength()));
        return response;
    }
}
