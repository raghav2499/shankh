package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class DressImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setCalf(measurementDetails.getCalf());
        measurementDAO.setSeat(measurementDetails.getSeat());
        measurementDAO.setAnkle(measurementDetails.getAnkle());
        measurementDAO.setLength(measurementDetails.getLength());
    }

    @Override
    public MeasurementDetails getMeasurementResponse(MeasurementDAO measurementDAO, MeasurementScale scale) {
        MeasurementDetails response = new MeasurementDetails();
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
        if(measurementDAO == null) {
            return response;
        }
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()/dividingFactor));
        response.setCalf(CommonUtils.doubleToString(measurementDAO.getCalf()/dividingFactor));
        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()/dividingFactor));
        response.setAnkle(CommonUtils.doubleToString(measurementDAO.getAnkle()/dividingFactor));
        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()/dividingFactor));
        response.setScale(scale.getScale());
        return response;
    }

    @Override
    public boolean haveAllRequiredMeasurements(MeasurementDAO measurement) {
        return measurement.getWaist() != null && measurement.getCalf() != null && measurement.getSeat() != null
                && measurement.getAnkle() != null && measurement.getLength() != null;

    }
}
