package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OrderItemStatus;
import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.request.PriceBreakUpDetails;
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
import java.time.format.DateTimeFormatter;
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
    private Boolean isDeleted = Boolean.FALSE;

    private OrderItemStatus orderItemStatus = OrderItemStatus.DRAFTED;

    private Integer quantity;

    private MeasurementRevisionsDAO measurementRevision;

    private List<PriceBreakupDAO> priceBreakup;

    private List<FileDetail> clothImageDetails;

    private List<FileDetail> audioFileDetails;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String formattedDeliveryDate;
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
        OrderItemStatus value = OrderItemStatus.getOrderItemStatusEnumOrdinalMap().get(valueOrd);
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
        if (CollectionUtils.isEmpty(this.getActivePriceBreakUpList())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Price Break Up of Order Item " + outfitAlias + " is missing");
        }
        return true;
    }

    public Double calculateItemPrice() {
        List<PriceBreakupDAO> priceBreakupList = getActivePriceBreakUpList();
        if (!CollectionUtils.isEmpty(priceBreakupList)) {
            return priceBreakupList.stream()
                    .filter(pb -> !Boolean.TRUE.equals(pb.getIsDeleted()))
                    .mapToDouble(pb -> pb.getValue() * pb.getQuantity())
                    .sum();
        }
        return 0d;
    }

    public List<PriceBreakupDAO> getActivePriceBreakUpList() {
        return this.getPriceBreakup().stream()
                .filter(pb -> !Boolean.TRUE.equals(pb.getIsDeleted()))
                .collect(Collectors.toList());
    }

    public void setFormattedDeliveryDate() {
        if (this.deliveryDate == null) {
            return;
        }
        DateTimeFormatter deliveryDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a");
        this.formattedDeliveryDate = this.deliveryDate.format(deliveryDateFormatter);
    }

    public boolean isPriceBreakupListUpdated(List<PriceBreakUpDetails> priceBreakupList) {
        // If any of the lists is empty, then they are definitely not equal
        if (CollectionUtils.isEmpty(priceBreakupList) || CollectionUtils.isEmpty(this.priceBreakup)) {
            return !CollectionUtils.isEmpty(priceBreakupList) || !CollectionUtils.isEmpty(this.priceBreakup);
        }
        // If the size of the lists is different, then they are definitely not equal
        if (priceBreakupList.size() != this.priceBreakup.size()) {
            return true;
        }
        // Check if any of the elements in the list are different
        return priceBreakupList.stream().anyMatch(pb -> {
            // Find the element in the OrderItem list with a matching id
            Optional<PriceBreakupDAO> existingPb = this.priceBreakup.stream()
                    .filter(epb -> epb.getId().equals(pb.getId())).findFirst();
            // If the element is not found, or any of the fields are different, then it's a
            // different list
            return !existingPb.isPresent() ||
                    !existingPb.get().getValue().equals(pb.getValue()) ||
                    !existingPb.get().getQuantity().equals(pb.getComponentQuantity()) ||
                    !existingPb.get().getIsDeleted().equals(pb.getIsDeleted())
                    || !existingPb.get().getComponent().equals(pb.getComponent());
        });
    }

}
