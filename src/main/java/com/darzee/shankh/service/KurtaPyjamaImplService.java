package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.enums.MeasurementScale;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.response.OverallMeasurementDetails;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class KurtaPyjamaImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(com.darzee.shankh.request.MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {

        measurementDAO.setKurtaLength(measurementDetails.getKurtaLength());
        measurementDAO.setShoulder(measurementDetails.getShoulder());
        measurementDAO.setUpperChest(measurementDetails.getUpperChest());
        measurementDAO.setBust(measurementDetails.getBust());
        measurementDAO.setWaist(measurementDetails.getWaist());
        measurementDAO.setSeat(measurementDetails.getSeat());
        measurementDAO.setArmHole(measurementDetails.getArmHole()) ;
        measurementDAO.setSleeveLength(measurementDetails.getSleeveLength());
        measurementDAO.setSleeveCircumference(measurementDetails.getSleeveCircumference());
        measurementDAO.setFrontNeckDepth(measurementDetails.getFrontNeckDepth());
        measurementDAO.setBackNeckDepth(measurementDetails.getBackNeckDepth());
        measurementDAO.setPyjamaLength(measurementDetails.getPyjamaLength()) ;
        measurementDAO.setPyjamaHip(measurementDetails.getPyjamaHip());
        measurementDAO.setKnee(measurementDetails.getKnee());
        measurementDAO.setAnkle(measurementDetails.getAnkle());
    }

//    @Override
//    public MeasurementDetails getMeasurementResponse(MeasurementDAO measurementDAO, MeasurementScale scale) {
//        MeasurementDetails response = new MeasurementDetails();
//        Double dividingFactor = MeasurementScale.INCH.equals(scale) ? Constants.CM_TO_INCH_FACTOR : 1;
//        if(measurementDAO == null) {
//            return response;
//        }
//        response.setKurtaLength(CommonUtils.doubleToString(measurementDAO.getKurtaLength()/dividingFactor));
//        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()/dividingFactor));
//        response.setUpperChest(CommonUtils.doubleToString(measurementDAO.getUpperChest()/dividingFactor));
//        response.setBust(CommonUtils.doubleToString(measurementDAO.getBust()/dividingFactor));
//        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()/dividingFactor));
//        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()/dividingFactor));
//        response.setArmHole(CommonUtils.doubleToString(measurementDAO.getArmHole()) );
//        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()/dividingFactor));
//        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()/dividingFactor));
//        response.setFrontNeckDepth(CommonUtils.doubleToString(measurementDAO.getFrontNeckDepth()/dividingFactor));
//        response.setBackNeckDepth(CommonUtils.doubleToString(measurementDAO.getBackNeckDepth()/dividingFactor));
//        response.setPyjamaLength(CommonUtils.doubleToString(measurementDAO.getPyjamaLength()) );
//        response.setPyjamaHip(CommonUtils.doubleToString(measurementDAO.getPyjamaHip()/dividingFactor));
//        response.setKnee(CommonUtils.doubleToString(measurementDAO.getKnee()/dividingFactor));
//        response.setAnkle(CommonUtils.doubleToString(measurementDAO.getAnkle()/dividingFactor));
//        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()/dividingFactor));
//        response.setScale(scale.getScale());
//        return response;
//    }

    @Override
    public boolean haveAllRequiredMeasurements(MeasurementDAO measurementDAO) {
            return measurementDAO.getKurtaLength() != null && measurementDAO.getShoulder() != null
                    && measurementDAO.getUpperChest() != null && measurementDAO.getBust() != null
                    && measurementDAO.getWaist() != null && measurementDAO.getSeat() != null
                    && measurementDAO.getArmHole() != null && measurementDAO.getSleeveLength() != null
                    && measurementDAO.getSleeveCircumference() != null && measurementDAO.getFrontNeckDepth() != null
                    && measurementDAO.getBackNeckDepth() != null && measurementDAO.getPyjamaLength() != null
                    && measurementDAO.getPyjamaHip() != null && measurementDAO.getKnee() != null
                    && measurementDAO.getAnkle() != null;
    }

    @Override
    public OverallMeasurementDetails setMeasurementDetails(MeasurementDAO measurementDAO, MeasurementScale scale) {
        return null;
    }
}
