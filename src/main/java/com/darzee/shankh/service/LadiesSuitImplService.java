package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class LadiesSuitImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {
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
    public MeasurementDetails getMeasurementResponse(MeasurementDAO measurementDAO, MeasurementScale scale) {
        MeasurementDetails response = new MeasurementDetails();
        if(measurementDAO == null) {
            return response;
        }
        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
        response.setScale(scale.getScale());
        response.setKameezLength(CommonUtils.doubleToString(measurementDAO.getKameezLength()/dividingFactor));
        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()/dividingFactor));
        response.setUpperChest(CommonUtils.doubleToString(measurementDAO.getUpperChest()/dividingFactor));
        response.setBust(CommonUtils.doubleToString(measurementDAO.getBust()/dividingFactor));
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()/dividingFactor));
        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()/dividingFactor));
        response.setArmHole(CommonUtils.doubleToString(measurementDAO.getArmHole()/dividingFactor));
        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()/dividingFactor));
        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()/dividingFactor));
        response.setFrontNeckDepth(CommonUtils.doubleToString(measurementDAO.getFrontNeckDepth()/dividingFactor));
        response.setBackNeckDepth(CommonUtils.doubleToString(measurementDAO.getBackNeckDepth()/dividingFactor));
        response.setSalwarLength(CommonUtils.doubleToString(measurementDAO.getSalwarLength()/dividingFactor));
        response.setSalwarHip(CommonUtils.doubleToString(measurementDAO.getSalwarHip()/dividingFactor));
        response.setKnee(CommonUtils.doubleToString(measurementDAO.getKnee()/dividingFactor));
        response.setAnkle(CommonUtils.doubleToString(measurementDAO.getAnkle()/dividingFactor));
        return response;
    }

    @Override
    public boolean haveAllRequiredMeasurements(MeasurementDAO measurement) {
        return measurement.getKameezLength() != null && measurement.getShoulder() != null
                && measurement.getUpperChest() != null && measurement.getBust() != null
                && measurement.getWaist() != null && measurement.getSeat() != null && measurement.getArmHole() != null
                && measurement.getSleeveLength() != null && measurement.getSleeveCircumference() != null
                && measurement.getFrontNeckDepth() != null && measurement.getBackNeckDepth() != null
                && measurement.getSalwarLength() != null && measurement.getSalwarHip() != null
                && measurement.getKnee() != null && measurement.getAnkle() != null;

    }
}
