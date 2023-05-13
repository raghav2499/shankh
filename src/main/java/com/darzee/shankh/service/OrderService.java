package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.entity.Order;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.enums.OrderStatus;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.CreateOrderRequest;
import com.darzee.shankh.request.UpdateOrderRequest;
import com.darzee.shankh.request.innerObjects.OrderAmountDetails;
import com.darzee.shankh.request.innerObjects.OrderDetails;
import com.darzee.shankh.request.innerObjects.UpdateOrderAmountDetails;
import com.darzee.shankh.request.innerObjects.UpdateOrderDetails;
import com.darzee.shankh.response.*;
import com.darzee.shankh.utils.CommonUtils;
import com.darzee.shankh.utils.pdfutils.BillGenerator;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private FilterOrderService filterOrderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OrderAmountRepo orderAmountRepo;
    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    @Autowired
    private OrderStateMachineService orderStateMachineService;

    @Autowired
    private ObjectImagesService objectImagesService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private BillGenerator billGenerator;

    @Autowired
    private MeasurementRepo measurementRepo;
    @Autowired
    private BoutiqueLedgerService boutiqueLedgerService;

    @Autowired
    private ObjectImagesRepo objectImagesRepo;

    @Autowired
    private ImageReferenceRepo imageReferenceRepo;

    @Autowired
    private AmazonClient s3Client;

    public ResponseEntity createOrderAndGenerateInvoice(CreateOrderRequest request) {
        CreateOrderResponse response = null;
        OrderSummary orderSummary = createNewOrder(request);
        if (orderSummary != null) {
            generateInvoice(orderSummary.getOrderId());
            String successMessage = "Order created successfully";
            response = new CreateOrderResponse(successMessage, orderSummary);
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        String failureMessage = "No eligible boutique/customer found";
        response = new CreateOrderResponse(failureMessage);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public OrderSummary createNewOrder(CreateOrderRequest request) {
        OrderDetails orderDetails = request.getOrderDetails();
        OrderAmountDetails orderAmountDetails = request.getOrderAmountDetails();
        Optional<Customer> optionalCustomer = customerRepo.findById(orderDetails.getCustomerId());
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(orderDetails.getBoutiqueId());
        if (optionalCustomer.isPresent() && optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(), new CycleAvoidingMappingContext());
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());

            OrderDAO orderDAO = setOrderSpecificDetails(orderDetails, boutiqueDAO, customerDAO);
            OrderAmountDAO orderAmountDAO = setOrderAmountSpecificDetails(orderAmountDetails, orderDAO);
            orderDAO.setOrderAmount(orderAmountDAO);
            orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext()));

            boutiqueRepo.save(mapper.boutiqueDaoToObject(boutiqueDAO, new CycleAvoidingMappingContext()));

            return new OrderSummary(orderDAO.getId(), orderDAO.getInvoiceNo(), orderDAO.getOutfitType().getName(),
                    orderDAO.getTrialDate().toString(), orderDAO.getDeliveryDate().toString(),
                    orderAmountDAO.getTotalAmount().toString(), orderAmountDAO.getAmountRecieved().toString());
        }
        return null;
    }

    public ResponseEntity<GetOrderResponse> getOrder(Map<String, Object> filterMap, Map<String, Object> pagingSortingMap) {
        Specification<Order> orderSpecification = OrderSpecificationClause.getSpecificationBasedOnFilters(filterMap);
        Pageable pagingCriteria = filterOrderService.getPagingCriteria(pagingSortingMap);
        List<Order> orderDetails = orderRepo.findAll(orderSpecification, pagingCriteria).getContent();
        List<OrderDAO> orderDAOList = Optional.ofNullable(orderDetails).orElse(new ArrayList<>()).stream().map(order -> mapper.orderObjectToDao(order, new CycleAvoidingMappingContext())).collect(Collectors.toList());
        List<OrderDetailResponse> orderDetailsList = orderDAOList.stream().map(order -> getOrderDetails(order)).collect(Collectors.toList());
        GetOrderResponse response = new GetOrderResponse(orderDetailsList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity getOrderDetails(Long orderId) throws Exception {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderDAO order = mapper.orderObjectToDao(optionalOrder.get(), new CycleAvoidingMappingContext());
            OrderAmountDAO orderAmount = order.getOrderAmount();
            OutfitType outfitType = order.getOutfitType();
            CustomerDAO customer = order.getCustomer();
            MeasurementDAO measurementDAO = customer.getMeasurement();
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
            OutfitMeasurementDetails measurementDetails = outfitTypeService.extractMeasurementDetails(measurementDAO);
            List<String> clothImagesReferenceIds = objectImagesService.getClothReferenceIds(order.getId());
            List<String> clothImageUrlLinks = getClothProfilePicLink(clothImagesReferenceIds);
            String message = "Details fetched succesfully";
            OrderDetailResponse response = new OrderDetailResponse(customer, order, measurementDetails, orderAmount, clothImageUrlLinks, message);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order ID");
    }

    public ResponseEntity getOrderInvoiceLink(Long orderId) {
        String link = bucketService.getInvoiceShortLivedLink(orderId);
        link = link.trim();
        GetInvoiceResponse response = new GetInvoiceResponse(link);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity updateOrder(Long orderId, UpdateOrderRequest request) {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderDAO order = mapper.orderObjectToDao(optionalOrder.get(), new CycleAvoidingMappingContext());
            OrderAmountDAO orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.findByOrderId(order.getId()), new CycleAvoidingMappingContext());
            UpdateOrderDetails orderDetails = request.getOrderDetails();
            UpdateOrderAmountDetails orderAmountDetails = request.getOrderAmountDetails();
            if (orderDetails != null) {
                order = updateOrderDetails(orderDetails, order);
            }
            if (orderAmountDetails != null) {
                orderAmountDAO = updateOrderAmountDetails(orderAmountDetails, orderAmountDAO, order.getId(),
                        order.getBoutique().getId());
            }

            OrderSummary orderSummary = new OrderSummary(order.getId(), order.getInvoiceNo(),
                    order.getOutfitType().getName(), order.getTrialDate().toString(), order.getDeliveryDate().toString(),
                    orderAmountDAO.getTotalAmount().toString(), orderAmountDAO.getAmountRecieved().toString());
            CreateOrderResponse response = new CreateOrderResponse("Order updated successfully", orderSummary);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID is invalid");
    }

    public String generateInvoice(Long orderId) {
        Optional<Order> order = orderRepo.findById(orderId);
        if (order.isPresent()) {
            OrderDAO orderDAO = mapper.orderObjectToDao(order.get(), new CycleAvoidingMappingContext());
            CustomerDAO customerDAO = orderDAO.getCustomer();
            String customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
            OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
            BoutiqueDAO boutique = orderDAO.getBoutique();
            File bill = billGenerator.generateBill(customerName, customerDAO.getPhoneNumber(), orderDAO, orderAmountDAO, boutique);
            String fileUploadUrl = bucketService.uploadInvoice(bill, orderId);
            bill.delete();
            return fileUploadUrl;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order id");
    }

    private OrderDAO updateOrderDetails(UpdateOrderDetails orderDetails, OrderDAO order) {
        if (order.isOrderStatusUpdated(orderDetails.getStatus())) {
            Integer targetStatusOrdinal = orderDetails.getStatus();
            OrderStatus initialStatus = order.getOrderStatus();
            OrderStatus targetStatus = OrderStatus.getOrderTypeEnumOrdinalMap().get(targetStatusOrdinal);
            if (targetStatus == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status " + targetStatusOrdinal + " is not valid status");
            }
            if (orderStateMachineService.isTransitionAllowed(order.getOrderStatus(), targetStatus)) {
                order.setOrderStatus(targetStatus);
                updateLedgerIfApplicable(order, initialStatus);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "State transition from "
                        + order.getOrderStatus() + " to " + targetStatus + " is not allowed");
            }
        }
        if (order.isTrialDateUpdated(orderDetails.getTrialDate())) {
            order.setTrialDate(orderDetails.getTrialDate());
        }
        if (order.isDeliveryDateUpdated(orderDetails.getDeliveryDate())) {
            order.setDeliveryDate(orderDetails.getDeliveryDate());
        }
        if (order.isPriorityUpdated(orderDetails.getIsPriorityOrder())) {
            order.setIsPriorityOrder(orderDetails.getIsPriorityOrder());
        }
        if (order.isInspirationUpdated(orderDetails.getInspiration())) {
            order.setInspiration(orderDetails.getInspiration());
        }
        if (order.areSpecialInstructionsUpdated(orderDetails.getSpecialInstructions())) {
            order.setSpecialInstructions(orderDetails.getSpecialInstructions());
        }
        if (!Collections.isEmpty(orderDetails.getClothImageReferenceIds())) {
            Long orderId = order.getId();
            objectImagesService.invalidateExistingReferenceIds(ImageEntityType.ORDER.getEntityType(), orderId);
            objectImagesService.saveObjectImages(orderDetails.getClothImageReferenceIds(), ImageEntityType.ORDER.getEntityType(), orderId);
        }
        order = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(order, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return order;
    }

    private OrderAmountDAO updateOrderAmountDetails(UpdateOrderAmountDetails orderAmountDetails,
                                                    OrderAmountDAO orderAmount, Long orderId, Long boutiqueId) {
        Double totalAmount = Optional.ofNullable(orderAmountDetails.getTotalOrderAmount()).orElse(orderAmount.getTotalAmount());
        Double advancePayment = Optional.ofNullable(orderAmountDetails.getAdvanceOrderAmount()).orElse(orderAmount.getAmountRecieved());

        if (orderAmount.getTotalAmount().equals(totalAmount)
                && orderAmount.getAmountRecieved().equals(advancePayment)) {
            return orderAmount;
        }

        if (advancePayment > totalAmount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount paid is greater than total amount. Amount Paid " + advancePayment + " and total amount " + totalAmount);
        }

        Double deltaTotalAmount = totalAmount - orderAmount.getTotalAmount();
        Double deltaPendingAmount = (totalAmount - advancePayment)
                - (orderAmount.getTotalAmount() - orderAmount.getAmountRecieved());
        orderAmount.setTotalAmount(totalAmount);
        orderAmount.setAmountRecieved(advancePayment);
        orderAmount = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmount, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());

        boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(deltaPendingAmount, deltaTotalAmount, boutiqueId);
        generateInvoice(orderId);

        return orderAmount;
    }

    private OrderDetailResponse getOrderDetails(OrderDAO orderDAO) {
        String customerProfilePicLink = customerService.getCustomerProfilePicLink(orderDAO.getCustomer().getId());
        return new OrderDetailResponse(orderDAO.getCustomer(), orderDAO, customerProfilePicLink, orderDAO.getOrderAmount());
    }

    private OrderDAO setOrderSpecificDetails(OrderDetails orderDetails, BoutiqueDAO boutiqueDAO, CustomerDAO customerDAO) {
        String invoiceNo = generateOrderInvoiceNo();
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(orderDetails.getOutfitType());
        if (outfitType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid outfit type");
        }
        OrderDAO orderDAO = new OrderDAO(orderDetails.getTrialDate(), orderDetails.getDeliveryDate(), outfitType, orderDetails.getSpecialInstructions(), orderDetails.getInspiration(), orderDetails.getOrderType(), invoiceNo, boutiqueDAO, customerDAO);
        orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        Long orderId = orderDAO.getId();
        List<String> clothImageReferenceIds = orderDetails.getClothImageReferenceIds();
        objectImagesService.saveObjectImages(clothImageReferenceIds, ImageEntityType.ORDER.getEntityType(), orderId);
        return orderDAO;
    }

    private OrderAmountDAO setOrderAmountSpecificDetails(OrderAmountDetails orderAmountDetails, OrderDAO orderDAO) {
        Double advanceRecieved = Optional.ofNullable(orderAmountDetails.getAdvanceOrderAmount()).orElse(0d);
        OrderAmountDAO orderAmountDAO = new OrderAmountDAO(orderAmountDetails.getTotalOrderAmount(), advanceRecieved, orderDAO);
        orderAmountDAO.setOrder(orderDAO);
        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return orderAmountDAO;
    }

    private List<String> getClothProfilePicLink(List<String> clothImageReferenceId) {
        if (Collections.isEmpty(clothImageReferenceId)) {
            return new ArrayList<>();
        }
        List<ImageReference> clothImageReferences = imageReferenceRepo.findAllByReferenceIdIn(clothImageReferenceId);
        if (!Collections.isEmpty(clothImageReferences)) {
            List<ImageReferenceDAO> imageReferenceDAOs = CommonUtils.mapList(clothImageReferences, mapper::imageReferenceToImageReferenceDAO);
            List<String> clothImageFileNames = imageReferenceDAOs.stream().map(imageRef -> imageRef.getImageName()).collect(Collectors.toList());
            return s3Client.generateShortLivedUrls(clothImageFileNames);
        }
        return new ArrayList<>();
    }

    private String generateOrderInvoiceNo() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    private void updateLedgerIfApplicable(OrderDAO order, OrderStatus initialStatus) {
        OrderStatus currentStatus = order.getOrderStatus();
        Long boutiqueId = order.getBoutique().getId();
        OrderAmountDAO orderAmountDAO = order.getOrderAmount();
        Double pendingAmount = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
        if (isOrderActivated(currentStatus, initialStatus)) {
            boutiqueLedgerService.updateBoutiqueLedgerOnStatusActivation(pendingAmount,
                    orderAmountDAO.getAmountRecieved(), boutiqueId);
        }
        if (isOrderClosed(currentStatus, initialStatus)) {
            boutiqueLedgerService.updateBoutiqueLedgerOnStatusClosure(pendingAmount,
                    orderAmountDAO.getAmountRecieved(), boutiqueId);
        }
    }

    private boolean isOrderActivated(OrderStatus currentStatus, OrderStatus initialStatus) {
        return Constants.ACTIVE_ORDER_STATUS_LIST.contains(currentStatus)
                && !Constants.ACTIVE_ORDER_STATUS_LIST.contains(initialStatus);
    }

    private boolean isOrderClosed(OrderStatus currentStatus, OrderStatus initialStatus) {
        return Constants.CLOSED_ORDER_STATUS_LIST.contains(currentStatus)
                && !Constants.CLOSED_ORDER_STATUS_LIST.contains(initialStatus);
    }
}
