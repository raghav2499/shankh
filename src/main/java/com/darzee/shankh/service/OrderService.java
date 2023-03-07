package com.darzee.shankh.service;

import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.CreateOrderRequest;
import com.darzee.shankh.request.MeasurementDetails;
import com.darzee.shankh.request.innerObjects.OrderDetails;
import com.darzee.shankh.request.innerObjects.PaymentDetails;
import com.darzee.shankh.response.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private PaymentsRepo paymentsRepo;
    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;
    @Autowired
    private MeasurementRepo measurementRepo;
    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;

    @Transactional
    public ResponseEntity createNewOrder(CreateOrderRequest request) throws Exception {
        OrderDetails orderDetails = request.getOrderDetails();
        Optional<Customer> optionalCustomer = customerRepo.findById(orderDetails.getCustomerId());
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(orderDetails.getBoutiqueId());
        if (optionalCustomer.isPresent() && optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(), new CycleAvoidingMappingContext());
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
            OrderDAO orderDAO = new OrderDAO(orderDetails.getTrialDate(), orderDetails.getDeliveryDate(),
                    orderDetails.getOutfitType(), orderDetails.getSpecialInstructions(), orderDetails.getInspiration(),
                    orderDetails.getOrderType(), orderDetails.getIsMeasurementOnPaper(), boutiqueDAO, customerDAO);
            orderDAO = mapper.orderObjectToDao(orderRepo.save(mapper.orderaDaoToObject(orderDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());

            PaymentDetails paymentDetails = request.getPaymentDetails();
            PaymentDAO paymentDAO = new PaymentDAO(paymentDetails.getTotalOrderAmount(),
                    paymentDetails.getAdvanceOrderAmount(), orderDAO);
            paymentsRepo.save(mapper.paymentDaoToObject(paymentDAO, new CycleAvoidingMappingContext()));

            MeasurementDetails measurementDetails = request.getMeasurementDetails();
            MeasurementDAO measurementDAO = Optional.ofNullable(customerDAO.getMeasurement()).orElse(new MeasurementDAO());
            if (measurementDetails.isPresent()) {
                OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(orderDetails.getOutfitType());
                outfitTypeService.setMeasurementDetailsInObject(measurementDetails, measurementDAO);
                measurementDAO.setCustomer(customerDAO);
            }
            measurementDAO = mapper.measurementObjectToDAO(measurementRepo.save(mapper.measurementDAOToObject(measurementDAO,
                    new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());

            if (customerDAO.getMeasurement() == null) {
                customerDAO.setMeasurement(measurementDAO);
                customerRepo.save(mapper.customerDaoToObject(customerDAO, new CycleAvoidingMappingContext()));
            }

            boutiqueDAO.setActiveOrders(boutiqueDAO.getActiveOrders() + 1);
            boutiqueRepo.save(mapper.boutiqueDaoToObject(boutiqueDAO, new CycleAvoidingMappingContext()));

            BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(boutiqueLedgerRepo.findByBoutiqueId(boutiqueDAO.getId()), new CycleAvoidingMappingContext());
            Double pendingOrderAmount = paymentDetails.getTotalOrderAmount() - paymentDetails.getAdvanceOrderAmount();
            boutiqueLedgerDAO.addOrderAmountToBoutiqueLedger(pendingOrderAmount, paymentDetails.getAdvanceOrderAmount());
            boutiqueLedgerRepo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO, new CycleAvoidingMappingContext()));

            CreateOrderResponse successResponse = new CreateOrderResponse(orderDAO.getId(), "Order Created Successfuly");
            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        }
        CreateOrderResponse failureResponse = new CreateOrderResponse(null, "No eligible boutique/customer found");
        return new ResponseEntity<>(failureResponse, HttpStatus.OK);
    }
}
