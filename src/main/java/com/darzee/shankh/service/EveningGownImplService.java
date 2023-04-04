package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class EveningGownImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
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
    public MeasurementDetails getMeasurementResponse(MeasurementDAO measurementDAO, MeasurementScale scale) {
        MeasurementDetails response = new MeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
        if(measurementDAO == null) {
            return response;
        }
        response.setGownLength(CommonUtils.doubleToString(measurementDAO.getGownLength()/dividingFactor));
        response.setChest(CommonUtils.doubleToString(measurementDAO.getChest()/dividingFactor));
        response.setUpperChest(CommonUtils.doubleToString(measurementDAO.getUpperChest()/dividingFactor));
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()/dividingFactor));
        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()/dividingFactor));
        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()/dividingFactor));
        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()/dividingFactor));
        response.setFrontNeckDepth(CommonUtils.doubleToString(measurementDAO.getFrontNeckDepth()/dividingFactor));
        response.setBackNeckDepth(CommonUtils.doubleToString(measurementDAO.getBackNeckDepth()/dividingFactor));
        response.setScale(scale.getScale());
        return response;
    }

    @Override
    public boolean haveAllRequiredMeasurements(MeasurementDAO measurement) {
        return measurement.getGownLength() != null && measurement.getChest() != null
                && measurement.getUpperChest() != null && measurement.getWaist() != null
                && measurement.getShoulder() != null && measurement.getSleeveLength() != null
                && measurement.getSleeveCircumference() != null && measurement.getFrontNeckDepth() != null
                && measurement.getBackNeckDepth() != null;

    }
}
