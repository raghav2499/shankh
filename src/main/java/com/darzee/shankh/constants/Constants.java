package com.darzee.shankh.constants;

import com.darzee.shankh.enums.OrderStatus;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final List<OrderStatus> activeOrderStatusList = Arrays.asList(OrderStatus.STITCHING_NOT_STARTED,
            OrderStatus.STITCHING_IN_PROGRESS,
            OrderStatus.ORDER_READY_FOR_TRIAL);

    public static final List<OrderStatus> closedOrderStatusList = Arrays.asList(OrderStatus.ORDER_COMPLETED);
    public static final Double CM_TO_INCH_FACTOR = 2.54;
    public static final Double DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE = 10d;

    public static final Integer MEASUREMENT_RESPONSE_TRUNCATION_LENGTH = 4;

    public class MeasurementTitles {
        public static final String DRESS_WAIST_TITLE = "Waist";
        public static final String DRESS_SEAT_TITLE = "Seat";
        public static final String DRESS_CALF_TITLE = "Calf";
        public static final String DRESS_ANKLE_TITLE = "Ankle";
        public static final String DRESS_LENGTH_TITLE = "Length";

        public static final String GOWN_LENGTH_TITLE = "Gown Length";
        public static final String GOWN_SHOULDER_TITLE = "Shoulder";
        public static final String GOWN_UPPER_CHEST_TITLE = "Upper Chest";
        public static final String GOWN_BUST_TITLE = "Bust";
        public static final String GOWN_WAIST_TITLE = "Waist";
        public static final String GOWN_SEAT_TITLE = "Seat";
        public static final String GOWN_ARMHOLE_TITLE = "Armhole";
        public static final String GOWN_SLEEVE_LENGTH_TITLE = "Sleeve Length";
        public static final String GOWN_SLEEVE_CIRCUM_TITLE = "Sleeve Circum";
        public static final String GOWN_FRONT_NECK_TITLE = "Front Neck Depth";
        public static final String GOWN_BACK_NECK_DEPTH_TITLE = "Back Neck Depth";


    }
    public class ImageLinks {
        public static final String DRESS_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress.svg";
        public static final String DRESS_ANKLE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress_ankle.svg";
        public static final String DRESS_CALF_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress_calf.svg";
        public static final String DRESS_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress_length.svg";
        public static final String DRESS_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress_seat.svg";
        public static final String DRESS_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress_waist.svg";
        public static final String GOWN_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/nightgown.svg";

        public static final String GOWN_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_length.svg";
        public static final String GOWN_BUST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_bust.svg";
        public static final String GOWN_ARMHOLE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_armhole.svg";
        public static final String GOWN_UPPER_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_upperchest.svg";
        public static final String GOWN_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_waist.svg";
        public static final String GOWN_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_waist.svg";
        public static final String GOWN_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_shoulder.svg";
        public static final String GOWN_SLEEVE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_slevelen.svg";
        public static final String GOWN_SLEEVE_CIRCUMFERENCE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_slevcircum.svg";
        public static final String GOWN_FRONT_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_frontneck.svg";
        public static final String GOWN_BACK_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_backneck.svg";


    }


}
