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
import com.darzee.shankh.request.innerObjects.OrderAmountDetails;
import com.darzee.shankh.request.innerObjects.OrderDetails;
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

import javax.transaction.Transactional;
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
    private MeasurementRepo measurementRepo;
    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;
    @Autowired
    private ObjectImagesRepo objectImagesRepo;

    @Transactional
    public ResponseEntity createNewOrder(CreateOrderRequest request) throws Exception {
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

    public ResponseEntity<List<OrderDetailResponse>> getOrder(Map<String, Object> paramsMap) {
        Specification<Order> orderSpecification = OrderSpecificationClause.getSpecificationBasedOnFilters(paramsMap);
        Pageable pagingCriteria = filterOrderService.getPagingCriteria(paramsMap);
        List<Order> orderDetails = orderRepo.findAll(orderSpecification, pagingCriteria).getContent();
        List<OrderDAO> orderDAOList = orderDetails.stream()
                .map(order -> mapper.orderObjectToDao(order, new CycleAvoidingMappingContext()))
                .collect(Collectors.toList());
        List<OrderDetailResponse> orderDetailsList = orderDAOList.stream()
                .map(order -> getOrderDetails(order))
                .collect(Collectors.toList());
        return new ResponseEntity<>(orderDetailsList, HttpStatus.OK);
    }

    private OrderDetailResponse getOrderDetails(OrderDAO orderDAO) {
        return new OrderDetailResponse(orderDAO.getCustomer(), orderDAO, orderDAO.getOrderAmountDAO());
    }

    private OrderDAO setOrderSpecificDetails(OrderDetails orderDetails, BoutiqueDAO boutiqueDAO, CustomerDAO customerDAO) {
        String invoiceNo = generateOrderInvoiceNo();
        OrderDAO orderDAO = new OrderDAO(orderDetails.getTrialDate(), orderDetails.getDeliveryDate(),
                OutfitType.getOutfitOrdinalEnumMap().get(orderDetails.getOutfitType()), orderDetails.getSpecialInstructions(),
                orderDetails.getInspiration(), orderDetails.getOrderType(), invoiceNo, boutiqueDAO, customerDAO);
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
