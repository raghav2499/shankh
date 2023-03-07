package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.response.GetMeasurementResponse;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class MensSuitImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setLength(measurementDetails.getLength());
        measurementDAO.setNeck(measurementDetails.getNeck());
        measurementDAO.setShoulder(measurementDetails.getShoulder());
        measurementDAO.setChest(measurementDetails.getChest());
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setSeat(measurementDetails.getSeat());
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength());
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference());
        measurementDAO.setKnee(measurementDetails.getKnee());
        measurementDAO.setBottom(measurementDetails.getBottom());
        measurementDAO.setFly(measurementDetails.getFly());
    }

    @Override
    public GetMeasurementResponse getMeasurementResponse(MeasurementDAO measurementDAO) {
        GetMeasurementResponse response = new GetMeasurementResponse();
        if(measurementDAO == null) {
            return response;
        }
        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()));
        response.setNeck(CommonUtils.doubleToString(measurementDAO.getNeck()));
        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()));
        response.setChest(CommonUtils.doubleToString(measurementDAO.getChest()));
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()));
        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()));
        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()));
        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()));
        response.setKnee(CommonUtils.doubleToString(measurementDAO.getKnee()));
        response.setBottom(CommonUtils.doubleToString(measurementDAO.getBottom()));
        response.setFly(CommonUtils.doubleToString(measurementDAO.getFly()));
        return response;
    }
}
