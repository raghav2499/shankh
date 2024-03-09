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
import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemDetails {

    private Long id;
    private Boolean isPriorityOrder;
    private String outfitType;
    private String outfitTypeName;
    private Integer outfitTypeIndex;

    private String outfitTypeImageLink;
    private String trialDate;
    private String deliveryDate;
    private String inspiration;
    private String specialInstructions;
    private String type;
    private Integer pieces;
    private Integer itemQuantity;
    private Double itemPrice;
    private List<FileDetail> clothImageFileDetails;
    private List<FileDetail> audioFileDetails;

    private OverallMeasurementDetails measurementDetails;
    private Map<String, List<OrderStitchOptionDetail>> orderItemStitchOptions;
    private Long orderId;
    private String customerName;
    private String status;
    private String outfitAlias;

    public OrderItemDetails(OrderItemDAO orderItem, String outfitTypeImgLink) {
        OutfitType outfitType = orderItem.getOutfitType();
        this.id = orderItem.getId();
        this.isPriorityOrder = Optional.ofNullable(orderItem.getIsPriorityOrder()).orElse(Boolean.FALSE);
        this.outfitType = outfitType.getDisplayString();
        this.trialDate = (orderItem.getTrialDate() != null ? orderItem.getTrialDate().toString() : null);
        this.deliveryDate = orderItem.getDeliveryDate().toString();
        this.itemQuantity = orderItem.getQuantity();
        this.outfitTypeIndex = outfitType.getOrdinal();
        this.outfitTypeImageLink = outfitTypeImgLink;
        this.pieces = outfitType.getPieces();
        this.status = orderItem.getOrderItemStatus().getDisplayString();
        this.customerName = orderItem.getOrder().getCustomer().constructName();
        this.orderId = orderItem.getOrder().getId();
        this.itemPrice = orderItem.calculateItemPrice();
        this.outfitAlias = orderItem.getOutfitAlias();
    }

    public OrderItemDetails(List<FileDetail> clothImageFileDetail, List<FileDetail> audioFileDetail,
                            OverallMeasurementDetails measurementDetails,
                            Map<String, List<OrderStitchOptionDetail>> orderItemStitchOptions, String outfitImageLink,
                            OrderItemDAO orderItem) {
        this.clothImageFileDetails = clothImageFileDetail;
        this.audioFileDetails = audioFileDetail;
        this.id = orderItem.getId();
        this.isPriorityOrder = Optional.ofNullable(orderItem.getIsPriorityOrder()).orElse(Boolean.FALSE);
        this.outfitType = orderItem.getOutfitType().getDisplayString();
        this.outfitTypeName = orderItem.getOutfitType().getName();
        this.outfitTypeIndex = orderItem.getOutfitType().getOrdinal();
        this.outfitTypeImageLink = outfitImageLink;
        this.trialDate = (orderItem.getTrialDate() != null ? orderItem.getTrialDate().toString() : null);
        this.deliveryDate = orderItem.getDeliveryDate().toString();
        this.type = orderItem.getOrderType().getDisplayName();
        this.itemQuantity = orderItem.getQuantity();
        this.inspiration = orderItem.getInspiration();
        this.specialInstructions = orderItem.getSpecialInstructions();
        this.pieces = orderItem.getOutfitType().getPieces();
        this.measurementDetails = measurementDetails;
        this.orderItemStitchOptions = orderItemStitchOptions;
        this.trialDate = orderItem.getTrialDate().toString();
        this.itemPrice = orderItem.calculateItemPrice();
        this.status = orderItem.getOrderItemStatus().getDisplayString();
        this.outfitAlias = orderItem.getOutfitAlias();
    }
}
