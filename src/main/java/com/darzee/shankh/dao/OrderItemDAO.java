package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OrderItemStatus;
import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.response.FileDetail;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDAO {
    private Long id;

    private LocalDateTime trialDate;

    private LocalDateTime deliveryDate;

    private String specialInstructions;

    private OrderType orderType;
    private String outfitAlias;

    private OutfitType outfitType;

    private String inspiration;

    private Boolean isPriorityOrder;
    private Boolean isDeleted;

    private OrderItemStatus orderItemStatus = OrderItemStatus.DRAFTED;

    private Integer quantity;

    private MeasurementRevisionsDAO measurementRevision;

    private List<PriceBreakupDAO> priceBreakup;

    private List<FileDetail> clothImageDetails;

    private List<FileDetail> audioFileDetails;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private OrderDAO order;

    public OrderItemDAO(LocalDateTime trialDate, LocalDateTime deliveryDate, String specialInstructions,
                        OrderType orderType, OutfitType outfitType, String inspiration, Boolean isPriorityOrder,
                        Integer quantity, String outfitAlias, MeasurementRevisionsDAO measurementRevision, OrderDAO orderDAO) {
        this.trialDate = trialDate;
        this.deliveryDate = deliveryDate;
        this.specialInstructions = specialInstructions;
        this.orderType = orderType;
        this.outfitType = outfitType;
        this.inspiration = inspiration;
        this.isPriorityOrder = isPriorityOrder;
        this.quantity = quantity;
        this.measurementRevision = measurementRevision;
        this.outfitAlias = outfitAlias;
        this.order = orderDAO;
    }

    public boolean isStatusUpdated(Integer valueOrd) {
        OrderItemStatus value = OrderItemStatus.getOrderItemTypeEnumOrdinalMap().get(valueOrd);
        return value != null && !value.equals(this.getOrderItemStatus());
    }

    public boolean isTrialDateUpdated(LocalDateTime value) {
        return value != null && !value.equals(this.getTrialDate());
    }

    public boolean isDeliveryDateUpdated(LocalDateTime value) {
        return value != null && !value.equals(this.getDeliveryDate());
    }

    public boolean areSpecialInstructionsUpdated(String value) {
        return value != null && !value.equals(this.getSpecialInstructions());
    }

    public boolean isInspirationUpdated(String value) {
        return value != null && !value.equals(this.getInspiration());
    }


    public boolean isPriorityUpdated(Boolean value) {
        return value != null && !value.equals(this.getIsPriorityOrder());
    }

    public boolean isQuantityUpdated(Integer value) {
        return value != null && !value.equals(this.getQuantity());
    }

    public boolean isMeasurementRevisionUpdated(Long value) {
        return (this.measurementRevision == null && value != null)
                || (this.measurementRevision != null && value != null && !value.equals(
                this.getMeasurementRevision().getId()));
    }

    public boolean mandatoryFieldsPresent() {
        String outfitAlias = Optional.ofNullable(this.outfitAlias).orElse(this.outfitType.getDisplayString());
        if (this.deliveryDate == null || this.orderType == null || this.outfitType == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Some Mandatory fields in Order Item "
                            + outfitAlias
                            + "are missing. Delivery date, Order type, Outfit Type are Mandatory params");
        }
        if (CollectionUtils.isEmpty(this.priceBreakup)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Price Break Up of Order Item " + outfitAlias + " is missing");
        }
        return true;
    }

    public Double calculateItemPrice() {
        return this.priceBreakup.stream().mapToDouble(pb -> pb.getValue() * pb.getQuantity()).sum();
    }
    
    public List<PriceBreakupDAO> getActivePriceBreakUpList() {
        return this.getPriceBreakup().stream()
                .filter(pb -> !Boolean.TRUE.equals(pb.getIsDeleted()))
                .collect(Collectors.toList());
    }

}
