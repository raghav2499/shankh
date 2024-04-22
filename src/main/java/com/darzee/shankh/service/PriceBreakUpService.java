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
        priceBreakupDAOList = mapper.priceBreakUpListToPriceBreakUpDAOList(priceBreakUpRepo.saveAll(mapper.priceBreakUpDAOListToPriceBreakUpList(priceBreakupDAOList, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return priceBreakupDAOList;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public List<PriceBreakupDAO> updatePriceBreakups(List<PriceBreakUpDetails> priceBreakupList, OrderItemDAO orderItem) {
        List<PriceBreakupDAO> priceBreakupDAOList = new ArrayList<>();

        for (PriceBreakUpDetails priceBreakUpDetail : priceBreakupList) {

            if (priceBreakUpDetail.getId() != null) {
                Optional<PriceBreakup> priceBreakup = priceBreakUpRepo.findById(priceBreakUpDetail.getId());

                if (priceBreakup.isPresent()) {
                    PriceBreakupDAO priceBreakupDAO = mapper.priceBreakupToPriceBreakupDAO(priceBreakup.get(), new CycleAvoidingMappingContext());
                    updatePriceBreakup(priceBreakupDAO, priceBreakUpDetail);
                    priceBreakupDAOList.add(priceBreakupDAO);

                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price Breakup ID is incorrect");
                }

            } else {
                PriceBreakupDAO priceBreakupDAO = new PriceBreakupDAO(priceBreakUpDetail, orderItem);
                priceBreakupDAOList.add(priceBreakupDAO);
            }
        }

        priceBreakupDAOList = mapper.priceBreakUpListToPriceBreakUpDAOList(
                priceBreakUpRepo.saveAll(mapper.priceBreakUpDAOListToPriceBreakUpList(priceBreakupDAOList, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return priceBreakupDAOList;
    }

    public List<PriceBreakupDAO> generatePriceBreakupList(List<PriceBreakUpDetails> priceBreakUpDetails, OrderItemDAO orderItemDAO) {
        List<PriceBreakupDAO> priceBreakupDAOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(priceBreakUpDetails)) {
            for (PriceBreakUpDetails priceBreakUpDetail : priceBreakUpDetails) {
                priceBreakupDAOList.add(new PriceBreakupDAO(priceBreakUpDetail, orderItemDAO));
            }
        }
        priceBreakupDAOList = savePriceBreakUp(priceBreakupDAOList);
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
