package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class ShirtImplService implements OutfitTypeService {

    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setLength(measurementDetails.getLength());
        measurementDAO.setNeck(measurementDetails.getNeck());
        measurementDAO.setShoulder(measurementDetails.getShoulder());
        measurementDAO.setChest(measurementDetails.getChest());
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength());
        measurementDAO.setSeat(measurementDetails.getSeat());
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference());
    }

    @Override
    public MeasurementDetails getMeasurementResponse(MeasurementDAO measurementDAO, MeasurementScale scale) {
        MeasurementDetails response = new MeasurementDetails();
        if(measurementDAO == null) {
            return response;
        }
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
        response.setScale(scale.getScale());
        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()/dividingFactor));
        response.setNeck(CommonUtils.doubleToString(measurementDAO.getNeck()/dividingFactor));
        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()/dividingFactor));
        response.setChest(CommonUtils.doubleToString(measurementDAO.getChest()/dividingFactor));
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()/dividingFactor));
        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()/dividingFactor));
        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()/dividingFactor));
        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()/dividingFactor));
        return response;
    }

    @Override
    public boolean haveAllRequiredMeasurements(MeasurementDAO measurement) {
        return measurement.getLength() != null && measurement.getNeck() != null && measurement.getShoulder() != null
                && measurement.getChest() != null && measurement.getWaist() != null
                && measurement.getSleeveLength() != null && measurement.getSeat() != null
                && measurement.getSleeveCircumference() != null;

    }
}
