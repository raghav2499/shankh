package com.darzee.shankh.constants;

import com.darzee.shankh.enums.OrderStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final List<OrderStatus> ACTIVE_ORDER_STATUS_LIST = Arrays.asList(OrderStatus.STITCHING_IN_PROGRESS,
            OrderStatus.ORDER_READY_FOR_TRIAL,
            OrderStatus.ORDER_COMPLETED);

        public static final List<OrderStatus> CLOSED_ORDER_STATUS_LIST = Arrays.asList(OrderStatus.ORDER_DELIVERED);
    public static final Double CM_TO_INCH_DIVIDING_FACTOR = 2.54;
    public static final Double INCH_TO_CM_MULTIPLYING_FACTOR = 2.54;
    public static final Double DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE = 0d;

    public static final LocalDate REPORTING_START_DATE = LocalDate.of(2023,5,1);

    public class MeasurementTitles {

        public static final String DRESS_LENGTH_TITLE = "Length";
        public static final String DRESS_SHOULDER_TITLE = "Shoulder";
        public static final String DRESS_UPPER_CHEST_TITLE = "Upper Chest";
        public static final String DRESS_BUST_TITLE = "Bust";
        public static final String DRESS_WAIST_TITLE = "Waist";
        public static final String DRESS_SEAT_TITLE = "Seat";
        public static final String DRESS_ARMHOLE_TITLE = "Arm Hole";
        public static final String DRESS_SLEEVE_LENGTH_TITLE = "Sleeve Length";
        public static final String DRESS_SLEEVE_CIRCUM_TITLE = "Sleeve Circum";

        public static final String DRESS_FRONT_NECK_DEPTH_TITLE = "Front Neck Depth";
        public static final String DRESS_BACK_NECK_DEPTH_TITLE = "Back Neck Depth";

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
        public static final String BLOUSE_BICEP_TITLE = "Bicep";
        public static final String BLOUSE_ELBOW_ROUND_TITLE = "Elbow Round";
        public static final String BLOUSE_FRONT_NECK_DEPTH_TITLE = "Front Neck Depth";
        public static final String BLOUSE_SHOULDER_TO_APEX_TITLE = "Shoulder to Apex";
        public static final String BLOUSE_APEX_TO_APEX_TITLE = "Apex to Apex";
        public static final String BLOUSE_BACK_NECK_DEPTH_TITLE = "Back Neck Depth";
        public static final String BLOUSE_CROSS_FRONT_TITLE = "Cross Front";
        public static final String BLOUSE_CROSS_BACK_TITLE = "Cross Back";
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

        public static final String PANTS_THIGH_TITLE = "Thigh Circum";

        public static final String PANTS_CALF_TITLE = "Calf/Knee";

        public static final String PANTS_BOTTOM_TITLE = "Bottom/Bells";

        public static final String PANTS_LENGTH_TITLE = "Length";

        public static final String PANTS_FLY_TITLE = "Fly(Ply)";
        public static final String PANTS_IN_SEAM_TITLE = "In-Seam";
        public static final String PANTS_CROTCH_TITLE = "Crotch";
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

        public static final String NEHRU_JACKET_LENGTH_TITLE = "Length";

        public static final String NEHRU_JACKET_NECK_TITLE = "Neck";

        public static final String NEHRU_JACKET_CHEST_TITLE = "Chest";

        public static final String NEHRU_JACKET_WAIST_TITLE = "Waist";
        public static final String WAIST_COAT_LENGTH_TITLE = "Length";
        public static final String WAIST_COAT_CHEST_TITLE = "Chest";

        public static final String WAIST_COAT_WAIST_TITLE = "Waist";

        public static final String RIDA_ABOVE_HEAD_TITLE = "Above Head";
        public static final String RIDA_PARDI_SHOULDER_TITLE = "Shoulder";
        public static final String RIDA_AROUND_SHOULDER_TITLE = "Around Shoulder";
        public static final String RIDA_PARDI_LENGTH_TITLE = "Length";
        public static final String RIDA_PARDI_GHER_TITLE = "Gher";
        public static final String RIDA_KAS_TITLE = "Kas";
        public static final String RIDA_LENGA_SHOULDER_TITLE = "Shoulder";
        public static final String RIDA_BUST_TITLE = "Bust";
        public static final String RIDA_WAIST_TITLE = "Waist";
        public static final String RIDA_HIP_TITLE = "Hips";
        public static final String RIDA_LENGA_LENGTH_TITLE = "Length";
        public static final String RIDA_LENGA_GHER_TITLE = "Gher";

        public static final String DRESS_OUTFIT_TYPE_HEADING = "Dress";
        public static final String PANTS_OUTFIT_TYPE_HEADING = "Pants";
        public static final String SHIRT_OUTFIT_TYPE_HEADING = "Shirt";
        public static final String NIGHT_GOWN_OUTFIT_TYPE_HEADING = "Night Gown";
        public static final String UNDER_SHIRT_OUTFIT_TYPE_HEADING = "Underskirt";
        public static final String SAREE_BLOUSE_OUTFIT_TYPE_HEADING = "Saree Blouse";
        public static final String MENS_KURTA_OUTFIT_TYPE_HEADING = "Kurta Pajama (Top)";
        public static final String MENS_PYJAMA_OUTFIT_TYPE_HEADING = "Kurta Pajama (Bottom)";
        public static final String MENS_SUIT_TOP_OUTFIT_TYPE_HEADING = "Men's Suit (Top)";
        public static final String MENS_SUIT_PANTS_OUTFIT_TYPE_HEADING = "Men's Suit (Bottom)";
        public static final String LADIES_SUIT_KAMEEZ_OUTFIT_TYPE_HEADING = "Ladies Suit (Top)";
        public static final String LADIES_SUIT_SALWAR_OUTFIT_TYPE_HEADING = "Ladies Suit (Bottom)";
        public static final String NEHRU_JACKET_OUTFIT_TYPE_HEADING = "Nehru Jacket";
        public static final String WAIST_COAT_OUTFIT_TYPE_HEADING = "Waist Coat";
        public static final String RIDA_PARDI_OUTFIT_TYPE_HEADING = "Pardi";
        public static final String RIDA_LENGA_OUTFIT_TYPE_HEADING = "Lenga";


    }
    public class ImageLinks {
        public static final String DRESS_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress.svg";
        public static final String DRESS_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress1_len.svg";
        public static final String DRESS_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress2_shoulder.svg";
        public static final String DRESS_UPPER_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress3_upperchest.svg";
        public static final String DRESS_BUST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress4_bust.svg";
        public static final String DRESS_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress5_waist.svg";
        public static final String DRESS_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress6_seat.svg";
        public static final String DRESS_ARMHOLE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress7_armhole.svg";
        public static final String DRESS_SLEEVE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress8_slevlen.svg";
        public static final String DRESS_SLEEVE_CIRCUMFERENCE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress9_slevcircum.svg";
        public static final String DRESS_FRONT_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress10_frontneckdept.svg";
        public static final String DRESS_BACK_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Dress/dress11_backneckdept.svg";
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
        public static final String BLOUSE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse1_len.svg";
        public static final String BLOUSE_BUST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse2_bust.svg";
        public static final String BLOUSE_UPPOER_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse3_upperchest.svg";
        public static final String BLOUSE_BELOW_BUST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse4_belowbust.svg";
        public static final String BLOUSE_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse5_shoulder.svg";
        public static final String BLOUSE_ARMHOLE_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse6_armhole.svg";
        public static final String BLOUSE_SLEEVE_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse7_slevelen.svg";
        public static final String BLOUSE_SLEEVE_CICUM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse8_slevcircumeference.svg";
        public static final String BLOUSE_BICEP_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse9_bicep.svg";
        public static final String BLOUSE_ELBOW_ROUND_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse10_elbowround.svg";
        public static final String BLOUSE_APEX_TO_APEX_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse11_apextoapex.svg";
        public static final String BLOUSE_SHOULDER_TO_APEX_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse12_shouldertoapex.svg";
        public static final String BLOUSE_FRONT_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse13_frontneckdepth.svg";
        public static final String BLOUSE_BACK_NECK_DEPTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse14_backneckdepth.svg";
        public static final String BLOUSE_CROSS_FRONT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse15_crossfront.svg";
        public static final String BLOUSE_CROSS_BACK_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Blouse/blouse16_crossback.svg";
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
        public static final String PANTS_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants1_waist.svg";
        public static final String PANTS_SEAT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants2_seat.svg";
        public static final String PANTS_THIGH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants3_thie.svg";
        public static final String PANTS_CALF_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants4_calf.svg";
        public static final String PANTS_BOTTOM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants5_bottom.svg";
        public static final String PANTS_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pant6_len.svg";
        public static final String PANTS_FLY_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants7_fly.svg";
        public static final String PANTS_IN_SEAM_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants8_Inseam.svg";
        public static final String PANTS_CROTCH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Pants/pants9_Crotch.svg";
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

        public static final String NEHRU_JACKET_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NehruJacket/Nehrujacket.svg";

        public static final String NEHRU_JACKET_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NehruJacket/nehrujacket1_length.svg";
        public static final String NEHRU_JACKET_NECK_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NehruJacket/nehrujacket2_neck.svg";
        public static final String NEHRU_JACKET_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NehruJacket/nehrujacket3_chest.svg";
        public static final String NEHRU_JACKET_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/NehruJacket/nehrujacket4_waist.svg";

        public static final String WAIST_COAT_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/WaistCoat/Waistcoat.svg";

        public static final String WAIST_COAT_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/WaistCoat/Waistcoat1_length.svg";
        public static final String WAIST_COAT_CHEST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/WaistCoat/Waistcoat2_chest.svg";
        public static final String WAIST_COAT_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/WaistCoat/Waistcoat3_waist.svg";
        public static final String PARDI_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/pardi/bhurkaupper.svg";
        public static final String RIDA_ABOVE_HEAD_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/pardi/bhurkaupper_above_head.svg";
        public static final String RIDA_PARDI_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/pardi/bhurkaupper_shoulder.svg";
        public static final String RIDA_AROUND_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/pardi/bhurkaupper_around_shoulder.svg";
        public static final String RIDA_PARDI_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/pardi/bhurkaupper_length.svg";
        public static final String RIDA_PARDI_GHER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/pardi/bhurkaupper_gher.svg";
        public static final String RIDA_KAS_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/pardi/bhurkaupper_kas.svg";

        public static final String LENGA_OUTFIT_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/lenga/bhurka.svg";
        public static final String RIDA_LENGA_SHOULDER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/lenga/bhurka1_shoulder.svg";
        public static final String RIDA_BUST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/lenga/bhurka2_Bust.svg";
        public static final String RIDA_WAIST_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/lenga/bhurka3_waist.svg";
        public static final String RIDA_HIPS_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/lenga/bhurka4_Hips.svg";
        public static final String RIDA_LENGA_LENGTH_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/lenga/bhurka5_Length.svg";
        public static final String RIDA_LENGA_GHER_IMAGE_LINK = "https://s3.amazonaws.com/darzee.backend.static/Measurement/Rida/lenga/bhurka6_gher.svg";

    }

    public class MeasurementKeys {
        public static final String LENGTH_MEASUREMENT_KEY = "length";
        public static final String WAIST_MEASUREMENT_KEY = "waist";
        public static final String BLOUSE_LENGTH_MEASUREMENT_KEY = "blouse_length";

        public static final String BUST_MEASUREMENT_KEY = "bust";
        public static final String UPPER_CHEST_MEASUREMENT_KEY = "upper_chest";
        public static final String BELOW_BUST_MEASUREMENT_KEY = "below_bust";
        public static final String SHOULDER_MEASUREMENT_KEY = "shoulder";

        public static final String ARM_HOLE_MEASUREMENT_KEY = "arm_hole";

        public static final String SLEEVE_LENGTH_MEASUREMENT_KEY = "sleeve_length";
        public static final String SLEEVE_CIRCUMFERENCE_MEASUREMENT_KEY = "sleeve_circumference";
        public static final String BICEP_MEASUREMENT_KEY = "bicep";
        public static final String ELBOW_ROUND_MEASUREMENT_KEY = "elbow_round";
        public static final String CROSS_FRONT_MEASUREMENT_KEY = "cross_front";
        public static final String CROSS_BACK_MEASUREMENT_KEY = "cross_back";

        public static final String FRONT_NECK_DEPTH_MEASUREMENT_KEY = "front_neck_depth";

        public static final String SHOULDER_TO_APEX_LENGTH_MEASUREMENT_KEY = "shoulder_to_apex_length";
        public static final String APEX_TO_APEX_LENGTH_MEASUREMENT_KEY = "apex_to_apex_length";
        public static final String BACK_NECK_DEPTH_MEASUREMENT_KEY = "back_neck_depth";
        public static final String SEAT_MEASUREMENT_KEY = "seat";
        public static final String GOWN_LENGTH_MEASUREMENT_KEY = "gown_length";
        public static final String SHIRT_LENGTH_MEASUREMENT_KEY = "shirt_length";

        public static final String NECK_MEASUREMENT_KEY = "neck";
        public static final String CHEST_MEASUREMENT_KEY = "chest";
        public static final String BOTTOM_WAIST_MEASUREMENT_KEY = "bottom_waist";
        public static final String BOTTOM_SEAT_MEASUREMENT_KEY = "bottom_seat";
        public static final String CALF_MEASUREMENT_KEY = "calf";

        public static final String BOTTOM_MEASUREMENT_KEY = "bottom";
        public static final String PANT_LENGTH_MEASUREMENT_KEY = "pant_length";
        public static final String FLY_MEASUREMENT_KEY = "fly";
        public static final String IN_SEAM_MEASUREMENT_KEY = "in_seam";
        public static final String CROTCH_MEASUREMENT_KEY = "crotch";
        public static final String KURTA_LENGTH_MEASUREMENT_KEY = "kurta_length";
        public static final String PYJAMA_LENGTH_MEASUREMENT_KEY = "pyjama_length";
        public static final String PYJAMA_HIP_MEASUREMENT_KEY = "pyjama_hip";

        public static final String KNEE_MEASUREMENT_KEY = "knee";

        public static final String ANKLE_MEASUREMENT_KEY = "ankle";
        public static final String KAMEEZ_LENGTH_MEASUREMENT_KEY = "kameez_length";

        public static final String SALWAR_HIP_MEASUREMENT_KEY = "salwar_hip";
        public static final String SALWAR_LENGTH_MEASUREMENT_KEY = "salwar_length";
        public static final String ABOVE_HEAD_MEASUREMENT_KEY = "above_head";
        public static final String AROUND_SHOULDER_MEASUREMENT_KEY = "around_shoulder";
        public static final String PARDI_SHOULDER_MEASUREMENT_KEY = "pardi_shoulder";
        public static final String LENGA_SHOULDER_MEASUREMENT_KEY = "lenga_shoulder";
        public static final String PARDI_LENGTH_MEASUREMENT_KEY = "pardi_length";
        public static final String LENGA_LENGTH_MEASUREMENT_KEY = "lenga_length";
        public static final String PARDI_GHER_MEASUREMENT_KEY = "pardi_gher";
        public static final String LENGA_GHER_MEASUREMENT_KEY = "lenga_gher";
        public static final String KAS_MEASUREMENT_KEY = "kas";
        public static final String HIP_MEASUREMENT_KEY = "hip";

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
        public static final String OUTFIT_TYPE_NIGHT_GOWN_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/nightgown.svg";
        public static final String OUTFIT_TYPE_LADIES_SUIT_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/womenssuit.svg";
        public static final String OUTFIT_TYPE_SHIRT_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/shirt.svg";
        public static final String OUTFIT_TYPE_UNDER_SKIRT_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/underskirt.svg";

        public static final String OUTFIT_TYPE_NEHRU_JACKET_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/nehrujacket.svg";
        public static final String OUTFIT_TYPE_BURKHA_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/burqa.svg";
        public static final String OUTFIT_TYPE_WAIST_COAT_LINK = "https://s3.amazonaws.com/darzee.backend.static/OutfitType/OutfitType/waistcost.svg";

    }

    public static final String PARAMS_MAP_SORT_KEY = "sort_on";
    public static final String PARAMS_MAP_COUNT_KEY = "count";
    public static final String PARAMS_MAP_OFFSET_KEY = "offset";

    public static final List<String> PHONE_NUMBER_POLLUTERS = Arrays.asList("+91", "0");


}
