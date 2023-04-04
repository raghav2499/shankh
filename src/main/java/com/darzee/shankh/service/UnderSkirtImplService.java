package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class UnderSkirtImplService implements OutfitTypeService{
    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setLength(measurementDetails.getLength());
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
        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()/dividingFactor));
        return response;
    }

    @Override
    public boolean haveAllRequiredMeasurements(MeasurementDAO measurement) {
        return measurement.getWaist() != null && measurement.getLength() != null;
    }
}
