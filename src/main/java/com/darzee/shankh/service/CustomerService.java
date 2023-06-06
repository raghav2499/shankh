package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.CreateCustomerRequest;
import com.darzee.shankh.request.UpdateCustomerRequest;
import com.darzee.shankh.response.CreateCustomerResponse;
import com.darzee.shankh.response.CustomerDashboard;
import com.darzee.shankh.response.CustomerDetails;
import com.darzee.shankh.response.GetCustomersResponse;
import com.darzee.shankh.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private ObjectImagesService objectImagesService;

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ImageReferenceRepo imageReferenceRepo;

    @Autowired
    private AmazonClient s3Client;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;

    @Autowired
    private MeasurementRepo measurementRepo;

    public ResponseEntity getCustomers(Long boutiqueId) {
        List<CustomerDAO> boutiqueCustomers =
                mapper.customerObjectListToDAOList(customerRepo.findAllByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
        boutiqueCustomers.sort(Comparator.comparing(CustomerDAO::getFirstName));
        List<CustomerDetails> customerDetails = boutiqueCustomers
                .stream()
                .map(customer ->
                        new CustomerDetails(customer,
                                getCustomerProfilePicLink(customer.getId()),
                                getCustomerRevenue(customer)))
                .collect(Collectors.toList());

        return new ResponseEntity(new GetCustomersResponse(customerDetails), HttpStatus.OK);
    }

    public ResponseEntity getCustomer(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepo.findById(customerId);
        if (optionalCustomer.isPresent()) {
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
            String profilePicLink = getCustomerProfilePicLink(customerDAO.getId());
            CustomerDetails customerDetails = new CustomerDetails(customerDAO, profilePicLink);
            return new ResponseEntity(customerDetails, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Customer id");
    }

    public CustomerDetails getCustomerDetails(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepo.findById(customerId);
        if (optionalCustomer.isPresent()) {
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
            CustomerDetails customerDetails = new CustomerDetails(customerDAO);
            return customerDetails;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Customer id");
    }

    public ResponseEntity createCustomer(CreateCustomerRequest request) {
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(request.getBoutiqueId());
        CreateCustomerResponse response = new CreateCustomerResponse();
        if (optionalBoutique.isPresent()) {
            String phoneNumber = CommonUtils.sanitisePhoneNumber(request.getPhoneNumber());
            Optional<Customer> existingCustomer = customerRepo.findByBoutiqueIdAndPhoneNumber(optionalBoutique.get().getId(), phoneNumber);
            if (existingCustomer.isPresent()) {
                CustomerDAO customerDAO = mapper.customerObjectToDao(existingCustomer.get(), new CycleAvoidingMappingContext());
                String customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
                response = new CreateCustomerResponse(customerName, customerDAO.getPhoneNumber(), "",
                        customerDAO.getId(), "Customer already registered");
                return new ResponseEntity(response, HttpStatus.OK);
            }

            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(),
                    new CycleAvoidingMappingContext());
            ImmutablePair<String, String> name = getCustomerNameFromRequest(request.getName());

            CustomerDAO customerDAO = new CustomerDAO(request.getAge(), phoneNumber, name.getKey(),
                    name.getValue(), request.getGender(), boutiqueDAO);
            customerDAO = mapper.customerObjectToDao(customerRepo.save(mapper.customerDaoToObject(customerDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());

            if (request.getCustomerImageReferenceId() != null) {
                saveCustomerImages(customerDAO, request.getCustomerImageReferenceId());
            }

            MeasurementDAO measurementDAO = measurementService.createCustomerMeasurement(customerDAO);
            customerDAO.setMeasurement(measurementDAO);
            customerDAO = mapper.customerObjectToDao(customerRepo.save(mapper.customerDaoToObject(customerDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());

            String customerName = CommonUtils.constructName(customerDAO.getFirstName(), customerDAO.getLastName());
            response = new CreateCustomerResponse(customerName, customerDAO.getPhoneNumber(), "",
                    customerDAO.getId(), "Customer created successfully");
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        response.setMessage("This boutique is not enrolled with us");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public ResponseEntity updateCustomer(Long customerId, UpdateCustomerRequest request) {
        Optional<Customer> optionalCustomer = customerRepo.findById(customerId);
        Long boutiqueId = request.getBoutiqueId();
        if (optionalCustomer.isPresent()) {
            CustomerDAO customer = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());

            if (customer.isPhoneNumberUpdated(request.getPhoneNumber())) {
                String phoneNumber = CommonUtils.sanitisePhoneNumber(request.getPhoneNumber());
                Optional<Customer> customerWithSameNumber = customerRepo.findByBoutiqueIdAndPhoneNumber(boutiqueId, phoneNumber);
                if (customerWithSameNumber.isPresent()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Sorry! Customer with same phone number is already registered with this boutique");
                }
                customer.setPhoneNumber(phoneNumber);
            }

            if (request.getName() != null) {
                ImmutablePair<String, String> destructedName = getCustomerNameFromRequest(request.getName());
                if (customer.isFirstNameUpdated(destructedName.getLeft())) {
                    customer.setFirstName(destructedName.getLeft());
                }
                if (customer.isLastNameUpdated(destructedName.getRight())) {
                    customer.setLastName(destructedName.getRight());
                }
            }

            if (customer.isAgeUpdated(request.getAge())) {
                customer.setAge(request.getAge());
            }

            customer = mapper.customerObjectToDao(customerRepo.save(mapper.customerDaoToObject(customer,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
            String customerName = CommonUtils.constructName(customer.getFirstName(), customer.getLastName());
            CreateCustomerResponse response = new CreateCustomerResponse(customerName, customer.getPhoneNumber(),
                    "", customer.getId(),
                    "Customer updated successfully");
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(
                "Sorry! Customer with this ID is not registered with us",
                HttpStatus.BAD_REQUEST);
    }

    public String getCustomerProfilePicLink(Long customerId) {
        String customerImageReferenceId = objectImagesService.getCustomerImageReferenceId(customerId);
        if (customerImageReferenceId != null) {
            return getCustomerProfilePicLink(customerImageReferenceId);
        }
        return "";
    }

    public Double getCustomerRevenue(CustomerDAO customerDAO) {
        Double sum = 0d;
        sum = customerDAO.getOrders()
                .stream()
                .map(order -> order.getOrderAmount())
                .mapToDouble(OrderAmountDAO::getTotalAmount)
                .sum();
        return sum;
    }

    public CustomerDashboard getCustomerDashboardDetails(Long boutiqueId, int month, int year) {
        LocalDateTime monthStart = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime nextMonthStart = monthStart.plusMonths(1);
        Integer totalCustomerCount = customerRepo.countByBoutiqueId(boutiqueId);
        Integer newlyAddedCustomers = customerRepo.countByBoutiqueIdAndCreatedAtAfterAndCreatedAtBefore(boutiqueId, monthStart, nextMonthStart);
        CustomerDashboard customerDashboard = new CustomerDashboard(totalCustomerCount, newlyAddedCustomers);
        return customerDashboard;
    }

    private String getCustomerProfilePicLink(String customerImageReferenceId) {
        if (StringUtils.isBlank(customerImageReferenceId)) {
            return "";
        }
        Optional<ImageReference> optionalImageReference = imageReferenceRepo.findByReferenceId(customerImageReferenceId);
        if (optionalImageReference.isPresent()) {
            ImageReferenceDAO imageReferenceDAO = mapper.imageReferenceToImageReferenceDAO(optionalImageReference.get());
            String fileName = imageReferenceDAO.getImageName();
            return s3Client.generateShortLivedUrl(fileName);
        }
        return "";
    }

    private ImmutablePair<String, String> getCustomerNameFromRequest(String name) {

        String[] nameArray = name.split(" ");
        String firstName = nameArray[0];
        String lastName = null;
        if (nameArray.length > 1) {
            lastName = nameArray[nameArray.length - 1];
        }
        return new ImmutablePair(firstName, lastName);
    }

    private void saveCustomerImages(CustomerDAO customerDAO, String imageReferenceId) {
        objectImagesService.invalidateExistingReferenceIds(ImageEntityType.CUSTOMER.getEntityType(), customerDAO.getId());
        objectImagesService.saveObjectImages(Arrays.asList(imageReferenceId),
                ImageEntityType.CUSTOMER.getEntityType(),
                customerDAO.getId());
    }


}
