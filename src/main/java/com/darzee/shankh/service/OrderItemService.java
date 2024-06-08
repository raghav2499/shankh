package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.constants.ErrorMessages;
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
import com.darzee.shankh.response.*;
import com.darzee.shankh.service.translator.ErrorMessageTranslator;
import com.darzee.shankh.service.translator.OrderItemDetailsTranslator;
import com.darzee.shankh.service.translator.SuccessMessageTranslator;

import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private StitchOptionService stitchOptionService;

    @Autowired
    private OrderItemDetailsTranslator orderItemDetailsTranslator;

    @Autowired
    private BoutiqueLedgerService ledgerService;

    @Autowired
    private OrderItemStateMachineService orderItemStateMachineService;

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private StitchOptionsRepo stitchOptionsRepo;

    @Autowired
    private PriceBreakUpService priceBreakUpService;

    @Autowired
    private ObjectFilesService objectFilesService;

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

    @Autowired
    private PriceBreakUpRepo priceBrekupRepo; 

    @Autowired
    private SuccessMessageTranslator successMessageTranslator;

    @Autowired
    private ErrorMessageTranslator errorMessageTranslator;

    @Transactional
    public List<OrderItemDAO> createOrderItems(List<OrderItemDetailRequest> orderItemDetails, OrderDAO order) {
        Map<String, Long> clothRefOrderItemIdMap = new HashMap<>();
        Map<String, Long> audioRefOrderItemIdMap = new HashMap<>();
        List<OrderItemDAO> orderItemList = Optional.ofNullable(order.getNonDeletedItems()).orElse(new ArrayList<>());

        for (OrderItemDetailRequest itemDetail : orderItemDetails) {
            OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(itemDetail.getOutfitType());
            if (outfitType == null) {
                String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_OUTFIT_TYPE_ERROR)+itemDetail.getOutfitType();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
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
            orderItemDAO.setPriceBreakup(priceBreakupDAOList);

            if (!CollectionUtils.isEmpty(itemDetail.getClothImageReferenceIds())) {
                for (String imageRef : itemDetail.getClothImageReferenceIds()) {
                    clothRefOrderItemIdMap.put(imageRef, orderItemDAO.getId());
                }
                List<FileDetail> clothImageFileDetails = getClothImageDetails(itemDetail.getClothImageReferenceIds());
                orderItemDAO.setClothImageDetails(clothImageFileDetails);
            }
            if (!CollectionUtils.isEmpty(itemDetail.getAudioReferenceIds())) {
                for (String audioRef : itemDetail.getAudioReferenceIds()) {
                    audioRefOrderItemIdMap.put(audioRef, orderItemDAO.getId());
                }
                List<FileDetail> audioFileDetails = getAudioDetails(itemDetail.getAudioReferenceIds());
                orderItemDAO.setAudioFileDetails(audioFileDetails);
            }
            if (!CollectionUtils.isEmpty(itemDetail.getStitchOptionReferences())) {
                stitchOptionService.addOrderItemId(itemDetail.getStitchOptionReferences(), orderItemDAO.getId());
            }
            orderItemList.add(orderItemDAO);
        }

        if (!CollectionUtils.isEmpty(clothRefOrderItemIdMap)) {
            objectFilesService.saveObjectFiles(clothRefOrderItemIdMap, FileEntityType.ORDER_ITEM.getEntityType());
        }
        if (!CollectionUtils.isEmpty(audioRefOrderItemIdMap)) {
            objectFilesService.saveObjectFiles(audioRefOrderItemIdMap, FileEntityType.AUDIO.getEntityType());
        }
        order.setOrderItems(orderItemList);
        return orderItemList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public OrderItemDAO updateOrderItem(Long orderItemId, OrderItemDetailRequest updateItemDetail) {
        OrderItemDAO orderItem = null;
        Optional<OrderItem> orderItemOb = orderItemRepo.findById(orderItemId);
        if (orderItemOb.isPresent()) {
            orderItem = mapper.orderItemToOrderItemDAO(orderItemOb.get(), new CycleAvoidingMappingContext());
        } else {
            String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_ORDER_ID_ERROR);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        if (orderItem.isStatusUpdated(updateItemDetail.getItemStatus())) {
            OrderItemStatus status = OrderItemStatus.getOrderItemStatusEnumOrdinalMap().get(updateItemDetail.getItemStatus());
            OrderItemStatus initialState = orderItem.getOrderItemStatus();
            orderItemStateMachineService.isTransitionAllowed(initialState, status);
            orderItem.setOrderItemStatus(status);
        }

        if (Boolean.TRUE.equals(updateItemDetail.getIsDeleted())) {
            orderItem.setIsDeleted(Boolean.TRUE);
            Double itemPrice = orderItem.calculateItemPrice();
            if(updateItemDetail.getAmountRefunded() != null && updateItemDetail.getAmountRefunded() > itemPrice) {
                throw new RuntimeException("Amount refunded cannot exceed item price");
            }
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
            objectFilesService.saveObjectFiles(updateItemDetail.getClothImageReferenceIds(),
                    FileEntityType.ORDER_ITEM.getEntityType(), orderItemId);
        } else {
            objectFilesService.invalidateExistingReferenceIds(FileEntityType.ORDER_ITEM.getEntityType(),
                    orderItemId);
        }

        if (!Collections.isEmpty(updateItemDetail.getAudioReferenceIds())) {
            objectFilesService.invalidateExistingReferenceIds(FileEntityType.AUDIO.getEntityType(),
                    orderItemId);
            objectFilesService.saveObjectFiles(updateItemDetail.getAudioReferenceIds(),
                    FileEntityType.AUDIO.getEntityType(), orderItemId);
        } else {
            objectFilesService.invalidateExistingReferenceIds(FileEntityType.AUDIO.getEntityType(),
                    orderItemId);
        }
        orderItem = mapper.orderItemToOrderItemDAO(orderItemRepo.save(mapper.orderItemDAOToOrderItem(orderItem, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());

        return orderItem;
    }

    public OrderItemDetails getOrderItemDetails(Long orderItemId) throws Exception {
        Optional<OrderItem> orderItem = orderItemRepo.findById(orderItemId);
        if (!orderItem.isPresent()) {
            String errorMessage =errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_ORDER_ITEM_ID_ERROR);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        OrderItemDAO orderItemDAO = mapper.orderItemToOrderItemDAO(orderItem.get(), new CycleAvoidingMappingContext());
        
        OrderItemDetails orderItemDetails = getOrderItemDetails(orderItemDAO);        
        return orderItemDetails;
    }

    public ResponseEntity<GetOrderItemResponse> getOrderItemDetails(Long boutiqueId, Long orderId, Long customerId, String orderItemStatusList,
                                                                    Boolean priorityOrdersOnly, String sortKey,
                                                                    String sortOrder, Integer countPerPage, Integer pageCount, String deliveryDateFrom,
                                                                    String deliveryDateTill) {
        validateGetOrderItemRequest(boutiqueId, orderId);
        Map<String, Object> filterMap = GetOrderDetailsRequest.getFilterMap(boutiqueId, orderItemStatusList, null, priorityOrdersOnly, customerId, deliveryDateFrom, deliveryDateTill, orderId, null);
        Map<String, Object> pagingCriteriaMap = GetOrderDetailsRequest.getPagingCriteria(countPerPage, pageCount, sortKey, sortOrder);
        Specification<OrderItem> orderItemSpecification = OrderItemSpecificationClause.getSpecificationBasedOnFilters(filterMap);
        Pageable pagingCriteria = filterOrderService.getPagingCriteria(pagingCriteriaMap);
        List<OrderItem> orderItems = orderItemRepo.findAll(orderItemSpecification, pagingCriteria).getContent();
        Long totalCount = orderItemRepo.count(orderItemSpecification);
        List<OrderItemDAO> orderItemDAOs = mapper.orderItemListToOrderItemDAOList(orderItems, new CycleAvoidingMappingContext());
        List<OrderItemDetails> orderItemDetails = Optional.ofNullable(orderItemDAOs).orElse(new ArrayList<>()).stream().map(orderItem -> {
            try {
                OrderItemDetails newOrderItemDetails = new OrderItemDetails(orderItem,
                        outfitTypeObjectService.getOutfitTypeObject(orderItem.getOutfitType()).getOutfitImageLink());
                return orderItemDetailsTranslator.translate(newOrderItemDetails);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        GetOrderItemResponse response = new GetOrderItemResponse(orderItemDetails, totalCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public OrderItemDetails getOrderItemDetails(OrderItemDAO orderItem) throws Exception {

        OutfitType outfitType = orderItem.getOutfitType();
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
        String outfitImageLink = outfitTypeService.getOutfitImageLink();

        List<String> clothImagesReferenceIds = objectFilesService.getClothReferenceIds(orderItem.getId());
        List<FileDetail> clothImageFileDetails = getClothImageDetails(clothImagesReferenceIds);

        List<FileDetail> audioFileDetails = getAudioFileDetails(orderItem.getId());

        Long customerId = orderItem.getOrder().getCustomer().getId();
        OverallMeasurementDetails overallMeasurementDetails = measurementService.getMeasurementDetails(customerId, null, orderItem.getId(), outfitType.getOrdinal(), null, true);

        Map<String, List<OrderStitchOptionDetail>> orderItemStitchOptions = stitchOptionService.getOrderItemStitchOptions(orderItem.getId());

        OrderItemDetails orderItemDetails = new OrderItemDetails(clothImageFileDetails, audioFileDetails, overallMeasurementDetails, orderItemStitchOptions, outfitImageLink, orderItem);
        return orderItemDetails;
    }

    public List<FileDetail> getAudioFileDetails(Long orderItemId) {
        List<String> audioReferenceIds = objectFilesService.getAudioReferenceIds(orderItemId);
        List<FileDetail> audioFileDetails = getAudioDetails(audioReferenceIds);
        return audioFileDetails;
    }

    public List<String> getAudioInstructionLinks(Long orderItemId) {
        List<FileDetail> audioFiles = getAudioFileDetails(orderItemId);
        return audioFiles.stream().map(fileDetail -> fileDetail.getShortLivedUrl()).collect(Collectors.toList());
    }

    public List<String> getClothImageLinks(Long orderItemId) {
        List<String> clothImagesReferenceIds = objectFilesService.getClothReferenceIds(orderItemId);
        List<FileDetail> clothImageFileDetails = getClothImageDetails(clothImagesReferenceIds);
        return clothImageFileDetails.stream().map(fileDetail -> fileDetail.getShortLivedUrl()).collect(Collectors.toList());
    }

    private List<FileDetail> getClothImageDetails(List<String> clothImageRefIds) {
        List<FileDetail> fileDetails = new ArrayList<>();
        if (Collections.isEmpty(clothImageRefIds)) {
            return fileDetails;
        }

        for (String clothImageRefId : clothImageRefIds) {
            Optional<ImageReference> imageRef = fileReferenceRepo.findByReferenceId(clothImageRefId);
            if (!imageRef.isPresent()) {
                continue;
            }
            ImageReferenceDAO clothImageReferences = mapper.imageReferenceToImageReferenceDAO(imageRef.get());
            String url = s3Client.generateShortLivedUrl(clothImageReferences.getImageName(), false);
            FileDetail fileDetail = new FileDetail(clothImageRefId, url);
            fileDetails.add(fileDetail);
        }
        return fileDetails;
    }

    private List<FileDetail> getAudioDetails(List<String> audioFileRefIds) {
        List<FileDetail> fileDetails = new ArrayList<>();
        if (Collections.isEmpty(audioFileRefIds)) {
            return fileDetails;
        }

        for (String audioFileRefId : audioFileRefIds) {
            ImageReferenceDAO audioFileReference = mapper.imageReferenceToImageReferenceDAO(fileReferenceRepo.findByReferenceId(audioFileRefId).get());
            String url = s3Client.generateShortLivedUrlForAudio(audioFileReference.getImageName());
            FileDetail fileDetail = new FileDetail(audioFileRefId, url);
            fileDetails.add(fileDetail);
        }
        return fileDetails;
    }

    private void validateGetOrderItemRequest(Long boutiqueId, Long orderId) {
        if (boutiqueId == null && orderId == null) {
            final String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_ORDER_ID_ERROR);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
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