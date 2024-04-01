package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.*;
import com.darzee.shankh.enums.*;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.BoutiqueDetails;
import com.darzee.shankh.request.CreateOrderRequest;
import com.darzee.shankh.request.RecievePaymentRequest;
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
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private ImageReferenceRepo imageReferenceRepo;

    @Autowired
    private AmazonClient s3Client;

    @Autowired
    private CustomerMeasurementService customerMeasurementService;

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
            String trialDate = orderDAO.getTrialDate() != null ? orderDAO.getTrialDate().toString() : null;

//            boutiqueRepo.save(mapper.boutiqueDaoToObject(boutiqueDAO, new CycleAvoidingMappingContext()));

            return new OrderSummary(orderDAO.getId(), orderDAO.getInvoiceNo(), orderDAO.getOutfitType().getName(),
                    trialDate, orderDAO.getDeliveryDate().toString(),
                    orderAmountDAO.getTotalAmount().toString(), orderAmountDAO.getAmountRecieved().toString());
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
            OutfitType outfitType = order.getOutfitType();
            CustomerDAO customer = order.getCustomer();
            MeasurementsDAO measurementsDAO = customerMeasurementService.getCustomerMeasurements(customer.getId(), outfitType);
            OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(outfitType);
            OutfitMeasurementDetails measurementDetails = outfitTypeService.extractMeasurementDetails(measurementsDAO);
            List<String> clothImagesReferenceIds = objectImagesService.getClothReferenceIds(order.getId());
            List<String> clothImageUrlLinks = getClothProfilePicLink(clothImagesReferenceIds);
            String outfitImageLink = outfitImageLinkService.getOutfitImageLink(outfitType);
            String message = "Details fetched succesfully";
            OrderDetailResponse response = new OrderDetailResponse(customer, order, measurementDetails, orderAmount,
                    clothImagesReferenceIds, clothImageUrlLinks, outfitImageLink, message);
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
            String trialDate = order.getTrialDate() != null ? order.getTrialDate().toString() : null;

            OrderSummary orderSummary = new OrderSummary(order.getId(), order.getInvoiceNo(),
                    order.getOutfitType().getName(), trialDate, order.getDeliveryDate().toString(),
                    orderAmountDAO.getTotalAmount().toString(), orderAmountDAO.getAmountRecieved().toString());
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

        if (Boolean.TRUE.equals(orderDetails.getDeleteOrder())) {
            order.setIsDeleted(Boolean.TRUE);
            OrderStage orderStage = getOrderStage(order.getOrderStatus());
            resetOrderAmountForDeletedOrders(order, orderStage);
        }
        if (!Collections.isEmpty(orderDetails.getClothImageReferenceIds())) {
            Long orderId = order.getId();
            objectImagesService.invalidateExistingReferenceIds(ImageEntityType.ORDER.getEntityType(), orderId);
            objectImagesService.saveObjectImages(orderDetails.getClothImageReferenceIds(), ImageEntityType.ORDER.getEntityType(), orderId);
        }
        order = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(order, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
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
        String outfitImageLink = outfitImageLinkService.getOutfitImageLink(orderDAO.getOutfitType());
        return new OrderDetailResponse(orderDAO.getCustomer(),
                orderDAO,
                customerProfilePicLink,
                orderDAO.getOrderAmount(),
                outfitImageLink);
    }

    private OrderDAO setOrderSpecificDetails(OrderDetails orderDetails, BoutiqueDAO boutiqueDAO, CustomerDAO customerDAO) {
        String invoiceNo = generateOrderInvoiceNo();
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(orderDetails.getOutfitType());
        if (outfitType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid outfit type");
        }
        OrderDAO orderDAO = new OrderDAO(orderDetails.getTrialDate(), orderDetails.getDeliveryDate(), outfitType,
                orderDetails.getSpecialInstructions(), orderDetails.getInspiration(), orderDetails.getOrderType(),
                orderDetails.getIsPriorityOrder(), invoiceNo, boutiqueDAO, customerDAO);
        orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        Long orderId = orderDAO.getId();
        List<String> clothImageReferenceIds = orderDetails.getClothImageReferenceIds();
        objectImagesService.saveObjectImages(clothImageReferenceIds, ImageEntityType.ORDER.getEntityType(), orderId);
        return orderDAO;
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
     * @param weekwiseSales
     */
    private void populateSalesIfRequired(List<WeekwiseSalesSplit> weekwiseSales) {
        while(weekwiseSales.size() < 5) {
            weekwiseSales.add(new WeekwiseSalesSplit(0d, null));
        }
    }

   //get invoice details of an order by order id and boutique id
    public ResponseEntity getOrderInvoiceDetails(Long orderId, Long boutiqueId) {
        Optional<Order> order = orderRepo.findById(orderId);
        if (order.isPresent()) {
            OrderDAO orderDAO = mapper.orderObjectToDao(order.get(), new CycleAvoidingMappingContext());
            OrderAmountDAO orderAmountDAO = orderDAO.getOrderAmount();
            CustomerDAO customerDAO = orderDAO.getCustomer();

            //invoice details 
            String invoice_number = orderDAO.getInvoiceNo();
            LocalDateTime invoiceDate = LocalDateTime.now();
             
          
            //boutique name
            String boutiqueName = orderDAO.getBoutique().getName();
            //customer name 
            String customerName =  CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());;



            //order summary details 
            OutfitType outfitType = orderDAO.getOutfitType();
            double total = orderAmountDAO.getTotalAmount();
            double advance = orderAmountDAO.getAmountRecieved();
            double balanceDue = total - advance;
            LocalDateTime deliveryDate = orderDAO.getDeliveryDate();

        InvoiceDetailsResponse response = new InvoiceDetailsResponse(invoice_number, invoiceDate.format(
            DateTimeFormatter.ISO_DATE_TIME
        ), orderId.intValue(), boutiqueName, customerName, outfitType, total, advance, balanceDue, deliveryDate.format(
            DateTimeFormatter.ISO_DATE_TIME
        ), 
        orderAmountDAO.getStichingCost(), orderAmountDAO.getMaterialCost());
        return new ResponseEntity(response, HttpStatus.OK);    
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order ID");
    }


}
