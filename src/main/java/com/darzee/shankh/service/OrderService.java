package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.Order;
import com.darzee.shankh.entity.Payment;
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
import com.darzee.shankh.utils.TimeUtils;
import com.darzee.shankh.utils.pdfutils.PdfGenerator;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private ObjectFilesService objectFilesService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private PdfGenerator pdfGenerator;

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
            try {
                orderDAO = findOrder(orderId, boutiqueId);
                return orderDAO;
            } catch (Exception e) {
                return null;
            }
        }

        orderDAO = createNewOrder(boutiqueId, customerId);
        return orderDAO;
    }

    @Transactional
    public OrderDAO createNewOrder(Long boutiqueId, Long customerId) {
        Optional<Customer> optionalCustomer = customerRepo.findById(customerId);
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(boutiqueId);
        if (optionalCustomer.isPresent() && optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(), new CycleAvoidingMappingContext());
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
            OrderAmountDAO orderAmountDAO = new OrderAmountDAO();
            orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
            OrderDAO orderDAO = setOrderSpecificDetails(boutiqueDAO, customerDAO);
            orderDAO.setOrderAmount(orderAmountDAO);
            orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
            return orderDAO;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid customer id or boutique id");
    }

    public ResponseEntity<GetOrderResponse> getOrder(Long boutiqueId, String orderItemStatusList,
                                                     String orderStatusList, Boolean priorityOrdersOnly,
                                                     Long customerId, String deliveryDateFrom,
                                                     String deliveryDateTill, Boolean paymentDue,
                                                     Integer countPerPage, Integer pageCount) {
        validateGetOrderRequest(orderItemStatusList, orderStatusList);
        Map<String, Object> filterMap = GetOrderDetailsRequest.getFilterMap(boutiqueId, orderItemStatusList,
                orderStatusList, priorityOrdersOnly, customerId, deliveryDateFrom, deliveryDateTill,
                null, paymentDue);
        String defaultOrderSortKey = "created_at";
        String defaultOrderSortOrder = "desc";
        Map<String, Object> pagingCriteriaMap = GetOrderDetailsRequest.getPagingCriteria(countPerPage, pageCount, defaultOrderSortKey, defaultOrderSortOrder);
        Specification<Order> orderSpecification = OrderSpecificationClause.getSpecificationBasedOnFilters(filterMap);
        Pageable pagingCriteria = filterOrderService.getPagingCriteria(pagingCriteriaMap);
        List<Order> orderDetails = orderRepo.findAll(orderSpecification, pagingCriteria).getContent();
        Long totalRecordsCount = orderRepo.count(orderSpecification);
        List<OrderDAO> orderDAOList = mapper.orderObjectListToDAOList(orderDetails, new CycleAvoidingMappingContext());
        List<OrderItemStatus> orderItemStatuses = Arrays.asList(OrderItemStatus.values());
        if (orderItemStatusList != null) {
            List<Integer> eligibleStatuses = Arrays.stream(orderItemStatusList.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            orderItemStatuses = orderItemStatuses.stream().filter(orderItemStatus -> eligibleStatuses.contains(orderItemStatus.getOrdinal())).collect(Collectors.toList());
        }
        List<OrderItemStatus> finalOrderItemStatuses = orderItemStatuses;
        List<OrderDetailResponse> orderDetailsList = orderDAOList.stream().map(order -> {
            try {
                return getOrderDetails(order, finalOrderItemStatuses);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        GetOrderResponse response = new GetOrderResponse(orderDetailsList, totalRecordsCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity getOrderDetails(Long orderId, Long boutiqueId) throws Exception {
        OrderDAO order = findOrder(orderId, boutiqueId);
        OrderAmountDAO orderAmount = order.getOrderAmount();
        CustomerDAO customer = order.getCustomer();
        List<OrderItemDetails> orderItemDetailsList = new ArrayList<>();
        for (OrderItemDAO item : order.getNonDeletedItems()) {
            orderItemDetailsList.add(new OrderItemDetails(item, null));
        }
        Integer paymentMode = getOrderPaymentMode(order.getId());
        String message = "Details fetched succesfully";
        OrderDetailResponse response = new OrderDetailResponse(customer, order, orderAmount, orderItemDetailsList, paymentMode, message);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Transactional
    public OrderDAO confirmOrder(Long orderId, Long boutiqueId, OrderCreationRequest request) throws Exception {
        OrderDAO orderDAO = findOrder(orderId, boutiqueId);
        validateOrderAmountInRequest(request.getOrderDetails().getOrderAmountDetails());
        if (!orderDAO.validateMandatoryOrderFields()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Some mandatory fields in order are missing." + " Boutique ID and customer ID are mandatory fields");
        }
        orderItemService.validateMandatoryOrderItemFields(orderDAO.getNonDeletedItems());

        Double priceBreakupSum = orderDAO.getPriceBreakupSum();
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        Double totalOrderAmount = orderAmountDAO.getTotalAmount();

        if (!priceBreakupSum.equals(totalOrderAmount)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Items' Price Breakups are not summing up to total amount. " + "Total amount " + totalOrderAmount + " and price break up sum " + priceBreakupSum);
        }
        orderDAO.setOrderStatus(OrderStatus.ACCEPTED);
        orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        orderItemService.acceptOrderItems(orderDAO.getNonDeletedItems());

        Double advanceRecieved = request.getOrderDetails().getOrderAmountDetails().getAdvanceReceived();
        if (advanceRecieved != null && advanceRecieved > 0) {
            orderAmountDAO.setAmountRecieved(advanceRecieved);
            orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(
                            mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
            orderDAO.setOrderAmount(orderAmountDAO);
            paymentService.recordPayment(advanceRecieved, PaymentMode.CASH, Boolean.TRUE, orderDAO);
        }

        updateBoutiqueLedgerOnOrderConfirmation(orderAmountDAO, orderDAO);
        return orderDAO;
    }

    public SalesDashboard getWeekWiseSales(Long boutiqueId, int month, int year) {
        ZoneId clientZoneId = ZoneId.of("Asia/Kolkata");
        LocalDateTime monthStart = TimeUtils.getTimeInDBTimeZone(LocalDateTime.of(year, month, 1, 0, 0, 0), clientZoneId);
        LocalDateTime nextMonthStart = TimeUtils.getTimeInDBTimeZone(monthStart.plusMonths(1), clientZoneId);
        List<Object[]> orderAmountsOfMonth = orderRepo.getOrderAmountsBetweenTheDates(boutiqueId, monthStart,
                nextMonthStart);
        TreeMap<LocalDate, Double> weekWiseCollatedAmounts = new TreeMap<>();
        for (Object[] orderAmountDetail : orderAmountsOfMonth) {
            LocalDate key = TimeUtils.getWeekStartDate((Timestamp) orderAmountDetail[2]);
            Double currentValue = weekWiseCollatedAmounts.getOrDefault(key, 0d);
            weekWiseCollatedAmounts.put(key, currentValue + (Double) orderAmountDetail[1]);
        }
        List<WeekwiseSalesSplit> weeklySalesAmount = new ArrayList<>();
        LocalDate dayInAWeek = LocalDate.of(year, month, 1);
        for (int weekCount = 0; weekCount < 5; weekCount++) {
            LocalDate weekStartDate = TimeUtils.getWeekStartDate(dayInAWeek);
            weeklySalesAmount.add(new WeekwiseSalesSplit(weekWiseCollatedAmounts.getOrDefault(weekStartDate, 0d), weekStartDate));
            dayInAWeek = dayInAWeek.plusDays(7);
        }
        return new SalesDashboard(weeklySalesAmount);
    }

    public List<OrderTypeDashboardData> getOrderTypeWiseSales(Long boutiqueId, int month, int year) {
        ZoneId clientZoneId = ZoneId.of("Asia/Kolkata");
        LocalDateTime monthStart = TimeUtils.getTimeInDBTimeZone(LocalDateTime.of(year, month, 1, 0, 0, 0), clientZoneId);
        LocalDateTime nextMonthStart = TimeUtils.getTimeInDBTimeZone(monthStart.plusMonths(1), clientZoneId);
        List<Object[]> orderDataBetweenTheDates = orderRepo.getOrderTypeBasedOrderAmountData(boutiqueId, monthStart, nextMonthStart);
        Map<OrderType, Double> orderTypeAmountMap = new HashMap<>(OrderType.values().length);

        //order amount data grouped by order type
        for (Object[] orderTypeSales : orderDataBetweenTheDates) {
            Integer orderTypeDbOrdinal = (Integer) orderTypeSales[2];
            if (orderTypeDbOrdinal == null) {
                continue;
            }
            OrderType orderType = OrderType.values()[orderTypeDbOrdinal];
            Double value = (Double) orderTypeSales[1];
            Double updatedValue = orderTypeAmountMap.getOrDefault(orderType, 0d) + value;
            orderTypeAmountMap.put(orderType, updatedValue);
        }
        List<OrderTypeDashboardData> orderTypeDashboardData = new ArrayList<>();
        for (OrderType orderType : OrderType.values()) {
            OrderTypeDashboardData orderTypeData = null;
            if (orderTypeAmountMap.containsKey(orderType)) {
                orderTypeData = new OrderTypeDashboardData(orderType.getDisplayName(), orderTypeAmountMap.get(orderType));
            } else {
                orderTypeData = new OrderTypeDashboardData(orderType.getDisplayName(), 0d);
            }
            orderTypeDashboardData.add(orderTypeData);
        }
        return orderTypeDashboardData;
    }

    public List<TopCustomerData> getTopCustomerData(Long boutiqueId, int month, int year) {
        ZoneId clientZoneId = ZoneId.of("Asia/Kolkata");
        LocalDateTime monthStart = TimeUtils.getTimeInDBTimeZone(LocalDateTime.of(year, month, 1, 0, 0, 0), clientZoneId);
        LocalDateTime nextMonthStart = TimeUtils.getTimeInDBTimeZone(monthStart.plusMonths(1), clientZoneId);
        List<Object[]> topCustomerSalesDetails = orderRepo.getTopCustomersByTotalAmount(boutiqueId, monthStart, nextMonthStart);
        Map<Long, Double> customerTotalSales = topCustomerSalesDetails.stream()
                .collect(Collectors.groupingBy(
                        obj -> ((BigInteger) obj[2]).longValue(), // Group by customer ID (Object[2])
                        Collectors.summingDouble(obj -> (Double) obj[1]) // Sum the amounts (Object[1])
                ));
        Map<Long, Double> topCustomers = customerTotalSales.entrySet().stream().sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(2).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<TopCustomerData> topCustomerDataList = new ArrayList<>(2);
        for (Map.Entry<Long, Double> topCustomer : topCustomers.entrySet()) {
            Long customerId = topCustomer.getKey();
            Double salesByCustomer = topCustomer.getValue();
            CustomerDetails customerDetails = customerService.getCustomerDetails(customerId);
            TopCustomerData topCustomerData = new TopCustomerData(customerDetails.getCustomerId(), customerDetails.getCustomerName(), customerDetails.getPhoneNumber(), salesByCustomer, customerService.getCustomerProfilePicLink(customerId));
            topCustomerDataList.add(topCustomerData);
        }
        return topCustomerDataList;
    }

    @Transactional
    public ResponseEntity recieveOrderPayment(Long orderId, Long boutiqueId, RecievePaymentRequest request) throws Exception {
        OrderDAO orderDAO = findOrder(orderId, boutiqueId);
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        Double pendingAmount = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
        Double amountRecieved = request.getAmount();
        if (amountRecieved > pendingAmount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount recieved is greater than pending order amount. Amount Received : " + amountRecieved + " Pending Amount : " + pendingAmount);
        }
        orderAmountDAO.setAmountRecieved(orderAmountDAO.getAmountRecieved() + amountRecieved);
        orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext()));

        boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(null, orderDAO.getBoutiqueId(), -amountRecieved, amountRecieved);
        String message = "Order payment recorded successfully";
        Double pendingAmountLeft = pendingAmount - amountRecieved;
        PaymentMode paymentMode = PaymentMode.getPaymentTypeEnumOrdinalMap().get(request.getPaymentMode());
        paymentService.recordPayment(amountRecieved, paymentMode, Boolean.FALSE, orderDAO);
        generateInvoiceV2(orderDAO);
        RecievePaymentResponse response = new RecievePaymentResponse(message, orderId, pendingAmountLeft);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public String generateInvoice(OrderDAO orderDAO) {
        CustomerDAO customerDAO = orderDAO.getCustomer();
        String customerName = customerDAO.constructName();
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        BoutiqueDAO boutique = orderDAO.getBoutique();
        File bill = pdfGenerator.generateBill(customerName, customerDAO.getPhoneNumber(), orderDAO, orderAmountDAO, boutique);
        String fileUploadUrl = bucketService.uploadInvoice(bill, orderDAO.getId());
        bill.delete();
        return fileUploadUrl;
    }

    public String generateInvoiceV2(OrderDAO orderDAO) {
        CustomerDAO customerDAO = orderDAO.getCustomer();
        String customerName = customerDAO.constructName();
        BoutiqueDAO boutique = orderDAO.getBoutique();
        TailorDAO tailorDAO = boutique.getAdminTailor();

        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();

        File bill = pdfGenerator.generateBillV2(boutique, customerName, tailorDAO.getPhoneNumber(), orderDAO);
        Long orderNo = orderDAO.getId();
        String fileUploadUrl = bucketService.uploadInvoice(bill, orderNo);
        bill.delete();
        return fileUploadUrl;
    }

    public ResponseEntity<InvoiceDetailResponse> getInvoiceDetail(Long orderId, Long boutiqueId) {
        try {
            OrderDAO orderDAO = findOrder(orderId, boutiqueId);
            if (orderDAO == null) {
                return new ResponseEntity("Order not found", HttpStatus.NOT_FOUND);
            }

            OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
            String invoiceDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            String boutiqueName = orderDAO.getBoutique().getName();
            String customerName = orderDAO.getCustomer().constructName();
            String recieveDateTime = orderDAO.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME);
            OrderSummary summary = new OrderSummary(orderDAO.getId(), orderDAO.getBoutiqueOrderId(),
                    orderDAO.getInvoiceNo(), orderAmountDAO.getTotalAmount(), orderAmountDAO.getAmountRecieved(),
                    orderDAO.getNonDeletedItems());

            Integer paymentMode = getOrderPaymentMode(orderDAO.getId());
            InvoiceDetailResponse response = new InvoiceDetailResponse(invoiceDateTime, boutiqueName, customerName, recieveDateTime, summary, paymentMode);

            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private OrderDetailResponse getOrderDetails(OrderDAO orderDAO, List<OrderItemStatus> eligibleStatuses) throws Exception {
        String customerProfilePicLink = customerService.getCustomerProfilePicLink(orderDAO.getCustomer().getId());
        List<Pair<OrderItemDAO, String>> orderItemOutfitLinkPairList = new ArrayList<>();
        List<OrderItemDAO> orderItems = orderDAO.getNonDeletedItems().stream().filter(item -> eligibleStatuses.contains(item.getOrderItemStatus())).collect(Collectors.toList());
        for (OrderItemDAO orderItem : orderItems) {
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(orderItem.getOutfitType());
            String outfitImgLink = outfitTypeService.getOutfitImageLink();
            orderItemOutfitLinkPairList.add(Pair.of(orderItem, outfitImgLink));
        }
        return new OrderDetailResponse(orderDAO, orderItemOutfitLinkPairList, customerProfilePicLink);
    }

    private OrderDAO setOrderSpecificDetails(BoutiqueDAO boutiqueDAO, CustomerDAO customerDAO) {
        String invoiceNo = generateOrderInvoiceNo();
        OrderDAO orderDAO = new OrderDAO(invoiceNo, boutiqueDAO, customerDAO);
        return orderDAO;
    }

    @Transactional
    public OrderAmountDAO setOrderAmountSpecificDetails(OrderAmountDetails orderAmountDetails, OrderDAO orderDAO) {
        validateOrderAmountInRequest(orderAmountDetails);
        Double amountRecieved = 0d;
        if (orderAmountDetails != null && orderAmountDetails.getAdvanceReceived() != null) {
            amountRecieved = orderAmountDetails.getAdvanceReceived();
        }
        Double totalOrderAmount = calculateTotalOrderAmount(orderDAO.getNonDeletedItems());
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        orderAmountDAO.setTotalAmount(totalOrderAmount);
        orderAmountDAO.setAmountRecieved(amountRecieved);
        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return orderAmountDAO;
    }

    private void updateBoutiqueLedgerOnOrderConfirmation(OrderAmountDAO orderAmount, OrderDAO orderDAO) {
        Double amountRecieved = orderAmount.getAmountRecieved();
        Double amountPending = orderAmount.getTotalAmount() - amountRecieved;
        BoutiqueLedgerDAO ledger = boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(null, orderDAO.getBoutiqueId(), amountPending, amountRecieved);
        boutiqueLedgerService.updateCountOnOrderConfirmation(ledger, orderDAO.getBoutiqueId());
    }

    /**
     * 1.Update order status if applicable
     * a. Mark order as accepted, if all items are accepted
     * b. Mark order as delovered if all items are delivered
     * 2. Delete order if all items are deleted
     * 3. Adjust order amount
     * 4. Adjust boutique ledger amounts
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public OrderDAO updateOrderPostItemUpdation(OrderDAO orderDAO, Double refundAmount, Boolean shouldUpdateLedger) {
        refundAmount = Optional.ofNullable(refundAmount).orElse(0d);
        BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(boutiqueLedgerRepo.findByBoutiqueId(orderDAO.getBoutiqueId()), new CycleAvoidingMappingContext());
        List<OrderItemDAO> orderItems = orderDAO.getNonDeletedItems();

        boolean allItemsAccepted = (orderItems.size() > 0 &&
                orderItems.stream()
                        .filter(item -> OrderItemStatus.ACCEPTED.equals(item.getOrderItemStatus()))
                        .collect(Collectors.toList()).size() == orderItems.size());
        if (allItemsAccepted && !OrderStatus.ACCEPTED.equals(orderDAO.getOrderStatus())) {
            orderDAO.setOrderStatus(OrderStatus.ACCEPTED);
            boutiqueLedgerDAO = boutiqueLedgerService.handleBoutiqueLedgerOnOrderUpdation(boutiqueLedgerDAO, orderDAO.getBoutiqueId(), 1, 0);
        }
        boolean allItemsDelivered = (orderItems.size() > 0
                && orderItems.stream()
                .filter(item -> OrderItemStatus.DELIVERED.equals(item.getOrderItemStatus()))
                .collect(Collectors.toList()).size() == orderItems.size());
        if (allItemsDelivered && !OrderStatus.DELIVERED.equals(orderDAO.getOrderStatus())) {
            orderDAO.setOrderStatus(OrderStatus.DELIVERED);
            boutiqueLedgerDAO = boutiqueLedgerService.handleBoutiqueLedgerOnOrderUpdation(boutiqueLedgerDAO, orderDAO.getBoutiqueId(), -1, 1);
        }


        boolean allItemsDeleted = (orderItems.size() == 0);
        if (allItemsDeleted && !Boolean.TRUE.equals(orderDAO.getIsDeleted())) {
            orderDAO.setIsDeleted(Boolean.TRUE);
            if (shouldUpdateLedger) {
                boutiqueLedgerDAO = boutiqueLedgerService.handleBoutiqueLedgerOnOrderDeletion(boutiqueLedgerDAO,
                        orderDAO.getBoutiqueId(), orderDAO);
            }
        }


        Double orderItemsPriceSum = orderItems.stream().mapToDouble(item -> item.calculateItemPrice()).sum();
        OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
        Double orderAmountDelta = 0d;
        if (!orderAmountDAO.getTotalAmount().equals(orderItemsPriceSum)) {
            orderAmountDelta = orderItemsPriceSum - orderAmountDAO.getTotalAmount();
            orderAmountDAO.setTotalAmount(orderItemsPriceSum);
            if (refundAmount > orderAmountDAO.getAmountRecieved()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refund cannot be greater than advance received");
            }
            orderAmountDAO.setAmountRecieved(orderAmountDAO.getAmountRecieved() - refundAmount);
        }
        orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext()));
        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO,
                new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        orderDAO.setOrderAmount(orderAmountDAO);


        if (Boolean.FALSE.equals(shouldUpdateLedger)) {
            return orderDAO;
        }
        Double deltaAmountRecieved = 0d;
        if (orderAmountDelta != 0) {
            if (refundAmount > 0) {
                if (refundAmount > -orderAmountDelta) {
                    throw new RuntimeException("Amount refund could not be greater than order amount change. " +
                            "Order Amount changed by " + (-orderAmountDelta) + " and we cannot refund " + refundAmount);
                }
                deltaAmountRecieved = -refundAmount;
            }
        }
        Double deltaPendingAmount = orderAmountDelta - deltaAmountRecieved;
        boutiqueLedgerService.updateBoutiqueLedgerAmountDetails(boutiqueLedgerDAO, orderDAO.getBoutiqueId(), deltaPendingAmount,
                deltaAmountRecieved);
        return orderDAO;
    }

    public Pair getItemsCount(Long boutiqueId, LocalDateTime startTime, LocalDateTime endTime) {
        Integer newItemsCount = orderRepo.getNewItemsCount(boutiqueId, startTime, endTime);
        Integer closedItemsCount = orderRepo.getCompletedItemsCount(boutiqueId, startTime, endTime);
        return Pair.of(newItemsCount, closedItemsCount);
    }

    public boolean checkIfBoutiqueIsActive(Long boutiqueId) {
        return orderRepo.countByBoutiqueId(boutiqueId) > 0;
    }

    public Integer getOrderPaymentMode(Long orderId) {
        Integer paymentMode = null;
        Optional<Payment> payment = paymentRepo.findTopByOrderIdOrderByIdDesc(orderId);
        if (payment.isPresent()) {
            PaymentDAO paymentDAO = mapper.paymentToPaymentDAO(payment.get(), new CycleAvoidingMappingContext());
            paymentMode = paymentDAO.getPaymentMode().getOrdinal();
        }
        return paymentMode;
    }

    private Double calculateTotalOrderAmount(List<OrderItemDAO> orderItems) {
        List<PriceBreakupDAO> priceBreakups = orderItems.stream().map(item -> item.getActivePriceBreakUpList()).flatMap(List::stream).collect(Collectors.toList());
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

    private void postUpdateOrderValidation(Double totalOrderAmount, List<PriceBreakupDAO> allItemsPriceBreakup) {
        Double itemsPriceBreakupSum = allItemsPriceBreakup.stream().mapToDouble(PriceBreakupDAO::getValue).sum();
        if (!itemsPriceBreakupSum.equals(totalOrderAmount)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either price break up or total order amount is incorrect");
        }
    }

    private void validateGetOrderRequest(String orderStatusList, String orderItemStatusList) {
        if (orderStatusList == null && orderItemStatusList == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either order status or item status is necessary to get orders");
        }
    }

    public OrderDAO findOrder(Long orderId, Long boutiqueId) throws Exception {
        Optional<Order> order = orderRepo.findById(orderId);
        if (!order.isPresent()) {
            throw new Exception("Invalid order ID");
        }
        OrderDAO orderDAO = mapper.orderObjectToDao(order.get(), new CycleAvoidingMappingContext());
        return orderDAO;
    }

    private void validateOrderAmountInRequest(OrderAmountDetails orderAmountDetails) {
        if (orderAmountDetails.getAdvanceReceived() != null
                && orderAmountDetails.getAdvanceReceived() > orderAmountDetails.getTotalAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Advance Received could not be greater than Total Order Amount");
        }
    }

}
