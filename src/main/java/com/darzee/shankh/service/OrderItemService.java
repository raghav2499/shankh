package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.dao.OrderItemDAO;
import com.darzee.shankh.dao.PriceBreakupDAO;
import com.darzee.shankh.entity.OrderItem;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.OrderItemRepo;
import com.darzee.shankh.request.innerObjects.OrderItemDetailRequest;
import com.darzee.shankh.request.innerObjects.UpdateOrderItemDetails;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private PriceBreakUpService priceBreakUpService;

    @Autowired
    private ObjectImagesService objectImagesService;

    @Autowired
    private DaoEntityMapper mapper;

    public List<OrderItemDAO> createOrderItems(List<OrderItemDetailRequest> orderItemDetails, OrderDAO order) {
        Map<String, Long> clothRefOrderItemIdMap = new HashMap<>();
        List<PriceBreakupDAO> priceBreakUpList = new ArrayList<>();
        List<OrderItemDAO> orderItemList = Optional.ofNullable(order.getOrderItems()).orElse(new ArrayList<>());
        for (OrderItemDetailRequest itemDetail : orderItemDetails) {
            OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(itemDetail.getOutfitType());
            if (outfitType == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid outfit type " + itemDetail.getOutfitType());
            }
            OrderItemDAO orderItemDAO = new OrderItemDAO(itemDetail.getTrialDate(), itemDetail.getDeliveryDate(),
                    itemDetail.getSpecialInstructions(), itemDetail.getOrderType(), outfitType,
                    itemDetail.getInspiration(), itemDetail.getIsPriorityOrder(), itemDetail.getItemQuantity(),
                    itemDetail.getMeasurementRevisionId(), order);
            orderItemDAO = mapper.orderItemToOrderItemDAO(orderItemRepo.save(mapper.orderItemDAOToOrderItem(orderItemDAO,
                    new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
            orderItemList.add(orderItemDAO);
            List<PriceBreakupDAO> priceBreakupDAOList =
                    priceBreakUpService.generatePriceBreakupList(itemDetail.getPriceBreakup(), orderItemDAO);
            priceBreakUpList.addAll(priceBreakupDAOList);
            for (String imageRef : itemDetail.getClothImageReferenceIds()) {
                clothRefOrderItemIdMap.put(imageRef, orderItemDAO.getId());
            }
        }
        priceBreakUpService.savePriceBreakUp(priceBreakUpList);
        objectImagesService.saveObjectImages(clothRefOrderItemIdMap, ImageEntityType.ORDER_ITEM.getEntityType());
        return orderItemList;
    }

    @Transactional
    public OrderItemDAO updateOrderItem(Long orderItemId, UpdateOrderItemDetails updateItemDetail) {
        OrderItemDAO orderItem = null;
        Optional<OrderItem> orderItemOb = orderItemRepo.findById(orderItemId);
        if (orderItemOb.isPresent()) {
            orderItem = mapper.orderItemToOrderItemDAO(orderItemOb.get(), new CycleAvoidingMappingContext());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item ID is invalid");
        }
        if (orderItem.isTrialDateUpdated(updateItemDetail.getTrialDate())) {
            orderItem.setTrialDate(updateItemDetail.getTrialDate());
        }
        if (orderItem.isDeliveryDateUpdated(updateItemDetail.getDeliveryDate())) {
            orderItem.setDeliveryDate(updateItemDetail.getDeliveryDate());
        }
        if (orderItem.isPriorityUpdated(updateItemDetail.getIsPriorityOrder())) {
            orderItem.setIsPriorityOrder(updateItemDetail.getIsPriorityOrder());
        }
        if (orderItem.isInspirationUpdated(updateItemDetail.getInspiration())) {
            orderItem.setInspiration(updateItemDetail.getInspiration());
        }
        if (orderItem.areSpecialInstructionsUpdated(updateItemDetail.getSpecialInstructions())) {
            orderItem.setSpecialInstructions(updateItemDetail.getSpecialInstructions());
        }
        if (orderItem.isQuantityUpdated(updateItemDetail.getItemQuantity())) {
            orderItem.setQuantity(updateItemDetail.getItemQuantity());
        }
        if(orderItem.isMeasurementRevisionUpdated(updateItemDetail.getMeasurementRevisionId())) {
            orderItem.setMeasurementRevisionId(updateItemDetail.getMeasurementRevisionId());
        }
        if (!Collections.isEmpty(updateItemDetail.getClothImageReferenceIds())) {
            objectImagesService.invalidateExistingReferenceIds(ImageEntityType.ORDER_ITEM.getEntityType(),
                    orderItemId);
            objectImagesService.saveObjectImages(updateItemDetail.getClothImageReferenceIds(),
                    ImageEntityType.ORDER_ITEM.getEntityType(), orderItemId);
        }
        if (!Collections.isEmpty(updateItemDetail.getPriceBreakupDetails())) {
            List<PriceBreakupDAO> updatedPriceBreakup =
                    priceBreakUpService.updatePriceBreakups(updateItemDetail.getPriceBreakupDetails(), orderItem);
            orderItem.setPriceBreakup(updatedPriceBreakup);
        }
        orderItem = mapper.orderItemToOrderItemDAO(orderItemRepo.save(mapper.orderItemDAOToOrderItem(orderItem,
                new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return orderItem;
    }

}