package com.darzee.shankh.constants;

import com.darzee.shankh.enums.OrderStatus;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final List<OrderStatus> ACTIVE_ORDER_STATUS_LIST = Arrays.asList(OrderStatus.STITCHING_IN_PROGRESS,
            OrderStatus.ORDER_READY_FOR_TRIAL,
            OrderStatus.ORDER_COMPLETED);

        public static final List<OrderStatus> CLOSED_ORDER_STATUS_LIST = Arrays.asList(OrderStatus.ORDER_DELIVERED);
    public static final Double CM_TO_INCH_DIVIDING_FACTOR = 2.54;
    public static final Double INCH_TO_CM_MULTIPLYING_FACTOR = 2.54;
    public static final Double DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE = 10d;

    public static final Integer MEASUREMENT_RESPONSE_TRUNCATION_LENGTH = 4;

    public class MeasurementTitles {
        public static final String DRESS_WAIST_TITLE = "Waist";
        public static final String DRESS_SEAT_TITLE = "Seat";
        public static final String DRESS_CALF_TITLE = "Calf";
        public static final String DRESS_ANKLE_TITLE = "Ankle";
        public static final String DRESS_LENGTH_TITLE = "Length";

        public static final String GOWN_LENGTH_TITLE = "Gown Length";

        public static final String GOWN_SEAT_TITLE = "Seat";
        public static final String GOWN_SHOULDER_TITLE = "Shoulder";
        public static final String GOWN_UPPER_CHEST_TITLE = "Upper Chest";
        public static final String GOWN_BUST_TITLE = "Bust";
        public static final String GOWN_WAIST_TITLE = "Waist";
        public static final String GOWN_ARMHOLE_TITLE = "Armhole";
        public static final String GOWN_SLEEVE_LENGTH_TITLE = "Sleeve Length";
        public static final String GOWN_SLEEVE_CIRCUM_TITLE = "Sleeve Circum";
        public static final String GOWN_FRONT_NECK_TITLE = "Front Neck Depth";
        public static final String GOWN_BACK_NECK_DEPTH_TITLE = "Back Neck Depth";

        public static final String UNDER_SKIRT_WAIST_TITLE = "Waist";

        public static final String UNDER_SKIRT_LENGTH_TITLE = "Length";

        public static final String BLOUSE_LENGTH_TITLE = "Blouse Length";
        public static final String BLOUSE_BUST_TITLE = "Bust";
        public static final String BLOUSE_UPPER_CHEST_TITLE = "Upper Chest";
        public static final String BLOUSE_BELOW_BUST_TITLE = "Below Bust";
        public static final String BLOUSE_SHOULDER_TITLE = "Shoulder";
        public static final String BLOUSE_ARMHOLE_TITLE = "Armhole";
        public static final String BLOUSE_SLEEVE_LENGTH_TITLE = "Sleeve Length";
        public static final String BLOUSE_SLEEVE_CIRCUM_TITLE = "Sleeve Circum";
        public static final String BLOUSE_FRONT_NECK_DEPTH_TITLE = "Front Neck Depth";
        public static final String BLOUSE_SHOULDER_TO_APEX_TITLE = "Shoulder to Apex";
        public static final String BLOUSE_APEX_TO_APEX_TITLE = "Apex to Apex";
        public static final String BLOUSE_BACK_NECK_DEPTH_TITLE = "Back Neck Depth";
        public static final String SHIRT_LENGTH_TITLE = "Length";
        public static final String SHIRT_NECK_TITLE = "Neck";
        public static final String SHIRT_SHOULDER_TITLE = "Shoulder";
        public static final String SHIRT_CHEST_TITLE = "Chest";
        public static final String SHIRT_WAIST_TITLE = "Waist";
        public static final String SHIRT_SEAT_TITLE = "Seat";
        public static final String SHIRT_SLEEVE_LENGTH_TITLE = "Sleeve";
        public static final String SHIRT_SLEEVE_CIRCUM_TITLE = "Sleeve Circum";

        public static final String PANTS_WAIST_TITLE = "Waist";

        public static final String PANTS_SEAT_TITLE = "Seat";

        public static final String PANTS_CALF_TITLE = "Calf/Knee";

        public static final String PANTS_BOTTOM_TITLE = "Bottom/Bells";

        public static final String PANTS_LENGTH_TITLE = "Length";

        public static final String PANTS_FLY_TITLE = "Fly(Ply)";
        public static final String KURTA_LENGTH_TITLE = "Kurta Length";
        public static final String KURTA_SHOULDER_TITLE = "Shoulder";
        public static final String KURTA_UPPER_CHEST_TITLE = "Upper Chest";
        public static final String KURTA_BUST_TITLE = "Bust";
        public static final String KURTA_WAIST_TITLE = "Waist";
        public static final String KURTA_SEAT_TITLE = "Seat";
        public static final String KURTA_ARMHOLE_TITLE = "Armhole";
        public static final String KURTA_SLEEVE_LENGTH_TITLE = "Sleeve Length";
        public static final String KURTA_SLEEVE_CIRCUM_TITLE = "Sleeve Circum";
        public static final String KURTA_FRONT_NECK_DEPTH_TITLE = "Front Neck Depth";
        public static final String KURTA_BACK_NECK_DEPTH_TITLE = "Back Neck Depth";
        public static final String PYJAMA_HIP_TITLE = "Pajama Hip";
        public static final String PYJAMA_KNEE_TITLE = "Knee";
        public static final String PYJAMA_ANKLE_TITLE = "Ankle";
        public static final String PYJAMA_LENGTH_TITLE = "Pajama Length";

        public static final String MENS_SUIT_UPPER_LENGTH_TITLE = "Length";
        public static final String MENS_SUIT_NECK_TITLE = "Neck";
        public static final String MENS_SUIT_SHOULDER_TITLE = "Shoulder";
        public static final String MENS_SUIT_CHEST_TITLE = "Chest";
        public static final String MENS_SUIT_UPPER_WAIST_TITLE = "Waist";
        public static final String MENS_SUIT_UPPER_SEAT_TITLE = "Seat";
        public static final String MENS_SUIT_SLEEVE_TITLE = "Sleeve";
        public static final String MENS_SUIT_SLEEVE_CIRCUM_TITLE = "Sleeve Circum";
        public static final String MENS_SUIT_LOWER_WAIST_TITLE = "Waist";
        public static final String MENS_SUIT_LOWER_SEAT_TITLE = "Seat";
        public static final String MENS_SUIT_CALF_TITLE = "Calf/Knee";
        public static final String MENS_SUIT_BOTTOM_TITLE = "Bottom/Bells";
        public static final String MENS_SUIT_LOWER_LENGTH_TITLE = "Length";
        public static final String MENS_SUIT_FLY_TITLE = "Fly(Ply)";
        public static final String LADIES_SUIT_KAMEEZ_LENGTH_TITLE = "Kameez Length";
        public static final String LADIES_SUIT_SHOULDER_TITLE = "Shoulder";
        public static final String LADIES_SUIT_UPPER_CHEST_TITLE = "Upper Chest";
        public static final String LADIES_SUIT_BUST_TITLE = "Bust";
        public static final String LADIES_SUIT_WAIST_TITLE = "Waist";
        public static final String LADIES_SUIT_SEAT_TITLE = "Seat";
        public static final String LADIES_SUIT_ARMHOLE_TITLE = "Armhole";
        public static final String LADIES_SUIT_SLEEVE_LENGTH_TITLE = "Sleeve Length";
        public static final String LADIES_SUIT_SLEEVE_CIRCUM_TITLE = "Sleeve Circum";
        public static final String LADIES_SUIT_FRONT_NECK_DEPTH_TITLE = "Front Neck Depth";
        public static final String LADIES_SUIT_BACK_NECK_DEPTH_TITLE = "Back Neck Depth";
        public static final String LADIES_SUIT_SALWAR_HIP_TITLE = "Salwar Hip";
        public static final String LADIES_SUIT_KNEE_TITLE = "Knee";
        public static final String LADIES_SUIT_ANKLE_TITLE = "Ankle";
        public static final String LADIES_SUIT_SALWAR_LENGTH_TITLE = "Salwar Length";

        public static final String DRESS_OUTFIT_TYPE_HEADING = "Dress";
        public static final String PANTS_OUTFIT_TYPE_HEADING = "Pants";
        public static final String SHIRT_OUTFIT_TYPE_HEADING = "Shirt";
        public static final String NIGHT_GOWN_OUTFIT_TYPE_HEADING = "Night Gown";
        public static final String UNDER_SHIRT_OUTFIT_TYPE_HEADING = "Underskirt";
        public static final String SAREE_BLOUSE_OUTFIT_TYPE_HEADING = "Saree Blouse";
        public static final String MENS_KURTA_OUTFIT_TYPE_HEADING = "Men's Kurta";
        public static final String MENS_PYJAMA_OUTFIT_TYPE_HEADING = "Men's Pajama";
        public static final String MENS_SUIT_TOP_OUTFIT_TYPE_HEADING = "Men's Suit";
        public static final String MENS_SUIT_PANTS_OUTFIT_TYPE_HEADING = "Men's Suit(Pants)";
        public static final String LADIES_SUIT_KAMEEZ_OUTFIT_TYPE_HEADING = "Ladies Suit(Kameez)";
        public static final String LADIES_SUIT_SALWAR_OUTFIT_TYPE_HEADING = "Ladies Suit(Salwar)";


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
        public static final String GOWN_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_shoulder.svg";
        public static final String GOWN_SLEEVE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_slevelen.svg";
        public static final String GOWN_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_seat.svg";
        public static final String GOWN_SLEEVE_CIRCUMFERENCE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_slevcircum.svg";
        public static final String GOWN_FRONT_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_frontneck.svg";
        public static final String GOWN_BACK_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_backneck.svg";
        public static final String UNDER_SKIRT_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Underskirt/Underskirt.svg";

        public static final String UNDER_SKIRT_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Underskirt/us_length.svg";
        public static final String UNDER_SKIRT_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Underskirt/us_waist.svg";
        public static final String BLOUSE_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse.svg";
        public static final String BLOUSE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_len.svg";
        public static final String BLOUSE_BUST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_bust.svg";
        public static final String BLOUSE_UPPOER_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_upperchest.svg";
        public static final String BLOUSE_BELOW_BUST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_belowbust.svg";
        public static final String BLOUSE_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_shoulder.svg";
        public static final String BLOUSE_ARMHOLE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_armhole.svg";
        public static final String BLOUSE_SLEEVE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_slevelen.svg";
        public static final String BLOUSE_SLEEVE_CICUM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_slevcircum.svg";
        public static final String BLOUSE_FRONT_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_frontneck.svg";
        public static final String BLOUSE_SHOULDER_TO_APEX_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_sholtoapex.svg";
        public static final String BLOUSE_APEX_TO_APEX_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_apextoapex.svg";
        public static final String BLOUSE_BACK_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse_back+neck.svg";
        public static final String SHIRT_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Shirt/shirt.svg";
        public static final String SHIRT_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Shirt/shirt_length.svg";
        public static final String SHIRT_NECK_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Shirt/shirt_neck.svg";
        public static final String SHIRT_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Shirt/shirt_shoulder.svg";
        public static final String SHIRT_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Shirt/shirt_chest.svg";
        public static final String SHIRT_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Shirt/shirt_waist.svg";
        public static final String SHIRT_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Shirt/shirt_seat.svg";
        public static final String SHIRT_SLEEVE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Shirt/shirt_sleeve.svg";
        public static final String SHIRT_SLEEVE_CIRCUM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Shirt/shirt_sleevecircum.svg";
        public static final String PANTS_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants.svg";
        public static final String PANTS_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants_waist.svg";
        public static final String PANTS_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants_seat.svg";
        public static final String PANTS_CALF_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants_calf.svg";
        public static final String PANTS_BOTTOM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants_bottom.svg";
        public static final String PANTS_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants_length.svg";
        public static final String PANTS_FLY_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants_fly.svg";
        public static final String KURTA_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown.svg";
        public static final String KURTA_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown1_len.svg";
        public static final String KURTA_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown2_shoulder.svg";
        public static final String KURTA_UPPER_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown3_upperchest.svg";
        public static final String KURTA_BUST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown4_bust.svg";
        public static final String KURTA_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown5_waist.svg";
        public static final String KURTA_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown6_seat.svg";
        public static final String KURTA_ARMHOLE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown7_armhole.svg";
        public static final String KURTA_SLEEVE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown8_slevlen.svg";
        public static final String KURTA_SLEEVE_CIRCUM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown9_slevcircum.svg";
        public static final String KURTA_FRONT_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown10_frontneckdept.svg";
        public static final String KURTA_BACK_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/menskurta/nightgown11_backneckdept.svg";
        public static final String PYJAMA_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/Pajama/pajama.svg";
        public static final String PYJAMA_HIP_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/Pajama/pajama_hip.svg";
        public static final String PYJAMA_KNEE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/Pajama/pajama_knee.svg";
        public static final String PYJAMA_ANKLE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/Pajama/pajama_ankle.svg";
        public static final String PYJAMA_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Kurta+pajama/Pajama/pajama_len.svg";
        public static final String MENS_SUIT_UPPER_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/Mens'suit(top)/shirt.svg";
        public static final String MENS_SUIT_UPPER_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/Mens'suit(top)/shirt_length.svg";
        public static final String MENS_SUIT_NECK_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/Mens'suit(top)/shirt_neck.svg";
        public static final String MENS_SUIT_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/Mens'suit(top)/shirt_shoulder.svg";
        public static final String MENS_SUIT_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/Mens'suit(top)/shirt_chest.svg";
        public static final String MENS_SUIT_UPPER_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/Mens'suit(top)/shirt_waist.svg";
        public static final String MENS_SUIT_UPPER_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/Mens'suit(top)/shirt_seat.svg";
        public static final String MENS_SUIT_SLEEVE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/Mens'suit(top)/shirt_sleeve.svg";
        public static final String MENS_SUIT_SLEEVE_CIRCUM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/Mens'suit(top)/shirt_sleevecircum.svg";
        public static final String MENS_SUIT_LOWER_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/mens'suit(pants)/pants.svg";
        public static final String MENS_SUIT_LOWER_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/mens'suit(pants)/pants_waist.svg";
        public static final String MENS_SUIT_LOWER_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/mens'suit(pants)/pants_seat.svg";
        public static final String MENS_SUIT_CALF_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/mens'suit(pants)/pants_calf.svg";
        public static final String MENS_SUIT_LOWER_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/mens'suit(pants)/pants_length.svg";
        public static final String MENS_SUIT_BOTTOM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/mens'suit(pants)/pants_bottom.svg";
        public static final String MENS_SUIT_FLY_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Mens+Suit/mens'suit(pants)/pants_fly.svg";
        public static final String LADIES_SUIT_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/nightgown.svg";
        public static final String LADIES_SUIT_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_length.svg";
        public static final String LADIES_SUIT_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_shoulder.svg";
        public static final String LADIES_SUIT_UPPER_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_upperchest.svg";
        public static final String LADIES_SUIT_BUST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_bust.svg";
        public static final String LADIES_SUIT_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_waist.svg";
        public static final String LADIES_SUIT_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NightGown/ng_seat.svg";
        public static final String LADIES_SUIT_ARMHOLE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_armhole.svg";
        public static final String LADIES_SUIT_SLEEVE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_slevelen.svg";
        public static final String LADIES_SUIT_SLEEVE_CIRCUM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_slevcircum.svg";
        public static final String LADIES_SUIT_FRONT_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_frontneck.svg";
        public static final String LADIES_SUIT_BACK_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/kameze/ng_backneck.svg";
        public static final String LADIES_SUIT_LOWER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/salwar/pajama.svg";
        public static final String LADIES_SUIT_SALWAR_HIP_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/salwar/pajama_hip.svg";
        public static final String LADIES_SUIT_KNEE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/salwar/pajama_knee.svg";
        public static final String LADIES_SUIT_ANKLE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/salwar/pajama_ankle.svg";
        public static final String LADIES_SUIT_SALWAR_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Salwar+kameez/salwar/pajama_len.svg";

    }
    public class OutfitType {
        public static final String KURTA_PYJAMA_TITLE = "Men's Kurta Pajama";
        public static final String DRESS_TITLE = "Dress";
        public static final String SAREE_BLOUSE_TITLE = "Saree Blouse";
        public static final String MENS_SUIT_TITLE = "Men's Suit";
        public static final String PANTS_TITLE = "Pant";
        public static final String NIGHT_GOWN_TITLE = "Night Gown";
        public static final String LADIES_SUIT_TITLE = "Ladies Suit";
        public static final String SHIRT_TITLE = "Shirt";
        public static final String UNDER_SKIRT_TITLE = "Under Skirt";
        public static final String OUTFIT_TYPE_KURTA_PYJAMA_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/Mens'+kurta.svg";
        public static final String OUTFIT_TYPE_DRESS_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/dress.svg";
        public static final String OUTFIT_TYPE_SAREE_BLOUSE_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/saree+blouse.svg";
        public static final String OUTFIT_TYPE_MENS_SUIT_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/mens+suit.svg";
        public static final String OUTFIT_TYPE_PANT_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/pants.svg";
        public static final String OUTFIT_TYPE_NIGHT_GOWN_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/night+gown.svg";
        public static final String OUTFIT_TYPE_LADIES_SUIT_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/womens+suit.svg";
        public static final String OUTFIT_TYPE_SHIRT_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/shirt.svg";
        public static final String OUTFIT_TYPE_UNDER_SKIRT_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/underskirt.svg";


    }

    public static final String PARAMS_MAP_SORT_KEY = "sort_on";
    public static final String PARAMS_MAP_COUNT_KEY = "count";
    public static final String PARAMS_MAP_OFFSET_KEY = "offset";

    public static final List<String> PHONE_NUMBER_POLLUTERS = Arrays.asList("+91", "0");


}
