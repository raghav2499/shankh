package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderItemDAO;
import com.darzee.shankh.dao.PriceBreakupDAO;
import com.darzee.shankh.entity.PriceBreakup;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.PriceBreakUpRepo;
import com.darzee.shankh.request.PriceBreakUpDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PriceBreakUpService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private PriceBreakUpRepo priceBreakUpRepo;

    public List<PriceBreakupDAO> savePriceBreakUp(List<PriceBreakupDAO> priceBreakupDAOList) {
        priceBreakupDAOList = mapper.priceBreakUpListToPriceBreakUpDAOList(
                priceBreakUpRepo.saveAll(mapper.priceBreakUpDAOListToPriceBreakUpList(priceBreakupDAOList,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return priceBreakupDAOList;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public List<PriceBreakupDAO> updatePriceBreakups(List<PriceBreakUpDetails> priceBreakupList,
            OrderItemDAO orderItem) {

        // Ensure that the price breakup list and order item are not null
        if (priceBreakupList == null) {
            throw new IllegalArgumentException("Price breakup list cannot be null");
        }

        if (orderItem == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        } 

        List<PriceBreakupDAO> priceBreakupDAOList = new ArrayList<>();

        // Iterate through each price breakup detail and update the corresponding price
        // breakup if it exists,
        // or create a new one if the id is null
        for (PriceBreakUpDetails priceBreakUpDetail : priceBreakupList) {

            // Ensure that the price breakup detail is not null
            if (priceBreakUpDetail == null) {
                throw new IllegalArgumentException("Price breakup detail cannot be null");
            }

            // If the price breakup detail contains an id, then try to find the
            // corresponding price breakup
            if (priceBreakUpDetail.getId() != null) {
                Optional<PriceBreakup> priceBreakup = priceBreakUpRepo.findById(priceBreakUpDetail.getId());

                // If the price breakup is present, update it with the new details
                if (priceBreakup.isPresent()) {
                    PriceBreakupDAO priceBreakupDAO = mapper.priceBreakupToPriceBreakupDAO(priceBreakup.get(),
                            new CycleAvoidingMappingContext()); 
                    updatePriceBreakup(priceBreakupDAO, priceBreakUpDetail);
                    priceBreakupDAOList.add(priceBreakupDAO);

                    // If the price breakup is not present, throw a bad request exception
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Price Breakup ID is incorrect");
                }

                // If the price breakup detail does not contain an id, create a new price
                // breakup
            } else {
                PriceBreakupDAO priceBreakupDAO = new PriceBreakupDAO(priceBreakUpDetail, orderItem);
                priceBreakupDAOList.add(priceBreakupDAO);
            }
        }

        try {
            // Save the updated or new price breakups
            priceBreakupDAOList = mapper.priceBreakUpListToPriceBreakUpDAOList(
                    priceBreakUpRepo.saveAll(mapper.priceBreakUpDAOListToPriceBreakUpList(priceBreakupDAOList,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());

            // Catch any exceptions and wrap them in a runtime exception
        } catch (Exception e) {
            throw new RuntimeException("Failed to save price breakups", e);
        }


        return priceBreakupDAOList;
    }

    public List<PriceBreakupDAO> generatePriceBreakupList(List<PriceBreakUpDetails> priceBreakUpDetails,
            OrderItemDAO orderItemDAO) {
        List<PriceBreakupDAO> priceBreakupDAOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(priceBreakUpDetails)) {
            for (PriceBreakUpDetails priceBreakUpDetail : priceBreakUpDetails) {
                priceBreakupDAOList.add(new PriceBreakupDAO(priceBreakUpDetail, orderItemDAO));
            }
        }
        return priceBreakupDAOList;
    }

    private void updatePriceBreakup(PriceBreakupDAO priceBreakupDAO, PriceBreakUpDetails updatedDetails) {

        if (priceBreakupDAO.isComponentStringUpdated(updatedDetails.getComponent())) {
            priceBreakupDAO.setComponent(updatedDetails.getComponent());
        }

        if (priceBreakupDAO.isValueUpdated(updatedDetails.getValue())) {
            priceBreakupDAO.setValue(updatedDetails.getValue());
        }

        if (priceBreakupDAO.isQuantityUpdated(updatedDetails.getComponentQuantity())) {
            priceBreakupDAO.setQuantity(updatedDetails.getComponentQuantity());
        }
    
        if (Boolean.TRUE.equals(updatedDetails.getIsDeleted())) {
            priceBreakupDAO.setIsDeleted(Boolean.TRUE);
        }
    }
}
