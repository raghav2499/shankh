package com.darzee.shankh.service.outfits;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.MeasurementDetails;
import com.darzee.shankh.response.OutfitDetails;
import com.darzee.shankh.service.OutfitImageLinkService;
import com.darzee.shankh.service.OutfitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.ImageLinks.*;
import static com.darzee.shankh.constants.MeasurementTitles.*;

@Service
public class DressImplService implements OutfitTypeService {
    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Override
    public OutfitDetails getOutfitDetails() {
        OutfitType outfitType = OutfitType.DRESS;
        return new OutfitDetails(outfitType.getOrdinal(), outfitType.getName(), outfitType.getDisplayString(),
                outfitImageLinkService.getOutfitImageLink(outfitType), 1, isPortfolioEligible());
    }

    @Override
    public Map<Integer, String> getSubOutfitMap() {
        Map<Integer, String> subOutfitMap = new HashMap<>();
        subOutfitMap.put(1, "Short Dress");
        subOutfitMap.put(2, "Long Dress");
        subOutfitMap.put(3, "Maxi Dress");
        return subOutfitMap;
    }

    public String getSubOutfitName(Integer ordinal) {
        return getSubOutfitMap().get(ordinal);
    }

    private MeasurementDetails addLength(String value) {
        String imageLink = DRESS_LENGTH_IMAGE_LINK;
        String title = DRESS_LENGTH_TITLE;
        String index = "1";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addShoulder(String value) {
        String imageLink = DRESS_SHOULDER_IMAGE_LINK;
        String title = DRESS_SHOULDER_TITLE;
        String index = "2";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addUpperChest(String value) {
        String imageLink = DRESS_UPPER_CHEST_IMAGE_LINK;
        String title = DRESS_UPPER_CHEST_TITLE;
        String index = "3";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBust(String value) {
        String imageLink = DRESS_BUST_IMAGE_LINK;
        String title = DRESS_BUST_TITLE;
        String index = "4";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addWaist(String value) {
        String imageLink = DRESS_WAIST_IMAGE_LINK;
        String title = DRESS_WAIST_TITLE;
        String index = "5";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSeat(String value) {
        String imageLink = DRESS_SEAT_IMAGE_LINK;
        String title = DRESS_SEAT_TITLE;
        String index = "6";
        return new MeasurementDetails(imageLink, title, value, index);
    }


    private MeasurementDetails addArmHole(String value) {
        String imageLink = DRESS_ARMHOLE_IMAGE_LINK;
        String title = DRESS_ARMHOLE_TITLE;
        String index = "7";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveLength(String value) {
        String imageLink = DRESS_SLEEVE_LENGTH_IMAGE_LINK;
        String title = DRESS_SLEEVE_LENGTH_TITLE;
        String index = "8";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBicep(String value) {
        String imageLink = DRESS_BICEP_IMAGE_LINK;
        String title = DRESS_BICEP_TITLE;
        String index = "9";
        return new MeasurementDetails(imageLink, title, value, index);
    }


    private MeasurementDetails addElbowRound(String value) {
        String imageLink = DRESS_ELBOW_ROUND_IMAGE_LINK;
        String title = DRESS_ELBOW_ROUND_TITLE;
        String index = "10";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addSleeveCircum(String value) {
        String imageLink = DRESS_SLEEVE_CIRCUMFERENCE_IMAGE_LINK;
        String title = DRESS_SLEEVE_CIRCUM_TITLE;
        String index = "11";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addFrontNeckDepth(String value) {
        String imageLink = DRESS_FRONT_NECK_DEPTH_IMAGE_LINK;
        String title = DRESS_FRONT_NECK_DEPTH_TITLE;
        String index = "12";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addBackNeckDepth(String value) {
        String imageLink = DRESS_BACK_NECK_DEPTH_IMAGE_LINK;
        String title = DRESS_BACK_NECK_DEPTH_TITLE;
        String index = "13";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCrossFront(String value) {
        String imageLink = DRESS_CROSS_FRONT_IMAGE_LINK;
        String title = DRESS_CROSS_FRONT_TITLE;
        String index = "14";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addCrossBack(String value) {
        String imageLink = DRESS_CROSS_BACK_IMAGE_LINK;
        String title = DRESS_CROSS_BACK_TITLE;
        String index = "15";
        return new MeasurementDetails(imageLink, title, value, index);
    }

    private MeasurementDetails addDartPoint(String value) {
        String imageLink = DRESS_DART_POINT_IMAGE_LINK;
        String title = DRESS_DART_POINT_TITLE;
        String index = "16";
        return new MeasurementDetails(imageLink, title, value, index);
    }


    @Override
    public boolean isPortfolioEligible() {
        return true;
    }

    @Override
    public String getTopHeading() {
        return DRESS_OUTFIT_TYPE_HEADING;
    }

    @Override
    public String getBottomHeading() {
        return "";
    }
}
