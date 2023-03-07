package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementDAO;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.response.GetMeasurementResponse;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class KurtaPyjamaImplService implements OutfitTypeService {
    @Override
    public void setMeasurementDetailsInObject(MeasurementDetails measurementDetails, MeasurementDAO measurementDAO) {

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

    @Override
    public GetMeasurementResponse getMeasurementResponse(MeasurementDAO measurementDAO) {
        GetMeasurementResponse response = new GetMeasurementResponse();
        if(measurementDAO == null) {
            return response;
        }
        response.setKurtaLength(CommonUtils.doubleToString(measurementDAO.getKurtaLength()));
        response.setShoulder(CommonUtils.doubleToString(measurementDAO.getShoulder()));
        response.setUpperChest(CommonUtils.doubleToString(measurementDAO.getUpperChest()));
        response.setBust(CommonUtils.doubleToString(measurementDAO.getBust()));
        response.setWaist(CommonUtils.doubleToString(measurementDAO.getWaist()));
        response.setSeat(CommonUtils.doubleToString(measurementDAO.getSeat()));
        response.setArmHole(CommonUtils.doubleToString(measurementDAO.getArmHole()) );
        response.setSleeveLength(CommonUtils.doubleToString(measurementDAO.getSleeveLength()));
        response.setSleeveCircumference(CommonUtils.doubleToString(measurementDAO.getSleeveCircumference()));
        response.setFrontNeckDepth(CommonUtils.doubleToString(measurementDAO.getFrontNeckDepth()));
        response.setBackNeckDepth(CommonUtils.doubleToString(measurementDAO.getBackNeckDepth()));
        response.setPyjamaLength(CommonUtils.doubleToString(measurementDAO.getPyjamaLength()) );
        response.setPyjamaHip(CommonUtils.doubleToString(measurementDAO.getPyjamaHip()));
        response.setKnee(CommonUtils.doubleToString(measurementDAO.getKnee()));
        response.setAnkle(CommonUtils.doubleToString(measurementDAO.getAnkle()));
        response.setLength(CommonUtils.doubleToString(measurementDAO.getLength()));
        return response;
    }
}
