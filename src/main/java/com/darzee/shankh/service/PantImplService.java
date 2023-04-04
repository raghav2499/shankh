package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class PantImplService implements OutfitTypeService{
    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setCalf(measurementDetails.getCalf());
        measurementDAO.setSeat(measurementDetails.getSeat());
        measurementDAO.setBottom(measurementDetails.getBottom());
        measurementDAO.setLength(measurementDetails.getLength());
        measurementDAO.setFly(measurementDetails.getFly());
    }

    @Override
    public MeasurementDetails getMeasurementResponse(MeasurementDAO measurementDAO, MeasurementScale scale) {
        MeasurementDetails response = new MeasurementDetails();
        if(measurementDAO == null) {
            return response;
        }
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
        response.setScale(scale.getScale());
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()/dividingFactor));
        response.setCalf(CommonUtils.doubleToString(measurementDAO.getCalf()/dividingFactor));
        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()/dividingFactor));
        response.setBottom(CommonUtils.doubleToString(measurementDAO.getBottom()/dividingFactor));
        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()/dividingFactor));
        response.setFly(CommonUtils.doubleToString(measurementDAO.getFly()/dividingFactor));
        return response;
    }

    @Override
    public boolean haveAllRequiredMeasurements(MeasurementDAO measurement) {
        return measurement.getWaist() != null && measurement.getCalf() != null && measurement.getSeat() != null
                && measurement.getBottom() != null && measurement.getLength() != null && measurement.getFly() != null;

    }
}
