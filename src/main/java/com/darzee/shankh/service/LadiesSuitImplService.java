package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.response.GetMeasurementResponse;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class LadiesSuitImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setKameezLength(measurementDetails.getKameezLength());
        measurementDAO.setShoulder(measurementDetails.getShoulder());
        measurementDAO.setUpperChest(measurementDetails.getUpperChest());
        measurementDAO.setBust(measurementDetails.getBust());
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setSeat(measurementDetails.getSeat());
        measurementDAO.setArmHole(measurementDetails.getArmHole());
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength());
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference());
        measurementDAO.setFrontNeckDepth(measurementDetails.getFrontNeckDepth());
        measurementDAO.setBackNeckDepth(measurementDetails.getBackNeckDepth());
        measurementDAO.setSalwarLength(measurementDetails.getSalwarLength());
        measurementDAO.setSalwarHip(measurementDetails.getSalwarHip());
        measurementDAO.setKnee(measurementDetails.getKnee());
        measurementDAO.setAnkle(measurementDetails.getAnkle());
    }

    @Override
    public GetMeasurementResponse getMeasurementResponse(MeasurementDAO measurementDAO) {
        GetMeasurementResponse response = new GetMeasurementResponse();
        if(measurementDAO == null) {
            return response;
        }
        response.setKameezLength(CommonUtils.doubleToString(measurementDAO.getKameezLength()));
        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()));
        response.setUpperChest(CommonUtils.doubleToString(measurementDAO.getUpperChest()));
        response.setBust(CommonUtils.doubleToString(measurementDAO.getBust()));
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()));
        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()));
        response.setArmHole(CommonUtils.doubleToString(measurementDAO.getArmHole()));
        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()));
        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()));
        response.setFrontNeckDepth(CommonUtils.doubleToString(measurementDAO.getFrontNeckDepth()));
        response.setBackNeckDepth(CommonUtils.doubleToString(measurementDAO.getBackNeckDepth()));
        response.setSalwarLength(CommonUtils.doubleToString(measurementDAO.getSalwarLength()));
        response.setSalwarHip(CommonUtils.doubleToString(measurementDAO.getSalwarHip()));
        response.setKnee(CommonUtils.doubleToString(measurementDAO.getKnee()));
        response.setAnkle(CommonUtils.doubleToString(measurementDAO.getAnkle()));
        return response;
    }
}
