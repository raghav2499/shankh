package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.entity.OrderItem;
import com.darzee.shankh.entity.OrderStitchOptions;
import com.darzee.shankh.entity.StitchOptions;
import com.darzee.shankh.enums.FileEntityType;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.FileReferenceRepo;
import com.darzee.shankh.repo.ObjectFilesRepo;
import com.darzee.shankh.repo.OrderItemRepo;
import com.darzee.shankh.repo.StitchOptionsRepo;
import com.darzee.shankh.request.CreateStitchOptionRequest;
import com.darzee.shankh.request.innerObjects.OrderItemDetailRequest;
import com.darzee.shankh.request.innerObjects.UpdateOrderItemDetailRequest;
import com.darzee.shankh.response.OrderItemDetails;
import com.darzee.shankh.utils.CommonUtils;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private StitchOptionsRepo stitchOptionsRepo;

    @Autowired
    private PriceBreakUpService priceBreakUpService;

    @Autowired
    private ObjectFilesService objectFilesService;

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Autowired
    private FileReferenceRepo fileReferenceRepo;

    @Autowired
    private AmazonClient s3Client;
    @Autowired
    private DaoEntityMapper mapper;
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private ObjectFilesRepo objectFilesRepo;

    public List<OrderItemDAO> createOrderItems(List<OrderItemDetailRequest> orderItemDetails, OrderDAO order) {
        Map<String, Long> clothRefOrderItemIdMap = new HashMap<>();
        Map<String, Long> audioRefOrderItemIdMap = new HashMap<>();
        List<PriceBreakupDAO> priceBreakUpList = new ArrayList<>();
        List<OrderItemDAO> orderItemList = Optional.ofNullable(order.getOrderItems()).orElse(new ArrayList<>());
        for (OrderItemDetailRequest itemDetail : orderItemDetails) {
            OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(itemDetail.getOutfitType());
            if (outfitType == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid outfit type " + itemDetail.getOutfitType());
            }

            MeasurementRevisionsDAO measurementRevisionsDAO = null;
            if (itemDetail.getMeasurementRevisionId() != null) {
                measurementRevisionsDAO = measurementService.getMeasurementRevisionById(itemDetail.getMeasurementRevisionId());
            }

            OrderItemDAO orderItemDAO = new OrderItemDAO(itemDetail.getTrialDate(), itemDetail.getDeliveryDate(),
                    itemDetail.getSpecialInstructions(), itemDetail.getOrderType(), outfitType,
                    itemDetail.getInspiration(), itemDetail.getIsPriorityOrder(), itemDetail.getItemQuantity(),
                    itemDetail.getOutfitAlias(), measurementRevisionsDAO, order);
            orderItemDAO = mapper.orderItemToOrderItemDAO(orderItemRepo.save(mapper.orderItemDAOToOrderItem(orderItemDAO,
                    new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
            orderItemList.add(orderItemDAO);
            List<PriceBreakupDAO> priceBreakupDAOList =
                    priceBreakUpService.generatePriceBreakupList(itemDetail.getPriceBreakup(), orderItemDAO);
            priceBreakUpList.addAll(priceBreakupDAOList);
            for (String imageRef : itemDetail.getClothImageReferenceIds()) {
                clothRefOrderItemIdMap.put(imageRef, orderItemDAO.getId());
            }
            for (String audioRef : itemDetail.getAudioReferenceIds()) {
                audioRefOrderItemIdMap.put(audioRef, orderItemDAO.getId());
            }
        }
        priceBreakUpService.savePriceBreakUp(priceBreakUpList);
        if (!CollectionUtils.isEmpty(clothRefOrderItemIdMap)) {
            objectFilesService.saveObjectImages(clothRefOrderItemIdMap, FileEntityType.ORDER_ITEM.getEntityType());
        }
        if (!CollectionUtils.isEmpty(audioRefOrderItemIdMap)) {
            objectFilesService.saveObjectImages(audioRefOrderItemIdMap, FileEntityType.AUDIO.getEntityType());
        }
        return orderItemList;
    }

    @Transactional
    public OrderItemDAO updateOrderItem(Long orderItemId, UpdateOrderItemDetailRequest updateItemDetail) {
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
        if (orderItem.isMeasurementRevisionUpdated(updateItemDetail.getMeasurementRevisionId())) {
            MeasurementRevisionsDAO measurementRevisionsDAO = measurementService.getMeasurementRevisionById(updateItemDetail.getMeasurementRevisionId());
            orderItem.setMeasurementRevision(measurementRevisionsDAO);
        }
        if (!Collections.isEmpty(updateItemDetail.getClothImageReferenceIds())) {
            objectFilesService.invalidateExistingReferenceIds(FileEntityType.ORDER_ITEM.getEntityType(),
                    orderItemId);
            objectFilesService.saveObjectImages(updateItemDetail.getClothImageReferenceIds(),
                    FileEntityType.ORDER_ITEM.getEntityType(), orderItemId);
        }
        if (!Collections.isEmpty(updateItemDetail.getAudioReferenceIds())) {
            objectFilesService.invalidateExistingReferenceIds(FileEntityType.AUDIO.getEntityType(),
                    orderItemId);
            objectFilesService.saveObjectImages(updateItemDetail.getClothImageReferenceIds(),
                    FileEntityType.AUDIO.getEntityType(), orderItemId);
        }
        if (!Collections.isEmpty(updateItemDetail.getPriceBreakup())) {
            List<PriceBreakupDAO> updatedPriceBreakup =
                    priceBreakUpService.updatePriceBreakups(updateItemDetail.getPriceBreakup(), orderItem);
            orderItem.setPriceBreakup(updatedPriceBreakup);
        }
        orderItem = mapper.orderItemToOrderItemDAO(orderItemRepo.save(mapper.orderItemDAOToOrderItem(orderItem,
                new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return orderItem;
    }

    public OrderItemDetails getOrderItemDetails(Long orderItemId) {
        Optional<OrderItem> orderItem = orderItemRepo.findById(orderItemId);
        if (!orderItem.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect order item id");
        }
        OrderItemDAO orderItemDAO = mapper.orderItemToOrderItemDAO(orderItem.get(), new CycleAvoidingMappingContext());
        return getOrderItemDetails(orderItemDAO);
    }

    public OrderItemDetails getOrderItemDetails(OrderItemDAO orderItem) {
        OutfitType outfitType = orderItem.getOutfitType();
        String outfitImageLink = outfitImageLinkService.getOutfitImageLink(outfitType);
        List<String> clothImagesReferenceIds = objectFilesService.getClothReferenceIds(orderItem.getId());
        List<String> clothImageUrlLinks = getClothProfilePicLink(clothImagesReferenceIds);
        OrderItemDetails orderItemDetails = new OrderItemDetails(clothImagesReferenceIds,
                clothImageUrlLinks, outfitImageLink, orderItem);
        return orderItemDetails;
    }

    private List<String> getClothProfilePicLink(List<String> clothImageReferenceId) {
        if (Collections.isEmpty(clothImageReferenceId)) {
            return new ArrayList<>();
        }
        List<ImageReference> clothImageReferences = fileReferenceRepo.findAllByReferenceIdIn(clothImageReferenceId);
        if (!Collections.isEmpty(clothImageReferences)) {
            List<ImageReferenceDAO> imageReferenceDAOs = CommonUtils.mapList(clothImageReferences,
                    mapper::imageReferenceToImageReferenceDAO);
            List<String> clothImageFileNames = imageReferenceDAOs.stream().map(imageRef ->
                    imageRef.getImageName()).collect(Collectors.toList());
            return s3Client.generateShortLivedUrls(clothImageFileNames);
        }
        return new ArrayList<>();
    }

}