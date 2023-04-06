package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.response.OverallMeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class MensSuitImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
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

//    @Override
//    public MeasurementDetails getMeasurementResponse(MeasurementDAO measurementDAO, MeasurementScale scale) {
//        MeasurementDetails response = new MeasurementDetails();
//        if(measurementDAO == null) {
//            return response;
//        }
//        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
//        response.setScale(scale.getScale());
//        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()/dividingFactor));
//        response.setNeck(CommonUtils.doubleToString(measurementDAO.getNeck()/dividingFactor));
//        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()/dividingFactor));
//        response.setChest(CommonUtils.doubleToString(measurementDAO.getChest()/dividingFactor));
//        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()/dividingFactor));
//        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()/dividingFactor));
//        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()/dividingFactor));
//        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()/dividingFactor));
//        response.setKnee(CommonUtils.doubleToString(measurementDAO.getKnee()/dividingFactor));
//        response.setBottom(CommonUtils.doubleToString(measurementDAO.getBottom()/dividingFactor));
//        response.setFly(CommonUtils.doubleToString(measurementDAO.getFly()/dividingFactor));
//        return response;
//    }

    @Override
    public boolean haveAllRequiredMeasurements(MeasurementDAO measurement) {
        return measurement.getLength() != null && measurement.getNeck() != null && measurement.getShoulder() != null
                && measurement.getChest() != null && measurement.getWaist() != null && measurement.getSeat() != null
                && measurement.getSleeveLength() != null && measurement.getSleeveCircumference() != null
                && measurement.getKnee() != null && measurement.getBottom() != null && measurement.getFly() != null;

    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale) {
        return null;
    }
}
