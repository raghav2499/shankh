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
import com.darzee.shankh.request.*;
import com.darzee.shankh.request.innerObjects.OrderAmountDetails;
import com.darzee.shankh.request.innerObjects.UpdateOrderAmountDetails;
import com.darzee.shankh.request.innerObjects.UpdateOrderDetails;
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

//    public ResponseEntity createOrderAndGenerateInvoice(CreateOrderRequest request) {
//        CreateOrderResponse response = null;
//        OrderSummary orderSummary = createNewOrder(request);
//        if (orderSummary != null) {
//            generateInvoice(orderSummary.getOrderId());
//            String successMessage = "Order created successfully";
//            response = new CreateOrderResponse(successMessage, orderSummary);
//            return new ResponseEntity(response, HttpStatus.CREATED);
//        }
//        String failureMessage = "No eligible boutique/customer found";
//        response = new CreateOrderResponse(failureMessage);
//        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
//    }

//    @Transactional
//    public OrderSummary createNewOrder(CreateOrderRequest request) {
//        OrderDetails orderDetails = request.getOrderDetails();
//        OrderAmountDetails orderAmountDetails = request.getOrderAmountDetails();
//        List<PriceBreakUpDetails> allItemsPriceBreakUpDetails = orderDetails.getOrderItems()
//                .stream().map(orderItem -> orderItem.getPriceBreakup()).flatMap(List::stream)
//                .collect(Collectors.toList());
//        validatePriceBreakup(allItemsPriceBreakUpDetails, orderAmountDetails.getTotalOrderAmount());
//
//        Optional<Customer> optionalCustomer = customerRepo.findById(orderDetails.getCustomerId());
//        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(orderDetails.getBoutiqueId());
//        if (optionalCustomer.isPresent() && optionalBoutique.isPresent()) {
//            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(), new CycleAvoidingMappingContext());
//            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
//
//            OrderDAO orderDAO = setOrderSpecificDetails(orderDetails, boutiqueDAO, customerDAO);
//            OrderAmountDAO orderAmountDAO = setOrderAmountSpecificDetails(orderAmountDetails, orderDAO);
//            orderDAO.setOrderAmount(orderAmountDAO);
//            orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext()));
//            return new OrderSummary(orderDAO.getId(), orderDAO.getInvoiceNo(),
//                    orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(),
//                    orderDAO.getOrderItems());
//        }
//        return null;
//    }

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
            OrderDAO orderDAO = setOrderSpecificDetails(boutiqueDAO, customerDAO);
            OrderAmountDAO orderAmountDAO = new OrderAmountDAO();
            orderAmountDAO.setOrder(orderDAO);
            orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(
                            mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
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
        Map<String, Object> filterMap = GetOrderDetailsRequest.getFilterMap(boutiqueId, orderItemStatusList,
                orderStatusList, priorityOrdersOnly, customerId, deliveryDateFrom, deliveryDateTill,
                null, paymentDue);
        Map<String, Object> pagingCriteriaMap = GetOrderDetailsRequest.getPagingCriteria(countPerPage, pageCount, null, null);
        Specification<Order> orderSpecification = OrderSpecificationClause.getSpecificationBasedOnFilters(filterMap);
        Pageable pagingCriteria = filterOrderService.getPagingCriteria(pagingCriteriaMap);
        List<Order> orderDetails = orderRepo.findAll(orderSpecification, pagingCriteria).getContent();
        Long totalRecordsCount = orderRepo.count(orderSpecification);
        List<OrderDAO> orderDAOList = mapper.orderObjectListToDAOList(orderDetails, new CycleAvoidingMappingContext());
        List<OrderDetailResponse> orderDetailsList = orderDAOList.stream()
                .map(order -> getOrderDetails(order))
                .collect(Collectors.toList());
        GetOrderResponse response = new GetOrderResponse(orderDetailsList, totalRecordsCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity getOrderDetails(Long orderId) throws Exception {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderDAO order = mapper.orderObjectToDao(optionalOrder.get(), new CycleAvoidingMappingContext());
            OrderAmountDAO orderAmount = order.getOrderAmount();
            CustomerDAO customer = order.getCustomer();
            List<OrderItemDetails> orderItemDetailsList = new ArrayList<>();
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
            List<PriceBreakupDAO> priceBreakupDAOList = order.getNonDeletedItems().stream()
                    .map(OrderItemDAO::getPriceBreakup).flatMap(List::stream).collect(Collectors.toList());
            postUpdateOrderValidation(orderAmountDAO.getTotalAmount(), priceBreakupDAOList);
            OrderSummary orderSummary = new OrderSummary(order.getId(), order.getInvoiceNo(),
                    orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(),
                    order.getNonDeletedItems());
            CreateOrderResponse response = new CreateOrderResponse("Order updated successfully", orderSummary);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID is invalid");
    }

    public ResponseEntity<OrderSummary> confirmOrderAndGenerateInvoice(Long orderId, OrderCreationRequest request) {
        OrderSummary orderSummary = confirmOrder(orderId, request);
        generateInvoiceV2(orderId);
        return new ResponseEntity<>(orderSummary, HttpStatus.OK);
    }

    @Transactional
    public OrderSummary confirmOrder(Long orderId, OrderCreationRequest request) {
        Optional<Order> order = orderRepo.findById(orderId);
        if (!order.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order with ID " + orderId + " doesn't exist");
        }
        OrderDAO orderDAO = mapper.orderObjectToDao(order.get(), new CycleAvoidingMappingContext());

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
        if (request.getOrderDetails().getOrderAmountDetails().getAdvanceOrderAmount() != null) {
            Double advanceAmount = request.getOrderDetails().getOrderAmountDetails().getAdvanceOrderAmount();
            orderAmountDAO.setAmountRecieved(advanceAmount);
            orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext()));
            paymentService.recordPayment(advanceAmount, PaymentMode.CASH, Boolean.TRUE, orderDAO);
        }
        OrderSummary summary = new OrderSummary(orderDAO.getId(), orderDAO.getInvoiceNo(), totalOrderAmount,
                orderDAO.getOrderAmount().getAmountRecieved(), orderDAO.getNonDeletedItems());
        return summary;
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
            String customerName = customerDAO.constructName();
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

    public String generateInvoiceV2(Long orderId) {
        Optional<Order> order = orderRepo.findById(orderId);
        if (order.isPresent()) {
            OrderDAO orderDAO = mapper.orderObjectToDao(order.get(), new CycleAvoidingMappingContext());
            CustomerDAO customerDAO = orderDAO.getCustomer();
            String customerName = customerDAO.constructName();
            BoutiqueDAO boutique = orderDAO.getBoutique();
            TailorDAO tailorDAO = boutique.getAdminTailor();
            File bill = billGenerator.generateBillV2(boutique, customerName,
                    tailorDAO.getPhoneNumber(), orderDAO);
            String fileUploadUrl = bucketService.uploadInvoice(bill, orderId);
            bill.delete();
            return fileUploadUrl;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order id");
    }

    @Transactional
    private OrderDAO updateOrderDetails(UpdateOrderDetails orderDetails, OrderDAO order) {
//        if (order.isOrderStatusUpdated(orderDetails.getStatus())) {
//            Integer targetStatusOrdinal = orderDetails.getStatus();
//            OrderStatus initialStatus = order.getOrderStatus();
//            OrderStatus targetStatus = OrderStatus.getOrderTypeEnumOrdinalMap().get(targetStatusOrdinal);
//            if (targetStatus == null) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status " + targetStatusOrdinal + " is not valid status");
//            }
//            if (orderStateMachineService.isTransitionAllowed(order.getOrderStatus(), targetStatus)) {
//                order.setOrderStatus(targetStatus);
//                updateLedgerIfApplicable(order, initialStatus);
//            } else {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "State transition from "
//                        + order.getOrderStatus() + " to " + targetStatus + " is not allowed");
//            }
//        }

        if (Boolean.TRUE.equals(orderDetails.getDeleteOrder())) {
            order.setIsDeleted(Boolean.TRUE);
            resetOrderAmountForDeletedOrders(order, order.getOrderStatus());
        }

        Map<Long, OrderItemDAO> orderItemDAOMap = order.getOrderItemDAOMap();
        List<OrderItemDAO> updatedItems = new ArrayList<>();
        order = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(order,
                new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return order;
    }

    @Transactional
    private OrderAmountDAO updateOrderAmountDetails(UpdateOrderAmountDetails orderAmountDetails,
                                                    OrderAmountDAO orderAmount, OrderDAO order, Long boutiqueId) {
        Double totalAmount = Optional.ofNullable(orderAmountDetails.getTotalOrderAmount()).orElse(orderAmount.getTotalAmount());
        List<PaymentDAO> payments = mapper.paymentToPaymentDAOList(paymentRepo.findAllByOrderId(order.getId()), new CycleAvoidingMappingContext());
        Double advancePaid = getAdvancePaid(payments);
        Double totalAmountPaid = getTotalAmountPaid(payments);
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

    private OrderDetailResponse getOrderDetails(OrderDAO orderDAO) {
        String customerProfilePicLink = customerService.getCustomerProfilePicLink(orderDAO.getCustomer().getId());
        List<Pair<OrderItemDAO, String>> orderItemOutfitLinkPairList = new ArrayList<>();
        for (OrderItemDAO orderItem : orderDAO.getNonDeletedItems()) {
            String outfitImgLink = outfitImageLinkService.getOutfitImageLink(orderItem.getOutfitType());
            orderItemOutfitLinkPairList.add(Pair.of(orderItem, outfitImgLink));
        }

        return new OrderDetailResponse(orderDAO, orderItemOutfitLinkPairList, customerProfilePicLink);
    }

//    @Transactional(REQUIRES_NEW)
//    public OrderDAO setOrderSpecificDetails(OrderDetails orderDetails, BoutiqueDAO boutiqueDAO,
//                                            CustomerDAO customerDAO) {
//
//        String invoiceNo = generateOrderInvoiceNo();
//        OrderDAO orderDAO = new OrderDAO(invoiceNo, boutiqueDAO, customerDAO);
//        orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO,
//                new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
//
//        List<OrderItemDetailRequest> orderItemDetails = orderDetails.getOrderItems();
//        List<OrderItemDAO> orderItems = orderItemService.createOrderItems(orderItemDetails, orderDAO);
//        orderDAO.setOrderItems(orderItems);
//        return orderDAO;
//    }


    @Transactional
    private OrderDAO setOrderSpecificDetails(BoutiqueDAO boutiqueDAO, CustomerDAO customerDAO) {

        String invoiceNo = generateOrderInvoiceNo();
        OrderDAO orderDAO = new OrderDAO(invoiceNo, boutiqueDAO, customerDAO);
        orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO,
                new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
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

    @Transactional
    public OrderAmountDAO setOrderAmountSpecificDetails(OrderAmountDetails orderAmountDetails, OrderDAO orderDAO) {
        Double amountRecieved = 0d;
        if (orderAmountDetails != null && orderAmountDetails.getAdvanceOrderAmount() != null) {
            amountRecieved = orderAmountDetails.getAdvanceOrderAmount();
        }
        Double totalOrderAmount = calculateTotalOrderAmount(orderDAO.getNonDeletedItems());

        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        Double oldTotal = Optional.ofNullable(orderAmountDAO.getTotalAmount()).orElse(0d);
        Double oldAmountRecieved = Optional.ofNullable(orderAmountDAO.getAmountRecieved()).orElse(0d);
        orderAmountDAO.setTotalAmount(totalOrderAmount);
        orderAmountDAO.setAmountRecieved(amountRecieved);

        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        Double deltaAmountRecieved = amountRecieved - oldAmountRecieved;
        Double deltaPending = (totalOrderAmount - oldTotal) - deltaAmountRecieved;
        boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(deltaPending, deltaAmountRecieved,
                orderDAO.getBoutique().getId());

        if (deltaAmountRecieved > 0) {
            paymentService.recordPayment(deltaAmountRecieved, PaymentMode.CASH, Boolean.TRUE, orderDAO);
        }
        return orderAmountDAO;
    }

    //todo : move this logic to spring state machine
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public OrderDAO updateOrderPostItemUpdation(Long orderId) {
        OrderDAO orderDAO = mapper.orderObjectToDao(orderRepo.findById(orderId).get(),
                new CycleAvoidingMappingContext());
        List<OrderItemDAO> orderItems = orderDAO.getNonDeletedItems();
        boolean allItemsAccepted = orderItems.stream()
                .map(item -> OrderItemStatus.ACCEPTED.equals(item.getOrderItemStatus()))
                .collect(Collectors.toList()).size() == orderItems.size();
        if (allItemsAccepted && !OrderStatus.ACCEPTED.equals(orderDAO.getOrderStatus())) {
            orderDAO.setOrderStatus(OrderStatus.ACCEPTED);
        }
        boolean allItemsDelivered = orderItems.stream()
                .map(item -> OrderItemStatus.DELIVERED.equals(item.getOrderItemStatus()))
                .collect(Collectors.toList()).size() == orderItems.size();
        if (allItemsDelivered && !OrderStatus.DELIVERED.equals(orderDAO.getOrderStatus())) {
            orderDAO.setOrderStatus(OrderStatus.DELIVERED);
        }
        Double orderItemsPriceSum = orderItems.stream().mapToDouble(item -> item.calculateItemPrice()).sum();
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        if (!orderAmountDAO.getTotalAmount().equals(orderItemsPriceSum)) {
            orderAmountDAO.setTotalAmount(orderItemsPriceSum);
        }
        orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext()));
        orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext()));
        return orderDAO;
    }

    public boolean checkIfBoutiqueIsActive(Long boutiqueId) {
        return orderRepo.countByBoutiqueId(boutiqueId) > 0;
    }

    private Double calculateTotalOrderAmount(List<OrderItemDAO> orderItems) {
        List<PriceBreakupDAO> priceBreakups = orderItems.stream().map(item -> item.getPriceBreakup())
                .flatMap(List::stream).collect(Collectors.toList());
        Double totalAmount = 0d;
        if (!CollectionUtils.isEmpty(priceBreakups)) {
            for (PriceBreakupDAO priceBreakupDAO : priceBreakups) {
                totalAmount += priceBreakupDAO.getValue() * priceBreakupDAO.getQuantity();
            }
        }
        return totalAmount;
    }

    @Transactional
    private void resetOrderAmountForDeletedOrders(OrderDAO orderDAO, OrderStatus status) {
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        Double pendingOrderAmount = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
        Double amountRecieved = orderAmountDAO.getAmountRecieved();
        orderAmountDAO.setAmountRecieved(0d);
        orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext()));
        paymentService.recordPayment(-amountRecieved, PaymentMode.CASH, Boolean.FALSE, orderDAO);
        boutiqueLedgerService.handleBoutiqueLedgerForDeletedOrder(orderDAO.getBoutique().getId(),
                pendingOrderAmount,
                amountRecieved,
                status);
    }

    private String generateOrderInvoiceNo() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

//    @Transactional
//    private void updateLedgerIfApplicable(OrderDAO order, OrderStatus initialStatus) {
//        OrderStatus currentStatus = order.getOrderStatus();
//        Long boutiqueId = order.getBoutique().getId();
//        boutiqueLedgerService.handleBoutiqueLedgerOnOrderUpdation(boutiqueId, initialStatus, currentStatus);
//    }

//    private OrderStage getOrderStage(OrderStatus status) {
//        if (Constants.ACTIVE_ORDER_STATUS_LIST.contains(status)) {
//            return OrderStage.ACTIVE;
//        } else if (Constants.CLOSED_ORDER_STATUS_LIST.contains(status)) {
//            return OrderStage.CLOSED;
//        }
//        return OrderStage.OTHER;
//    }

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
}
