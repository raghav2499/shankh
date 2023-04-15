package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.CustomerDAO;
import com.darzee.shankh.dao.ImageReferenceDAO;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.CustomerRepo;
import com.darzee.shankh.repo.ImageReferenceRepo;
import com.darzee.shankh.request.CreateCustomerRequest;
import com.darzee.shankh.response.CreateCustomerResponse;
import com.darzee.shankh.response.CustomerDetails;
import com.darzee.shankh.response.GetCustomerResponse;
import com.darzee.shankh.response.GetCustomersResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

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

    public ResponseEntity getCustomers(Long boutiqueId) {
        List<CustomerDAO> boutiqueCustomers =
                mapper.customerObjectListToDAOList(customerRepo.findAllByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
        boutiqueCustomers.sort(Comparator.comparing(CustomerDAO::getFirstName));
        List<CustomerDetails> customerDetails = boutiqueCustomers
                .stream()
                .map(customer ->
                        new CustomerDetails(customer.getFirstName()
                                + " "
                                + customer.getLastName(),
                                customer.getPhoneNumber(),
                                getCustomerProfilePicLink(customer.getImageReferenceId()),
                                customer.getId()))
                .collect(Collectors.toList());

        return new ResponseEntity(new GetCustomersResponse(customerDetails), HttpStatus.OK);
    }

    public ResponseEntity getCustomer(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepo.findById(customerId);
        if (optionalCustomer.isPresent()) {
            CustomerDAO customerDAO = mapper.customerObjectToDao(optionalCustomer.get(), new CycleAvoidingMappingContext());
            GetCustomerResponse response = mapper.customerDAOToGetCustomerResponse(customerDAO, new CycleAvoidingMappingContext());
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity createCustomer(CreateCustomerRequest request) {
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(request.getBoutiqueId());
        CreateCustomerResponse response = new CreateCustomerResponse();
        if (optionalBoutique.isPresent()) {
            Optional<Customer> existingCustomer = customerRepo.findByPhoneNumber(request.getPhoneNumber());
            if (existingCustomer.isPresent()) {
                response.setMessage("Customer already registered");
                return new ResponseEntity(response, HttpStatus.OK);
            }
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(),
                    new CycleAvoidingMappingContext());
            String name = request.getName();

            String[] nameArray = name.split(" ");
            String firstName = nameArray[0];
            String lastName = null;
            if (nameArray.length > 1) {
                lastName = nameArray[nameArray.length - 1];
            }
            CustomerDAO customerDAO = new CustomerDAO(request.getAge(), request.getPhoneNumber(), firstName, lastName,
                    request.getGender(), request.getCustomerImageReferenceId(), boutiqueDAO);
            customerDAO = mapper.customerObjectToDao(customerRepo.save(mapper.customerDaoToObject(customerDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
            response = new CreateCustomerResponse(customerDAO.getId(), customerDAO.getFirstName(),
                    customerDAO.getLastName(), "Customer created successfully");
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        response.setMessage("This boutique is not enrolled with us");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    private String getCustomerProfilePicLink(String customerImageReferenceId) {
        if(StringUtils.isBlank(customerImageReferenceId)) {
            return "";
        }
        Optional<ImageReference> optionalImageReference = imageReferenceRepo.findByReferenceId(customerImageReferenceId);
        if(optionalImageReference.isPresent()) {
            ImageReferenceDAO imageReferenceDAO = mapper.imageReferenceToImageReferenceDAO(optionalImageReference.get());
            String fileName = imageReferenceDAO.getImageName();
            return s3Client.generateShortLivedUrl(fileName);
        }
        return "";
    }


}
