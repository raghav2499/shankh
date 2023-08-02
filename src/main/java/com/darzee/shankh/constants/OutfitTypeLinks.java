package com.darzee.shankh.constants;

import org.springframework.stereotype.Component;

import static com.darzee.shankh.config.BucketConfig.getStaticBucket;

@Component
public class OutfitTypeLinks {

    public static final String OUTFIT_TYPE_KURTA_PYJAMA_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/Mens'+kurta.svg";
    public static final String OUTFIT_TYPE_DRESS_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/dress.svg";
    public static final String OUTFIT_TYPE_SAREE_BLOUSE_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/saree+blouse.svg";
    public static final String OUTFIT_TYPE_MENS_SUIT_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/mens+suit.svg";
    public static final String OUTFIT_TYPE_PANT_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/pants.svg";
    public static final String OUTFIT_TYPE_NIGHT_GOWN_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/nightgown.svg";
    public static final String OUTFIT_TYPE_LADIES_SUIT_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/womenssuit.svg";
    public static final String OUTFIT_TYPE_SHIRT_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/shirt.svg";
    public static final String OUTFIT_TYPE_UNDER_SKIRT_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/underskirt.svg";

    public static final String OUTFIT_TYPE_NEHRU_JACKET_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/nehrujacket.svg";
    public static final String OUTFIT_TYPE_BURKHA_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/burqa.svg";
    public static final String OUTFIT_TYPE_WAIST_COAT_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/waistcost.svg";
    public static final String OUTFIT_TYPE_LEHENGA_LINK = "https://s3.amazonaws.com/" + getStaticBucket() + "/OutfitType/OutfitType/lehenga.svg";

}