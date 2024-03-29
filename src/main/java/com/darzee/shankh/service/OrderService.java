package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.Order;
import com.darzee.shankh.enums.OrderItemStatus;
import com.darzee.shankh.enums.OrderStatus;
import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.PaymentMode;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.GetOrderDetailsRequest;
import com.darzee.shankh.request.OrderCreationRequest;
import com.darzee.shankh.request.RecievePaymentRequest;
import com.darzee.shankh.request.innerObjects.OrderAmountDetails;
import com.darzee.shankh.response.*;
import com.darzee.shankh.utils.pdfutils.BillGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    private PaymentService paymentService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OrderAmountRepo orderAmountRepo;
    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    @Autowired
    private OutfitImageLinkService outfitImageLinkService;

    @Autowired
    private ObjectFilesService objectFilesService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private BillGenerator billGenerator;
    @Autowired
    private BoutiqueLedgerService boutiqueLedgerService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private FileReferenceRepo fileReferenceRepo;

    @Autowired
    private AmazonClient s3Client;

    @Autowired
    private PriceBreakUpService priceBreakUpService;

    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private PaymentRepo paymentRepo;

    @Transactional
    public OrderDAO findOrCreateNewOrder(Long orderId, Long boutiqueId, Long customerId) {
        OrderDAO orderDAO = null;
        if (orderId != null) {
            Optional<Order> optionalOrder = orderRepo.findById(orderId);
            if (optionalOrder.isPresent()) {
                orderDAO = mapper.orderObjectToDao(optionalOrder.get(), new CycleAvoidingMappingContext());
            }
        }
        if (orderDAO == null) {
            orderDAO = createNewOrder(boutiqueId, customerId);
        }
        return orderDAO;
    }

    @Transactional
    public OrderDAO createNewOrder(Long boutiqueId, Long customerId) {
        Optional<Customer> optionalCustomer = customerRepo.findById(customerId);
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(boutiqueId);
        if (optionalCustomer.isPresent() && optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(),
                    new CycleAvoidingMappingContext());
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(),
                    new CycleAvoidingMappingContext());
            OrderAmountDAO orderAmountDAO = new OrderAmountDAO();
            orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(
                            mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
            OrderDAO orderDAO = setOrderSpecificDetails(boutiqueDAO, customerDAO);
            orderDAO.setOrderAmount(orderAmountDAO);
            orderDAO = mapper.orderObjectToDao(
                    orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
            return orderDAO;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid customer id or boutique id");
    }

    public ResponseEntity<GetOrderResponse> getOrder(Long boutiqueId, String orderItemStatusList,
                                                     String orderStatusList,
                                                     Boolean priorityOrdersOnly, Long customerId,
                                                     String deliveryDateFrom, String deliveryDateTill,
                                                     Boolean paymentDue, Integer countPerPage, Integer pageCount) {
        validateGetOrderRequest(orderItemStatusList, orderStatusList);
        Map<String, Object> filterMap = GetOrderDetailsRequest.getFilterMap(boutiqueId, null,
                orderStatusList, priorityOrdersOnly, customerId, deliveryDateFrom, deliveryDateTill,
                null, paymentDue);
        String defaultOrderSortKey = "created_at";
        String defaultOrderSortOrder = "desc";
        Map<String, Object> pagingCriteriaMap = GetOrderDetailsRequest.getPagingCriteria(countPerPage, pageCount,
                defaultOrderSortKey, defaultOrderSortOrder);
        Specification<Order> orderSpecification = OrderSpecificationClause.getSpecificationBasedOnFilters(filterMap);
        Pageable pagingCriteria = filterOrderService.getPagingCriteria(pagingCriteriaMap);
        List<Order> orderDetails = orderRepo.findAll(orderSpecification, pagingCriteria).getContent();
        Long totalRecordsCount = orderRepo.count(orderSpecification);
        List<OrderDAO> orderDAOList = mapper.orderObjectListToDAOList(orderDetails, new CycleAvoidingMappingContext());
        List<OrderItemStatus> orderItemStatuses = Arrays.asList(OrderItemStatus.values());
        if (orderItemStatusList != null) {
            List<Integer> eligibleStatuses = Arrays.stream(orderItemStatusList.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            orderItemStatuses = orderItemStatuses.stream()
                    .filter(orderItemStatus -> eligibleStatuses.contains(orderItemStatus.getOrdinal()))
                    .collect(Collectors.toList());
        }
        List<OrderItemStatus> finalOrderItemStatuses = orderItemStatuses;
        List<OrderDetailResponse> orderDetailsList = orderDAOList.stream()
                .map(order -> getOrderDetails(order, finalOrderItemStatuses))
                .collect(Collectors.toList());
        GetOrderResponse response = new GetOrderResponse(orderDetailsList, totalRecordsCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity getOrderDetails(Long orderId, Long boutiqueId) throws Exception {
        OrderDAO order = findOrder(orderId, boutiqueId);
        OrderAmountDAO orderAmount = order.getOrderAmount();
        CustomerDAO customer = order.getCustomer();
        List<OrderItemDetails> orderItemDetailsList = new ArrayList<>();
        String message = "Details fetched succesfully";
        OrderDetailResponse response = new OrderDetailResponse(customer, order, orderAmount,
                orderItemDetailsList, message);
        return new ResponseEntity(response, HttpStatus.OK);
    }

//    public ResponseEntity getOrderInvoiceLink(Long orderId) {
//        String link = bucketService.getInvoiceShortLivedLink(orderId);
//        link = link.trim();
//        GetFileResponse response = new GetFileResponse(link);
//        return new ResponseEntity(response, HttpStatus.OK);
//    }

//    @Transactional
//    public ResponseEntity updateOrder(Long orderId, UpdateOrderRequest request) {
//        Optional<Order> optionalOrder = orderRepo.findById(orderId);
//        if (optionalOrder.isPresent()) {
//            OrderDAO order = mapper.orderObjectToDao(optionalOrder.get(), new CycleAvoidingMappingContext());
//            OrderAmountDAO orderAmountDAO = order.getOrderAmount();
//            UpdateOrderDetails orderDetails = request.getOrderDetails();
//            UpdateOrderAmountDetails orderAmountDetails = request.getOrderAmountDetails();
//            if (orderDetails != null) {
//                order = updateOrderDetails(orderDetails, order);
//            }
//            if (orderAmountDetails != null) {
//                orderAmountDAO = updateOrderAmountDetails(orderAmountDetails, orderAmountDAO, order,
//                        order.getBoutique().getId());
//            }
//            List<PriceBreakupDAO> priceBreakupDAOList = order.getNonDeletedItems().stream()
//                    .map(OrderItemDAO::getPriceBreakup).flatMap(List::stream).collect(Collectors.toList());
//            postUpdateOrderValidation(orderAmountDAO.getTotalAmount(), priceBreakupDAOList);
//            OrderSummary orderSummary = new OrderSummary(order.getId(), order.getInvoiceNo(),
//                    orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(),
//                    order.getNonDeletedItems());
//            CreateOrderResponse response = new CreateOrderResponse("Order updated successfully", orderSummary);
//            return new ResponseEntity(response, HttpStatus.OK);
//        }
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID is invalid");
//    }

    public ResponseEntity<OrderSummary> confirmOrderAndGenerateInvoice(Long boutiqueOrderId, Long boutiqueId,
                                                                       OrderCreationRequest request) {
        OrderDAO orderDAO = confirmOrder(boutiqueOrderId, boutiqueId, request);
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        OrderSummary summary = new OrderSummary(orderDAO.getBoutiqueOrderId(), orderDAO.getInvoiceNo(),
                orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(), orderDAO.getNonDeletedItems());
        generateInvoiceV2(orderDAO);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

    @Transactional
    public OrderDAO confirmOrder(Long boutiqueOrderId, Long boutiqueId, OrderCreationRequest request) {
        OrderDAO orderDAO = findOrder(boutiqueOrderId, boutiqueId);
        if (!orderDAO.validateMandatoryOrderFields()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Some mandatory fields in order are missing." +
                    " Boutique ID and customer ID are mandatory fields");
        }
        orderItemService.validateMandatoryOrderItemFields(orderDAO.getOrderItems());
        Double priceBreakupSum = orderDAO.getPriceBreakupSum();
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        Double totalOrderAmount = orderAmountDAO.getTotalAmount();
        if (!priceBreakupSum.equals(totalOrderAmount)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Items' Price Breakups are not summing up to total amount. " +
                            "Total amount " + totalOrderAmount + " and price break up sum "
                            + priceBreakupSum);
        }
        orderDAO.setOrderStatus(OrderStatus.ACCEPTED);
        orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO,
                new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        orderItemService.acceptOrderItems(orderDAO.getNonDeletedItems());
        if (request.getOrderDetails().getOrderAmountDetails().getAdvanceReceived() != null) {
            Double advanceAmount = request.getOrderDetails().getOrderAmountDetails().getAdvanceReceived();
            orderAmountDAO.setAmountRecieved(advanceAmount);
            orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext()));
            paymentService.recordPayment(advanceAmount, PaymentMode.CASH, Boolean.TRUE, orderDAO);
        }
        updateBoutiqueLedgerOnOrderConfirmation(orderAmountDAO, orderDAO);
        return orderDAO;
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
            if (orderTypeDbOrdinal == null) {
                continue;
            }
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
    public ResponseEntity recieveOrderPayment(Long orderId, Long boutiqueId, RecievePaymentRequest request) {
        OrderDAO orderDAO = findOrder(orderId, boutiqueId);
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

        boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(null, orderDAO.getBoutiqueId(),
                -amountRecieved, amountRecieved);
        String message = "Order payment recorded successfully";
        Double pendingAmountLeft = pendingAmount - amountRecieved;
        PaymentMode paymentMode = PaymentMode.getPaymentTypeEnumOrdinalMap().get(request.getPaymentMode());
        paymentService.recordPayment(amountRecieved, paymentMode, Boolean.FALSE, orderDAO);
        generateInvoice(orderDAO);

        RecievePaymentResponse response = new RecievePaymentResponse(message,
                orderId,
                pendingAmountLeft);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public String generateInvoice(OrderDAO orderDAO) {
        CustomerDAO customerDAO = orderDAO.getCustomer();
        String customerName = customerDAO.constructName();
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        BoutiqueDAO boutique = orderDAO.getBoutique();
        File bill = billGenerator.generateBill(customerName,
                customerDAO.getPhoneNumber(),
                orderDAO,
                orderAmountDAO,
                boutique);
        String fileUploadUrl = bucketService.uploadInvoice(bill, orderDAO.getId());
        bill.delete();
        return fileUploadUrl;
    }

    public String generateInvoiceV2(OrderDAO orderDAO) {
        CustomerDAO customerDAO = orderDAO.getCustomer();
        String customerName = customerDAO.constructName();
        BoutiqueDAO boutique = orderDAO.getBoutique();
        TailorDAO tailorDAO = boutique.getAdminTailor();
        File bill = billGenerator.generateBillV2(boutique, customerName,
                tailorDAO.getPhoneNumber(), orderDAO);
        String fileUploadUrl = bucketService.uploadInvoice(bill, orderDAO.getId());
        bill.delete();
        return fileUploadUrl;
    }

//    @Transactional
//    private OrderDAO updateOrderDetails(UpdateOrderDetails orderDetails, OrderDAO order) {
//        if (Boolean.TRUE.equals(orderDetails.getDeleteOrder())) {
//            order.setIsDeleted(Boolean.TRUE);
//            resetOrderAmountForDeletedOrders(order, order.getOrderStatus());
//        }
//
//        Map<Long, OrderItemDAO> orderItemDAOMap = order.getOrderItemDAOMap();
//        List<OrderItemDAO> updatedItems = new ArrayList<>();
//        order = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(order,
//                new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
//        return order;
//    }

//    @Transactional
//    private OrderAmountDAO updateOrderAmountDetails(UpdateOrderAmountDetails orderAmountDetails,
//                                                    OrderAmountDAO orderAmount, OrderDAO order, Long boutiqueId) {
//        Double totalAmount = Optional.ofNullable(orderAmountDetails.getTotalOrderAmount()).orElse(orderAmount.getTotalAmount());
//        List<PaymentDAO> payments = mapper.paymentToPaymentDAOList(paymentRepo.findAllByOrderId(order.getId()), new CycleAvoidingMappingContext());
//        Double advancePaid = getAdvancePaid(payments);
//        Double totalAmountPaid = getTotalAmountPaid(payments);
//        Double advancePayment = Optional.ofNullable(orderAmountDetails.getAdvanceOrderAmount()).orElse(advancePaid);
//        if (orderAmount.getTotalAmount().equals(totalAmount)
//                && advancePaid.equals(advancePayment)) {
//            return orderAmount;
//        }
//
//        Double deltaTotalAmount = totalAmount - orderAmount.getTotalAmount();
//        Double amountPaidAfterThisUpdate = totalAmountPaid + (advancePayment - advancePaid);
//
//        if (amountPaidAfterThisUpdate > totalAmount) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount paid is greater than total amount. Amount Paid "
//                    + amountPaidAfterThisUpdate
//                    + " and total amount " + totalAmount);
//        }
//        Double deltaPendingAmount = (totalAmount - advancePayment) - (orderAmount.getTotalAmount() - advancePaid);
//        orderAmount.setTotalAmount(totalAmount);
//        orderAmount.setAmountRecieved(amountPaidAfterThisUpdate);
//        orderAmount = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmount, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
//
//        if (advancePaid != advancePayment) {
//            paymentService.updateAdvancePayment(order, advancePayment);
//        }
//
//        Double deltaAmountRecieved = amountPaidAfterThisUpdate - totalAmountPaid;
//        boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(deltaPendingAmount, deltaAmountRecieved, boutiqueId);
//
//        generateInvoice(order.getId());
//
//        return orderAmount;
//    }

    private Double getAdvancePaid(List<PaymentDAO> payments) {
        Double advancePaid = 0d;
        advancePaid = payments.stream()
                .filter(paymentDAO -> Boolean.TRUE.equals(paymentDAO.getIsAdvancePayment()))
                .mapToDouble(PaymentDAO::getAmount)
                .sum();
        return advancePaid;
    }

    private Double getTotalAmountPaid(List<PaymentDAO> payments) {
        Double totalAmountPaid = 0d;
        totalAmountPaid = payments.stream().mapToDouble(PaymentDAO::getAmount).sum();
        return totalAmountPaid;
    }

    private OrderDetailResponse getOrderDetails(OrderDAO orderDAO, List<OrderItemStatus> eligibleStatuses) {
        String customerProfilePicLink = customerService.getCustomerProfilePicLink(orderDAO.getCustomer().getId());
        List<Pair<OrderItemDAO, String>> orderItemOutfitLinkPairList = new ArrayList<>();
        List<OrderItemDAO> orderItems = orderDAO.getNonDeletedItems().stream()
                .filter(item -> eligibleStatuses.contains(item.getOrderItemStatus()))
                .collect(Collectors.toList());
        for (OrderItemDAO orderItem : orderItems) {
            String outfitImgLink = outfitImageLinkService.getOutfitImageLink(orderItem.getOutfitType());
            orderItemOutfitLinkPairList.add(Pair.of(orderItem, outfitImgLink));
        }

        return new OrderDetailResponse(orderDAO, orderItemOutfitLinkPairList, customerProfilePicLink);
    }


    private OrderDAO setOrderSpecificDetails(BoutiqueDAO boutiqueDAO, CustomerDAO customerDAO) {

        String invoiceNo = generateOrderInvoiceNo();
        OrderDAO orderDAO = new OrderDAO(invoiceNo, boutiqueDAO, customerDAO);
        return orderDAO;
    }

//    private void validatePriceBreakup(List<PriceBreakUpDetails> allPriceBreakups, Double totalAmount) {
//        Double expectedTotalAmount = allPriceBreakups.stream()
//                .mapToDouble(priceBreakUp -> priceBreakUp.getValue() * priceBreakUp.getComponentQuantity())
//                .sum();
//        if (!expectedTotalAmount.equals(totalAmount)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total amount calculated is incorrect");
//        }
//    }

    @Transactional
    public OrderAmountDAO setOrderAmountSpecificDetails(OrderAmountDetails orderAmountDetails, OrderDAO orderDAO) {
        Double amountRecieved = 0d;
        if (orderAmountDetails != null && orderAmountDetails.getAdvanceReceived() != null) {
            amountRecieved = orderAmountDetails.getAdvanceReceived();
        }
        Double totalOrderAmount = calculateTotalOrderAmount(orderDAO.getNonDeletedItems());

        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        orderAmountDAO.setTotalAmount(totalOrderAmount);
        orderAmountDAO.setAmountRecieved(amountRecieved);
        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return orderAmountDAO;
    }

    private void updateBoutiqueLedgerOnOrderConfirmation(OrderAmountDAO orderAmount, OrderDAO orderDAO) {
        Double amountRecieved = orderAmount.getAmountRecieved();
        Double amountPending = orderAmount.getTotalAmount() - amountRecieved;
        BoutiqueLedgerDAO ledger = boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(null, orderDAO.getBoutiqueId(),
                amountPending, amountRecieved);
        boutiqueLedgerService.updateCountOnOrderConfirmation(ledger, orderDAO.getBoutiqueId());
    }

    //todo : move this logic to spring state machine
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public OrderDAO updateOrderPostItemUpdation(Long orderId) {
        OrderDAO orderDAO = mapper.orderObjectToDao(orderRepo.findById(orderId).get(),
                new CycleAvoidingMappingContext());
        BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(boutiqueLedgerRepo.findByBoutiqueId(orderDAO.getBoutiqueId()),
                new CycleAvoidingMappingContext());
        List<OrderItemDAO> orderItems = orderDAO.getNonDeletedItems();
        boolean allItemsAccepted = orderItems.stream()
                .filter(item -> OrderItemStatus.ACCEPTED.equals(item.getOrderItemStatus()))
                .collect(Collectors.toList()).size() == orderItems.size();
        if (allItemsAccepted && !OrderStatus.ACCEPTED.equals(orderDAO.getOrderStatus())) {
            orderDAO.setOrderStatus(OrderStatus.ACCEPTED);
            boutiqueLedgerDAO = boutiqueLedgerService.handleBoutiqueLedgerOnOrderUpdation(boutiqueLedgerDAO,
                    orderDAO.getBoutiqueId(), 1, 0);
        }
        boolean allItemsDelivered = orderItems.stream()
                .filter(item -> OrderItemStatus.DELIVERED.equals(item.getOrderItemStatus()))
                .collect(Collectors.toList()).size() == orderItems.size();
        if (allItemsDelivered && !OrderStatus.DELIVERED.equals(orderDAO.getOrderStatus())) {
            orderDAO.setOrderStatus(OrderStatus.DELIVERED);
            boutiqueLedgerDAO = boutiqueLedgerService.handleBoutiqueLedgerOnOrderUpdation(boutiqueLedgerDAO,
                    orderDAO.getBoutiqueId(), 0, 1);
        }
        boolean allItemsDeleted = (orderItems.size() == 0);
        if (allItemsDeleted && !Boolean.TRUE.equals(orderDAO.getIsDeleted())) {
            orderDAO.setIsDeleted(Boolean.TRUE);
            boutiqueLedgerDAO = boutiqueLedgerService.handleBoutiqueLedgerOnOrderDeletion(boutiqueLedgerDAO,
                    orderDAO.getBoutiqueId(), orderDAO);
        }
        Double orderItemsPriceSum = orderItems.stream().mapToDouble(item -> item.calculateItemPrice()).sum();
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        Double orderAmountDelta = 0d;
        if (!orderAmountDAO.getTotalAmount().equals(orderItemsPriceSum)) {
            orderAmountDelta = orderItemsPriceSum - orderAmountDAO.getTotalAmount();
            orderAmountDAO.setTotalAmount(orderItemsPriceSum);
        }
        orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext()));
        orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext()));
        if (orderAmountDelta != 0) {
            Double deltaPendingAmount = orderAmountDelta;
            boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(boutiqueLedgerDAO, orderDAO.getBoutiqueId(),
                    deltaPendingAmount, 0d);
        }
        return orderDAO;
    }

    public Pair getItemsCount(Long boutiqueId, LocalDateTime startTime, LocalDateTime endTime) {
        Integer newItemsCount = orderRepo.getNewItemsCount(boutiqueId, startTime, endTime);
        Integer closedItemsCount = orderRepo.getNewItemsCount(boutiqueId, startTime, endTime);
        return Pair.of(newItemsCount, closedItemsCount);
    }

    public boolean checkIfBoutiqueIsActive(Long boutiqueId) {
        return orderRepo.countByBoutiqueId(boutiqueId) > 0;
    }

    private Double calculateTotalOrderAmount(List<OrderItemDAO> orderItems) {
        List<PriceBreakupDAO> priceBreakups = orderItems.stream().map(item -> item.getActivePriceBreakUpList())
                .flatMap(List::stream).collect(Collectors.toList());
        Double totalAmount = 0d;
        if (!CollectionUtils.isEmpty(priceBreakups)) {
            for (PriceBreakupDAO priceBreakupDAO : priceBreakups) {
                if (priceBreakupDAO.getValue() != null && priceBreakupDAO.getQuantity() != null) {
                    totalAmount += priceBreakupDAO.getValue() * priceBreakupDAO.getQuantity();
                }
            }
        }
        return totalAmount;
    }

//    @Transactional
//    private void resetOrderAmountForDeletedOrders(OrderDAO orderDAO, OrderStatus status) {
//        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
//        Double pendingOrderAmount = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
//        Double amountRecieved = orderAmountDAO.getAmountRecieved();
//        orderAmountDAO.setAmountRecieved(0d);
//        orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext()));
//        paymentService.recordPayment(-amountRecieved, PaymentMode.CASH, Boolean.FALSE, orderDAO);
//        boutiqueLedgerService.handleBoutiqueLedgerForDeletedOrder(orderDAO.getBoutique().getId(),
//                pendingOrderAmount,
//                amountRecieved,
//                status);
//    }

    private String generateOrderInvoiceNo() {
        return RandomStringUtils.randomAlphanumeric(6);
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

//    private List<OrderItemSummary> generateOrderItemSummaries(List<OrderItemDAO> orderItemDAOList) {
//        List<OrderItemSummary> orderItemSummaryList = new ArrayList<>();
//        for (OrderItemDAO item : orderItemDAOList) {
//            orderItemSummaryList.add(new OrderItemSummary(item));
//        }
//        return orderItemSummaryList;
//    }

    private void postUpdateOrderValidation(Double totalOrderAmount, List<PriceBreakupDAO> allItemsPriceBreakup) {
        Double itemsPriceBreakupSum = allItemsPriceBreakup.stream().mapToDouble(PriceBreakupDAO::getValue).sum();
        if (!itemsPriceBreakupSum.equals(totalOrderAmount)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Either price break up or total order amount is incorrect");
        }
    }

    private void validateGetOrderRequest(String orderStatusList, String orderItemStatusList) {
        if (orderStatusList == null && orderItemStatusList == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either order status or item status is necessary to get orders");
        }
    }

    private OrderDAO findOrder(Long boutiqueOrderId, Long boutiqueId) {
        Optional<Order> order = orderRepo.findByBoutiqueOrderIdAndBoutiqueId(boutiqueOrderId, boutiqueId);
        if (!order.isPresent()) {
            order = orderRepo.findById(boutiqueOrderId);
            if (!order.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order ID");
            }
        }
        OrderDAO orderDAO = mapper.orderObjectToDao(order.get(), new CycleAvoidingMappingContext());
        return orderDAO;
    }
}
