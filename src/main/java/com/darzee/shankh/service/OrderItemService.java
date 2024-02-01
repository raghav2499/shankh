package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.entity.OrderItem;
import com.darzee.shankh.enums.FileEntityType;
import com.darzee.shankh.enums.OrderItemStatus;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.GetOrderDetailsRequest;
import com.darzee.shankh.request.innerObjects.OrderItemDetailRequest;
import com.darzee.shankh.response.GetOrderItemResponse;
import com.darzee.shankh.response.OrderItemDetails;
import com.darzee.shankh.utils.CommonUtils;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private StitchOptionService stitchOptionService;

    @Autowired
    private BoutiqueLedgerService ledgerService;

    @Autowired
    private OrderItemStateMachineService orderItemStateMachineService;

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
    private FilterOrderService filterOrderService;

    @Autowired
    private AmazonClient s3Client;
    @Autowired
    private DaoEntityMapper mapper;
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private ObjectFilesRepo objectFilesRepo;
    @Autowired
    private OrderRepo orderRepo;

    @Transactional
    public List<OrderItemDAO> createOrderItems(List<OrderItemDetailRequest> orderItemDetails, OrderDAO order) {
        Map<String, Long> clothRefOrderItemIdMap = new HashMap<>();
        Map<String, Long> audioRefOrderItemIdMap = new HashMap<>();
        List<PriceBreakupDAO> priceBreakUpList = new ArrayList<>();
        List<OrderItemDAO> orderItemList = Optional.ofNullable(order.getNonDeletedItems()).orElse(new ArrayList<>());
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
            List<PriceBreakupDAO> priceBreakupDAOList =
                    priceBreakUpService.generatePriceBreakupList(itemDetail.getPriceBreakup(), orderItemDAO);
            priceBreakUpList.addAll(priceBreakupDAOList);
            if (!CollectionUtils.isEmpty(itemDetail.getClothImageReferenceIds())) {
                for (String imageRef : itemDetail.getClothImageReferenceIds()) {
                    clothRefOrderItemIdMap.put(imageRef, orderItemDAO.getId());
                }
            }
            if (!CollectionUtils.isEmpty(itemDetail.getAudioReferenceIds())) {
                for (String audioRef : itemDetail.getAudioReferenceIds()) {
                    audioRefOrderItemIdMap.put(audioRef, orderItemDAO.getId());
                }
            }
            if (!CollectionUtils.isEmpty(itemDetail.getStitchOptionReferences())) {
                stitchOptionService.addOrderItemId(itemDetail.getStitchOptionReferences(), orderItemDAO.getId());
            }
            orderItemDAO.setPriceBreakup(priceBreakUpList);
            orderItemList.add(orderItemDAO);
        }
        if (!CollectionUtils.isEmpty(priceBreakUpList)) {
            priceBreakUpService.savePriceBreakUp(priceBreakUpList);
        }
        if (!CollectionUtils.isEmpty(clothRefOrderItemIdMap)) {
            objectFilesService.saveObjectImages(clothRefOrderItemIdMap, FileEntityType.ORDER_ITEM.getEntityType());
        }
        if (!CollectionUtils.isEmpty(audioRefOrderItemIdMap)) {
            objectFilesService.saveObjectImages(audioRefOrderItemIdMap, FileEntityType.AUDIO.getEntityType());
        }
        order.setOrderItems(orderItemList);
        return orderItemList;
    }

    @Transactional
    public OrderItemDAO updateOrderItem(Long orderItemId, OrderItemDetailRequest updateItemDetail) {
        OrderItemDAO orderItem = null;
        Optional<OrderItem> orderItemOb = orderItemRepo.findById(orderItemId);
        if (orderItemOb.isPresent()) {
            orderItem = mapper.orderItemToOrderItemDAO(orderItemOb.get(), new CycleAvoidingMappingContext());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item ID is invalid");
        }
        if (orderItem.isStatusUpdated(updateItemDetail.getItemStatus())) {
            OrderItemStatus status = OrderItemStatus.getOrderItemTypeEnumOrdinalMap().get(updateItemDetail.getItemStatus());
            OrderItemStatus initialState = orderItem.getOrderItemStatus();
            orderItemStateMachineService.isTransitionAllowed(initialState, status);
            orderItem.setOrderItemStatus(status);
            ledgerService.handleBoutiqueLedgerOnOrderItemUpdation(orderItem.getOrder().getBoutique().getId(), initialState, status);
        }
        if (Boolean.TRUE.equals(updateItemDetail.getIsDeleted())) {
            orderItem.setIsDeleted(Boolean.TRUE);
            ledgerService.handleBoutiqueLedgerOnOrderItemDeletion(orderItem.getOrder().getBoutique().getId(), orderItem);
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

    public ResponseEntity<GetOrderItemResponse> getOrderItemDetails(Long boutiqueId, Long orderId, String orderItemStatusList,
                                                                    Boolean priorityOrdersOnly, String sortKey,
                                                                    Integer countPerPage, Integer pageCount, String deliveryDateFrom,
                                                                    String deliveryDateTill) {
        validateGetOrderItemRequest(boutiqueId, orderId);
        Map<String, Object> filterMap = GetOrderDetailsRequest.getFilterMap(boutiqueId, orderItemStatusList,
                priorityOrdersOnly, null, deliveryDateFrom, deliveryDateTill, orderId);
        Map<String, Object> pagingCriteriaMap = GetOrderDetailsRequest.getPagingCriteria(countPerPage, pageCount, sortKey);
        Specification<OrderItem> orderItemSpecification = OrderItemSpecificationClause.getSpecificationBasedOnFilters(filterMap);
        Pageable pagingCriteria = filterOrderService.getPagingCriteria(pagingCriteriaMap);
        List<OrderItem> orderItems = orderItemRepo.findAll(orderItemSpecification, pagingCriteria).getContent();
        List<OrderItemDAO> orderItemDAOs = mapper.orderItemListToOrderItemDAOList(orderItems, new CycleAvoidingMappingContext());
        List<OrderItemDetails> orderItemDetails = Optional.ofNullable(orderItemDAOs).orElse(new ArrayList<>()).stream()
                .map(orderItem -> new OrderItemDetails(orderItem)).collect(Collectors.toList());
        GetOrderItemResponse response = new GetOrderItemResponse(orderItemDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
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

    private void validateGetOrderItemRequest(Long boutiqueId, Long orderId) {
        if (boutiqueId == null && orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either Order ID or Boutique ID is mandatory");
        }
    }

    public boolean validateMandatoryOrderItemFields(List<OrderItemDAO> orderItems) {
        for (OrderItemDAO orderItem : orderItems) {
            orderItem.mandatoryFieldsPresent();
        }
        return true;
    }

    public void acceptOrderItems(List<OrderItemDAO> orderItems) {
        orderItems.forEach(orderItem -> orderItem.setOrderItemStatus(OrderItemStatus.ACCEPTED));
        orderItemRepo.saveAll(mapper.orderItemDAOListToOrderItemList(orderItems, new CycleAvoidingMappingContext()));
    }

}