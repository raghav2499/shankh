package com.darzee.shankh.service;

import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.Order;
import com.darzee.shankh.enums.ImageEntityType;
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
import com.darzee.shankh.response.CreateOrderResponse;
import com.darzee.shankh.response.OrderDetailResponse;
import com.darzee.shankh.utils.CommonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
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
    private MeasurementRepo measurementRepo;
    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;
    @Autowired
    private ObjectImagesRepo objectImagesRepo;

    @Transactional
    public ResponseEntity createNewOrder(CreateOrderRequest request) {
        OrderDetails orderDetails = request.getOrderDetails();
        OrderAmountDetails orderAmountDetails = request.getOrderAmountDetails();
        Optional<Customer> optionalCustomer = customerRepo.findById(orderDetails.getCustomerId());
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(orderDetails.getBoutiqueId());
        if (optionalCustomer.isPresent() && optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(), new CycleAvoidingMappingContext());
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());

            OrderDAO orderDAO = setOrderSpecificDetails(orderDetails, boutiqueDAO, customerDAO);
            OrderAmountDAO orderAmountDAO = setOrderAmountSpecificDetails(orderAmountDetails, orderDAO);

            boutiqueDAO.setActiveOrders(boutiqueDAO.getActiveOrders() + 1);
            boutiqueRepo.save(mapper.boutiqueDaoToObject(boutiqueDAO, new CycleAvoidingMappingContext()));

            setBoutiqueLedgerSpecificDetails(orderAmountDAO, boutiqueDAO.getId());

            CreateOrderResponse successResponse = new CreateOrderResponse("Order created successfully", orderDAO.getInvoiceNo(),
                    orderDAO.getOutfitType().getName(), orderDAO.getTrialDate().toString(), orderDAO.getDeliveryDate().toString(),
                    orderAmountDAO.getTotalAmount().toString(), orderAmountDAO.getAmountRecieved().toString());
            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        }
        CreateOrderResponse failureResponse = new CreateOrderResponse("No eligible boutique/customer found");
        return new ResponseEntity<>(failureResponse, HttpStatus.OK);
    }

    public ResponseEntity<List<OrderDetailResponse>> getOrder(Map<String, Object> filterMap, Map<String, Object> pagingSortingMap) {
        Specification<Order> orderSpecification = OrderSpecificationClause.getSpecificationBasedOnFilters(filterMap);
        Pageable pagingCriteria = filterOrderService.getPagingCriteria(pagingSortingMap);
        List<Order> orderDetails = orderRepo.findAll(orderSpecification, pagingCriteria).getContent();
        List<OrderDAO> orderDAOList = Optional.ofNullable(orderDetails).orElse(new ArrayList<>()).stream()
                .map(order -> mapper.orderObjectToDao(order, new CycleAvoidingMappingContext()))
                .collect(Collectors.toList());
        List<OrderDetailResponse> orderDetailsList = orderDAOList.stream()
                .map(order -> getOrderDetails(order))
                .collect(Collectors.toList());
        return new ResponseEntity<>(orderDetailsList, HttpStatus.OK);
    }

    public ResponseEntity updateOrder(Long orderId, UpdateOrderRequest request) {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderDAO order = mapper.orderObjectToDao(optionalOrder.get(), new CycleAvoidingMappingContext());
            OrderAmountDAO orderAmountDAO = order.getOrderAmountDAO();
            UpdateOrderDetails orderDetails = request.getOrderDetails();
            UpdateOrderAmountDetails orderAmountDetails = request.getOrderAmountDetails();
            if (orderDetails != null) {
                order = updateOrderDetails(orderDetails, order);
            }
            if (orderAmountDetails != null) {
                orderAmountDAO = updateOrderAmountDetails(orderAmountDetails, orderAmountDAO);
            }
            CreateOrderResponse response = new CreateOrderResponse("Order updated successfully",
                    order.getInvoiceNo(), order.getOutfitType().getName(), order.getTrialDate().toString(),
                    order.getDeliveryDate().toString(), orderAmountDAO.getTotalAmount().toString(),
                    orderAmountDAO.getAmountRecieved().toString());
            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID is invalid");
    }

    private OrderDAO updateOrderDetails(UpdateOrderDetails orderDetails, OrderDAO order) {
        if (orderDetails.getStatus() != null) {
            if (orderStateMachineService.isTransitionAllowed(order.getOrderStatus(), orderDetails.getStatus())) {
                order.setOrderStatus(orderDetails.getStatus());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "State transition from " + order.getOrderStatus() + " to " + orderDetails.getStatus()
                                + " is not allowed");
            }
        }
        if (orderDetails.getTrialDate() != null) {
            order.setTrialDate(orderDetails.getTrialDate());
        }
        if (orderDetails.getDeliveryDate() != null) {
            order.setDeliveryDate(orderDetails.getDeliveryDate());
        }
        if (orderDetails.getIsPriorityOrder() != null) {
            order.setIsPriorityOrder(orderDetails.getIsPriorityOrder());
        }
        if (orderDetails.getInspiration() != null) {
            order.setInspiration(orderDetails.getInspiration());
        }
        if (orderDetails.getSpecialInstructions() != null) {
            order.setSpecialInstructions(orderDetails.getSpecialInstructions());
        }
        order = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(order,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return order;
    }

    private OrderAmountDAO updateOrderAmountDetails(UpdateOrderAmountDetails orderAmountDetails,
                                                    OrderAmountDAO orderAmount) {
        Double totalAmount = Optional.ofNullable(orderAmountDetails.getTotalOrderAmount())
                .orElse(orderAmount.getTotalAmount());
        Double advancePayment = Optional.ofNullable(orderAmountDetails.getAdvanceOrderAmount())
                .orElse(orderAmount.getAmountRecieved());
        if (advancePayment > totalAmount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Amount paid is greater than total amount. Amount Paid "
                            + advancePayment
                            + " and total amount "
                            + totalAmount);
        }
        orderAmount.setTotalAmount(totalAmount);
        orderAmount.setAmountRecieved(advancePayment);
        orderAmount = mapper.orderAmountObjectToOrderAmountDao(
                orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmount,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return orderAmount;
    }

    private OrderDetailResponse getOrderDetails(OrderDAO orderDAO) {
        return new OrderDetailResponse(orderDAO.getCustomer(), orderDAO, orderDAO.getOrderAmountDAO());
    }

    private OrderDAO setOrderSpecificDetails(OrderDetails orderDetails, BoutiqueDAO boutiqueDAO, CustomerDAO customerDAO) {
        String invoiceNo = generateOrderInvoiceNo();
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(orderDetails.getOutfitType());
        if (outfitType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid outfit type");
        }
        OrderDAO orderDAO = new OrderDAO(orderDetails.getTrialDate(), orderDetails.getDeliveryDate(),
                outfitType, orderDetails.getSpecialInstructions(), orderDetails.getInspiration(),
                orderDetails.getOrderType(), invoiceNo, boutiqueDAO, customerDAO);
        orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        Long orderId = orderDAO.getId();
        List<String> clothImageReferenceIds = orderDetails.getClothImageReferenceIds();
        List<ObjectImagesDAO> objectImagesDAOList = clothImageReferenceIds.stream()
                .map(clothImageReferenceId -> new ObjectImagesDAO(clothImageReferenceId, ImageEntityType.ORDER.getEntityType(), orderId))
                .collect(Collectors.toList());
        objectImagesRepo.saveAll(CommonUtils.mapList(objectImagesDAOList, mapper::objectImageDAOToObjectImage));
        return orderDAO;
    }

    private OrderAmountDAO setOrderAmountSpecificDetails(OrderAmountDetails orderAmountDetails, OrderDAO orderDAO) {
        Double advanceRecieved = Optional.ofNullable(orderAmountDetails.getAdvanceOrderAmount()).orElse(0d);
        OrderAmountDAO orderAmountDAO = new OrderAmountDAO(orderAmountDetails.getTotalOrderAmount(),
                advanceRecieved, orderDAO);
        orderAmountDAO.setOrderDAO(orderDAO);
        orderAmountDAO = mapper.orderAmountObjectToOrderAmountDao(orderAmountRepo.save(mapper.orderAmountDaoToOrderAmountObject(orderAmountDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return orderAmountDAO;
    }

    private BoutiqueLedgerDAO setBoutiqueLedgerSpecificDetails(OrderAmountDAO orderAmountDAO, Long boutiqueId) {

        BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(boutiqueLedgerRepo.findByBoutiqueId(boutiqueId),
                new CycleAvoidingMappingContext());
        Double pendingOrderAmount = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
        boutiqueLedgerDAO.addOrderAmountToBoutiqueLedger(pendingOrderAmount, orderAmountDAO.getAmountRecieved());
        boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(boutiqueLedgerRepo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return boutiqueLedgerDAO;

    }

    private String generateOrderInvoiceNo() {
        return RandomStringUtils.randomAlphanumeric(6);
    }
}
