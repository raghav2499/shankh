package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class SareeBlouseImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
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
    public MeasurementDetails getMeasurementResponse(MeasurementDAO measurementDAO, MeasurementScale scale) {
        MeasurementDetails response = new MeasurementDetails();
        if(measurementDAO == null) {
            return response;
        }
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
        response.setScale(scale.getScale());
        response.setBlouseLength(CommonUtils.doubleToString(measurementDAO.getBlouseLength()/dividingFactor));
        response.setUpperChest(CommonUtils.doubleToString(measurementDAO.getUpperChest()/dividingFactor));
        response.setBust(CommonUtils.doubleToString(measurementDAO.getBust()/dividingFactor));
        response.setBelowBust(CommonUtils.doubleToString(measurementDAO.getBelowBust()/dividingFactor));
        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()/dividingFactor));
        response.setArmHole(CommonUtils.doubleToString(measurementDAO.getShoulder()/dividingFactor));
        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()/dividingFactor));
        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()/dividingFactor));
        response.setFrontNeckDepth(CommonUtils.doubleToString(measurementDAO.getFrontNeckDepth()/dividingFactor));
        response.setBackNeckDepth(CommonUtils.doubleToString(measurementDAO.getBackNeckDepth()/dividingFactor));
        response.setShoulderToApexLength(CommonUtils.doubleToString(measurementDAO.getShoulderToApexLength()/dividingFactor));
        return response;
    }

    @Override
    public boolean haveAllRequiredMeasurements(MeasurementDAO measurement) {
        return measurement.getBlouseLength() != null && measurement.getUpperChest() != null
                && measurement.getBust() != null && measurement.getBelowBust() != null
                && measurement.getShoulder() != null && measurement.getArmHole() != null
                && measurement.getSleeveLength() != null && measurement.getSleeveCircumference() != null
                && measurement.getFrontNeckDepth() != null && measurement.getBackNeckDepth() != null
                && measurement.getShoulderToApexLength() != null;
    }
}
