package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.*;
import com.darzee.shankh.enums.*;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.CreateOrderRequest;
import com.darzee.shankh.request.PriceBreakUpDetails;
import com.darzee.shankh.request.RecievePaymentRequest;
import com.darzee.shankh.request.UpdateOrderRequest;
import com.darzee.shankh.request.innerObjects.*;
import com.darzee.shankh.request.innerObjects.OrderAmountDetails;
import com.darzee.shankh.response.*;
import com.darzee.shankh.utils.CommonUtils;
import com.darzee.shankh.utils.pdfutils.BillGenerator;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.File;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@Service
public class OrderService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private FilterOrderService filterOrderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PaymentService paymentService;

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
    private OutfitImageLinkService outfitImageLinkService;

    @Autowired
    private ObjectImagesService objectImagesService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private BillGenerator billGenerator;
    @Autowired
    private BoutiqueLedgerService boutiqueLedgerService;

    @Autowired
    private PriceBreakUpService priceBreakUpService;

    @Autowired
    private ImageReferenceRepo imageReferenceRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

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
        List<PriceBreakUpDetails> allItemsPriceBreakUpDetails = orderDetails.getOrderItems()
                .stream().map(orderItem -> orderItem.getPriceBreakup()).flatMap(List::stream)
                .collect(Collectors.toList());
        validatePriceBreakup(allItemsPriceBreakUpDetails, orderAmountDetails.getTotalOrderAmount());

        Optional<Customer> optionalCustomer = customerRepo.findById(orderDetails.getCustomerId());
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(orderDetails.getBoutiqueId());
        if (optionalCustomer.isPresent() && optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(), new CycleAvoidingMappingContext());
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());

            OrderDAO orderDAO = setOrderSpecificDetails(orderDetails, boutiqueDAO, customerDAO);
            OrderAmountDAO orderAmountDAO = setOrderAmountSpecificDetails(orderAmountDetails, orderDAO);
            orderDAO.setOrderAmount(orderAmountDAO);
            orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext()));
            return new OrderSummary(orderDAO.getId(), orderDAO.getInvoiceNo(),
                    orderAmountDAO.getTotalAmount().toString(), orderAmountDAO.getAmountRecieved().toString(),
                    orderDAO.getOrderItems());
        }
        return null;
    }

    public ResponseEntity<GetOrderResponse> getOrder(Map<String, Object> filterMap, Map<String, Object> pagingSortingMap) {
        Specification<Order> orderSpecification = OrderSpecificationClause.getSpecificationBasedOnFilters(filterMap);
        Pageable pagingCriteria = filterOrderService.getPagingCriteria(pagingSortingMap);
        List<Order> orderDetails = orderRepo.findAll(orderSpecification, pagingCriteria).getContent();
        List<OrderDAO> orderDAOList = Optional.ofNullable(orderDetails).orElse(new ArrayList<>())
                .stream()
                .map(order -> mapper.orderObjectToDao(order, new CycleAvoidingMappingContext()))
                .collect(Collectors.toList());
        List<OrderDetailResponse> orderDetailsList = orderDAOList.stream()
                .map(order -> getOrderDetails(order))
                .collect(Collectors.toList());
        GetOrderResponse response = new GetOrderResponse(orderDetailsList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity getOrderDetails(Long orderId) throws Exception {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderDAO order = mapper.orderObjectToDao(optionalOrder.get(), new CycleAvoidingMappingContext());
            OrderAmountDAO orderAmount = order.getOrderAmount();
            CustomerDAO customer = order.getCustomer();
            List<OrderItemDetails> orderItemDetailsList = new ArrayList<>();
            for (OrderItemDAO orderItem : order.getOrderItems()) {
                OutfitType outfitType = orderItem.getOutfitType();
                String outfitImageLink = outfitImageLinkService.getOutfitImageLink(outfitType);
                List<String> clothImagesReferenceIds = objectImagesService.getClothReferenceIds(order.getId());
                List<String> clothImageUrlLinks = getClothProfilePicLink(clothImagesReferenceIds);
                OrderItemDetails orderItemDetails = new OrderItemDetails(clothImagesReferenceIds,
                        clothImageUrlLinks, outfitImageLink, orderItem);
                orderItemDetailsList.add(orderItemDetails);
            }
            String message = "Details fetched succesfully";
            OrderDetailResponse response = new OrderDetailResponse(customer, order, orderAmount,
                    orderItemDetailsList, message);
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
                orderAmountDAO = updateOrderAmountDetails(orderAmountDetails, orderAmountDAO, order,
                        order.getBoutique().getId());
            }
            List<PriceBreakupDAO> priceBreakupDAOList = order.getOrderItems().stream()
                    .map(OrderItemDAO::getPriceBreakup).flatMap(List::stream).collect(Collectors.toList());
            postUpdateOrderValidation(orderAmountDAO.getTotalAmount(), priceBreakupDAOList);
            OrderSummary orderSummary = new OrderSummary(order.getId(), order.getInvoiceNo(),
                    orderAmountDAO.getTotalAmount().toString(), orderAmountDAO.getAmountRecieved().toString(),
                    order.getOrderItems());
            CreateOrderResponse response = new CreateOrderResponse("Order updated successfully", orderSummary);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID is invalid");
    }


    public SalesDashboard getWeekWiseSales(Long boutiqueId, int month, int year) {
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate nextMonthStart = monthStart.plusMonths(1);
        List<Object[]> weekwiseSalesData = orderRepo.getTotalAmountByWeek(boutiqueId, monthStart, nextMonthStart);
        List<WeekwiseSalesSplit> weeklySalesAmount = weekwiseSalesData.stream()
                .map(weeklySalesData -> new WeekwiseSalesSplit((Double) weeklySalesData[0], (Date) weeklySalesData[1]))
                .collect(Collectors.toList());
        populateSalesIfRequired(weeklySalesAmount);
        return new SalesDashboard(weeklySalesAmount);
    }

    public List<OrderTypeDashboardData> getOrderTypeWiseSales(Long boutiqueId, int month, int year) {
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate nextMonthStart = monthStart.plusMonths(1);
        List<Object[]> orderTypeWiseSalesData = orderRepo.getTotalAmountByOrderType(boutiqueId, monthStart, nextMonthStart);
        Map<OrderType, Double> orderTypeAmountMap = new HashMap<>(OrderType.values().length);
        for (Object[] orderTypeSales : orderTypeWiseSalesData) {
            Integer orderTypeDbOrdinal = (Integer) orderTypeSales[1];
            OrderType orderType = OrderType.values()[orderTypeDbOrdinal];
            orderTypeAmountMap.put(orderType, (Double) orderTypeSales[0]);
        }
        List<OrderTypeDashboardData> orderTypeDashboardData = new ArrayList<>();
        for (OrderType orderType : OrderType.values()) {
            OrderTypeDashboardData orderTypeData = null;
            if (orderTypeAmountMap.containsKey(orderType)) {
                orderTypeData = new OrderTypeDashboardData(orderType.getDisplayName(),
                        orderTypeAmountMap.get(orderType));
            } else {
                orderTypeData = new OrderTypeDashboardData(orderType.getDisplayName(),
                        0d);
            }
            orderTypeDashboardData.add(orderTypeData);
        }
        return orderTypeDashboardData;
    }

    public List<TopCustomerData> getTopCustomerData(Long boutiqueId, int month, int year) {
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate nextMonthStart = monthStart.plusMonths(1);
        List<Object[]> topCustomerSalesDetails = orderRepo.getTopCustomersByTotalAmount(boutiqueId, monthStart, nextMonthStart);
        List<TopCustomerData> topCustomerDataList = new ArrayList<>(2);
        for (Object[] customerSalesDetails : topCustomerSalesDetails) {
            Long customerId = ((BigInteger) customerSalesDetails[1]).longValue();
            Double salesByCustomer = (Double) customerSalesDetails[0];
            CustomerDetails customerDetails = customerService.getCustomerDetails(customerId);
            TopCustomerData topCustomerData = new TopCustomerData(customerDetails.getCustomerId(),
                    customerDetails.getCustomerName(),
                    customerDetails.getPhoneNumber(),
                    salesByCustomer,
                    customerService.getCustomerProfilePicLink(customerId));
            topCustomerDataList.add(topCustomerData);
        }
        return topCustomerDataList;
    }


    @Transactional
    public ResponseEntity recieveOrderPayment(Long orderId, RecievePaymentRequest request) {
        Optional<Order> order = orderRepo.findById(orderId);
        if (order.isPresent()) {
            OrderDAO orderDAO = mapper.orderObjectToDao(order.get(), new CycleAvoidingMappingContext());
            OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
            Double pendingAmount = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
            Double amountRecieved = request.getAmount();
            if (amountRecieved > pendingAmount) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Amount recieved is greater than pending order amount");
            }

            orderAmountDAO.setAmountRecieved(orderAmountDAO.getAmountRecieved() + amountRecieved);
            orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO,
                    new CycleAvoidingMappingContext()));

            boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(-amountRecieved,
                    amountRecieved,
                    orderDAO.getBoutique().getId());
            String message = "Order payment recorded successfully";
            Double pendingAmountLeft = pendingAmount - amountRecieved;

            PaymentMode paymentMode = PaymentMode.getPaymentTypeEnumOrdinalMap().get(request.getPaymentMode());
            paymentService.recordPayment(amountRecieved, paymentMode, Boolean.FALSE, orderDAO);
            generateInvoice(orderDAO.getId());

            RecievePaymentResponse response = new RecievePaymentResponse(message,
                    orderId,
                    pendingAmountLeft);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Order Id");
    }

    public String generateInvoice(Long orderId) {
        Optional<Order> order = orderRepo.findById(orderId);
        if (order.isPresent()) {
            OrderDAO orderDAO = mapper.orderObjectToDao(order.get(), new CycleAvoidingMappingContext());
            CustomerDAO customerDAO = orderDAO.getCustomer();
            String customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
            OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
            BoutiqueDAO boutique = orderDAO.getBoutique();
            File bill = billGenerator.generateBill(customerName,
                    customerDAO.getPhoneNumber(),
                    orderDAO,
                    orderAmountDAO,
                    boutique);
            String fileUploadUrl = bucketService.uploadInvoice(bill, orderId);
            bill.delete();
            return fileUploadUrl;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order id");
    }

    @Transactional
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

        if (Boolean.TRUE.equals(orderDetails.getDeleteOrder())) {
            order.setIsDeleted(Boolean.TRUE);
            OrderStage orderStage = getOrderStage(order.getOrderStatus());
            resetOrderAmountForDeletedOrders(order, orderStage);
        }

        Map<Long, OrderItemDAO> orderItemDAOMap = order.getOrderItemDAOMap();
        List<OrderItemDAO> updatedItems = new ArrayList<>();
        for (UpdateOrderItemDetails updateItemDetail : orderDetails.getOrderItemDetails()) {
            Long orderItemId = updateItemDetail.getId();
            if (!orderItemDAOMap.containsKey(orderItemId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item ID is invalid");
            }
            OrderItemDAO orderItem = orderItemDAOMap.get(orderItemId);
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
            updatedItems.add(orderItem);
            if (!Collections.isEmpty(updateItemDetail.getClothImageReferenceIds())) {
                objectImagesService.invalidateExistingReferenceIds(ImageEntityType.ORDER_ITEM.getEntityType(),
                        orderItemId);
                objectImagesService.saveObjectImages(updateItemDetail.getClothImageReferenceIds(),
                        ImageEntityType.ORDER_ITEM.getEntityType(), orderItemId);
            }
            if (!Collections.isEmpty(updateItemDetail.getPriceBreakupDetails())) {
                List<PriceBreakupDAO> updatedPriceBreakup = priceBreakUpService.updatePriceBreakups(updateItemDetail.getPriceBreakupDetails(), orderItem);
                orderItem.setPriceBreakup(updatedPriceBreakup);
            }
        }
        //todo : check here if the updated object is also getting saved
        order = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(order,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return order;
    }

    @Transactional
    private OrderAmountDAO updateOrderAmountDetails(UpdateOrderAmountDetails orderAmountDetails,
                                                    OrderAmountDAO orderAmount, OrderDAO order, Long boutiqueId) {
        Double totalAmount = Optional.ofNullable(orderAmountDetails.getTotalOrderAmount()).orElse(orderAmount.getTotalAmount());
        Double advancePaid = getAdvancePaid(order);
        Double totalAmountPaid = getTotalAmountPaid(order);
        Double advancePayment = Optional.ofNullable(orderAmountDetails.getAdvanceOrderAmount()).orElse(advancePaid);
        if (orderAmount.getTotalAmount().equals(totalAmount)
                && advancePaid.equals(advancePayment)) {
            return orderAmount;
        }

        Double deltaTotalAmount = totalAmount - orderAmount.getTotalAmount();
        Double amountPaidAfterThisUpdate = totalAmountPaid + (advancePayment - advancePaid);

        if (amountPaidAfterThisUpdate > totalAmount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount paid is greater than total amount. Amount Paid "
                    + amountPaidAfterThisUpdate
                    + " and total amount " + totalAmount);
        }
        Double deltaPendingAmount = (totalAmount - advancePayment) - (orderAmount.getTotalAmount() - advancePaid);
        orderAmount.setTotalAmount(totalAmount);
        orderAmount.setAmountRecieved(amountPaidAfterThisUpdate);
        orderAmount = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmount, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());

        if (advancePaid != advancePayment) {
            paymentService.updateAdvancePayment(order, advancePayment);
        }

        Double deltaAmountRecieved = amountPaidAfterThisUpdate - totalAmountPaid;
        boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(deltaPendingAmount, deltaAmountRecieved, boutiqueId);

        generateInvoice(order.getId());

        return orderAmount;
    }

    private Double getAdvancePaid(OrderDAO orderDAO) {
        Double advancePaid = 0d;
        advancePaid = orderDAO.getPayment().stream()
                .filter(paymentDAO -> Boolean.TRUE.equals(paymentDAO.getIsAdvancePayment()))
                .mapToDouble(PaymentDAO::getAmount)
                .sum();
        return advancePaid;
    }

    private Double getTotalAmountPaid(OrderDAO orderDAO) {
        Double totalAmountPaid = 0d;
        totalAmountPaid = orderDAO.getPayment().stream()
                .mapToDouble(PaymentDAO::getAmount)
                .sum();
        return totalAmountPaid;
    }

    private OrderDetailResponse getOrderDetails(OrderDAO orderDAO) {
        String customerProfilePicLink = customerService.getCustomerProfilePicLink(orderDAO.getCustomer().getId());
        List<Pair<OrderItemDAO, String>> orderItemOutfitLinkPairList = new ArrayList<>();
        for (OrderItemDAO orderItem : orderDAO.getOrderItems()) {
            String outfitImgLink = outfitImageLinkService.getOutfitImageLink(orderItem.getOutfitType());
            orderItemOutfitLinkPairList.add(Pair.of(orderItem, outfitImgLink));
        }

        return new OrderDetailResponse(orderDAO, orderItemOutfitLinkPairList, customerProfilePicLink);
    }

    @Transactional(REQUIRES_NEW)
    private OrderDAO setOrderSpecificDetails(OrderDetails orderDetails, BoutiqueDAO boutiqueDAO,
                                             CustomerDAO customerDAO) {

        String invoiceNo = generateOrderInvoiceNo();
        OrderDAO orderDAO = new OrderDAO(invoiceNo, boutiqueDAO, customerDAO);
        orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO,
                new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());

        List<OrderItemDetailRequest> orderItems = orderDetails.getOrderItems();
        Map<String, Long> clothRefOrderItemIdMap = new HashMap<>();
        List<PriceBreakupDAO> priceBreakUpList = new ArrayList<>();
        List<OrderItemDAO> orderItemList = new ArrayList<>();
        for (OrderItemDetailRequest itemDetail : orderItems) {
            OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(itemDetail.getOutfitType());
            if (outfitType == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid outfit type " + itemDetail.getOutfitType());
            }
            OrderItemDAO orderItemDAO = new OrderItemDAO(itemDetail.getTrialDate(), itemDetail.getDeliveryDate(),
                    itemDetail.getSpecialInstructions(), itemDetail.getOrderType(), outfitType,
                    itemDetail.getInspiration(), itemDetail.getIsPriorityOrder(), itemDetail.getItemQuantity(),
                    orderDAO);
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
        orderDAO.setOrderItems(orderItemList);
        priceBreakUpService.savePriceBreakUp(priceBreakUpList);
        objectImagesService.saveObjectImages(clothRefOrderItemIdMap, ImageEntityType.ORDER_ITEM.getEntityType());
        return orderDAO;
    }

    private void validatePriceBreakup(List<PriceBreakUpDetails> allPriceBreakups, Double totalAmount) {
        Double expectedTotalAmount = allPriceBreakups.stream()
                .mapToDouble(priceBreakUp -> priceBreakUp.getValue() * priceBreakUp.getComponentQuantity())
                .sum();
        if (!expectedTotalAmount.equals(totalAmount)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total amount calculated is incorrect");
        }
    }

    private OrderAmountDAO setOrderAmountSpecificDetails(OrderAmountDetails orderAmountDetails, OrderDAO orderDAO) {
        Double advanceRecieved = Optional.ofNullable(orderAmountDetails.getAdvanceOrderAmount()).orElse(0d);
        Double totalOrderAmount = orderAmountDetails.getTotalOrderAmount();
        OrderAmountDAO orderAmountDAO = new OrderAmountDAO(totalOrderAmount, advanceRecieved, orderDAO);
        orderAmountDAO.setOrder(orderDAO);
        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        Double pendingAmount = totalOrderAmount - advanceRecieved;
        boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(pendingAmount, advanceRecieved,
                orderDAO.getBoutique().getId());

        if (advanceRecieved > 0) {
            paymentService.recordPayment(advanceRecieved, PaymentMode.CASH, Boolean.TRUE, orderDAO);
        }
        return orderAmountDAO;
    }

    @Transactional
    private void resetOrderAmountForDeletedOrders(OrderDAO orderDAO, OrderStage orderStage) {
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        Double pendingOrderAmount = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
        Double amountRecieved = orderAmountDAO.getAmountRecieved();
        orderAmountDAO.setAmountRecieved(0d);
        orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext()));
        paymentService.recordPayment(-amountRecieved, PaymentMode.CASH, Boolean.FALSE, orderDAO);
        boutiqueLedgerService.handleBoutiqueLedgerForDeletedOrder(orderDAO.getBoutique().getId(),
                pendingOrderAmount,
                amountRecieved,
                orderStage);
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

    @Transactional
    private void updateLedgerIfApplicable(OrderDAO order, OrderStatus initialStatus) {
        OrderStatus currentStatus = order.getOrderStatus();
        Long boutiqueId = order.getBoutique().getId();
        boutiqueLedgerService.handleBoutiqueLedgerOnOrderUpdation(boutiqueId, initialStatus, currentStatus);
    }

    private OrderStage getOrderStage(OrderStatus status) {
        if (Constants.ACTIVE_ORDER_STATUS_LIST.contains(status)) {
            return OrderStage.ACTIVE;
        } else if (Constants.CLOSED_ORDER_STATUS_LIST.contains(status)) {
            return OrderStage.CLOSED;
        }
        return OrderStage.OTHER;
    }

    /**
     * This is a throw-away code, front-end team to handle the week data dynamically
     *
     * @param weekwiseSales
     */
    private void populateSalesIfRequired(List<WeekwiseSalesSplit> weekwiseSales) {
        while (weekwiseSales.size() < 5) {
            weekwiseSales.add(new WeekwiseSalesSplit(0d, null));
        }
    }

    private List<OrderItemSummary> generateOrderItemSummaries(List<OrderItemDAO> orderItemDAOList) {
        List<OrderItemSummary> orderItemSummaryList = new ArrayList<>();
        for (OrderItemDAO item : orderItemDAOList) {
            orderItemSummaryList.add(new OrderItemSummary(item));
        }
        return orderItemSummaryList;
    }

    private void postUpdateOrderValidation(Double totalOrderAmount, List<PriceBreakupDAO> allItemsPriceBreakup) {
        Double itemsPriceBreakupSum = allItemsPriceBreakup.stream().mapToDouble(PriceBreakupDAO::getValue).sum();
        if (!itemsPriceBreakupSum.equals(totalOrderAmount)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Either price break up or total order amount is incorrect");
        }
    }
}
