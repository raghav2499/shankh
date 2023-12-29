package com.darzee.shankh.response;

import com.darzee.shankh.dao.OrderItemDAO;
import com.darzee.shankh.enums.OutfitType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemDetails {
    private Boolean isPriorityOrder;
    private String outfitType;
    private String outfitTypeName;
    private Integer outfitTypeIndex;
    private String trialDate;
    private String deliveryDate;
    private String inspiration;
    private String specialInstructions;
    private String type;
    private List<String> clothImageRefIds;
    private List<String> clothImagesLink;
    private String outfitTypeImageLink;
    private OutfitMeasurementDetails measurementDetails;


    public OrderItemDetails(OrderItemDAO orderItem, String outfitTypeImgLink) {
        OutfitType outfitType = orderItem.getOutfitType();
        this.isPriorityOrder = Optional.ofNullable(orderItem.getIsPriorityOrder()).orElse(Boolean.FALSE);
        this.outfitType = outfitType.getDisplayString();
        this.trialDate = (orderItem.getTrialDate() != null ? orderItem.getTrialDate().toString() : null);
        this.deliveryDate = orderItem.getDeliveryDate().toString();
        this.outfitTypeIndex = outfitType.getOrdinal();
        this.outfitTypeImageLink = outfitTypeImgLink;
    }

    public OrderItemDetails(OutfitMeasurementDetails measurementDetails, List<String> clothImagesReferenceIds,
                            List<String> clothImageUrlLinks, String outfitImageLink, OrderItemDAO orderItem) {
        this.measurementDetails = measurementDetails;
        this.clothImageRefIds = clothImagesReferenceIds;
        this.clothImagesLink = clothImageUrlLinks;
        this.isPriorityOrder = Optional.ofNullable(orderItem.getIsPriorityOrder()).orElse(Boolean.FALSE);
        this.outfitType = orderItem.getOutfitType().getDisplayString();
        this.outfitTypeName = orderItem.getOutfitType().getName();
        this.outfitTypeIndex = orderItem.getOutfitType().getOrdinal();
        this.outfitTypeImageLink = outfitImageLink;
        this.trialDate = (orderItem.getTrialDate() != null ? orderItem.getTrialDate().toString() : null);
        this.deliveryDate = orderItem.getDeliveryDate().toString();
        this.type = orderItem.getOrderType().getDisplayName();
        this.inspiration = orderItem.getInspiration();
        this.specialInstructions = orderItem.getSpecialInstructions();
    }
}
