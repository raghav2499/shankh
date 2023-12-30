package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderItemDAO;
import com.darzee.shankh.dao.PriceBreakupDAO;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.PriceBreakUpRepo;
import com.darzee.shankh.request.PriceBreakUpDetails;
import com.darzee.shankh.request.innerObjects.UpdatePriceBreakupDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PriceBreakUpService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private PriceBreakUpRepo priceBreakUpRepo;

    public void savePriceBreakUp(List<PriceBreakupDAO> priceBreakupDAOList) {
        priceBreakupDAOList = mapper.priceBreakUpListToPriceBreakUpDAOList(
                priceBreakUpRepo.saveAll(mapper.priceBreakUpDAOListToPriceBreakUpList(priceBreakupDAOList,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

    }

    public List<PriceBreakupDAO> updatePriceBreakups(List<UpdatePriceBreakupDetails> priceBreakupList, OrderItemDAO orderItemDAO) {
        List<PriceBreakupDAO> priceBreakups = orderItemDAO.getPriceBreakup();
        Map<Long, UpdatePriceBreakupDetails> updatedPriceBreakUpMap = priceBreakupList.stream()
                .collect(Collectors.toMap(UpdatePriceBreakupDetails::getId, Function.identity()));
        for(PriceBreakupDAO priceBreakupDAO : priceBreakups) {
            if(updatedPriceBreakUpMap.containsKey(priceBreakupDAO.getId())) {
                updatePriceBreakup(priceBreakupDAO, updatedPriceBreakUpMap.get(priceBreakupDAO.getId()));
            }
        }
        return priceBreakups;

    }

    public List<PriceBreakupDAO> generatePriceBreakupList(List<PriceBreakUpDetails> priceBreakUpDetails,
                                                          OrderItemDAO orderItemDAO) {
        List<PriceBreakupDAO> priceBreakupDAOList = new ArrayList<>();
        for (PriceBreakUpDetails priceBreakUpDetail : priceBreakUpDetails) {
            priceBreakupDAOList.add(new PriceBreakupDAO(priceBreakUpDetail, orderItemDAO));
        }
        return priceBreakupDAOList;
    }

    private void updatePriceBreakup(PriceBreakupDAO priceBreakupDAO, UpdatePriceBreakupDetails updatedDetails) {
        if(priceBreakupDAO.isComponentStringUpdated(updatedDetails.getComponent())) {
            priceBreakupDAO.setComponent(updatedDetails.getComponent());
        }
        if(priceBreakupDAO.isValueUpdated(updatedDetails.getValue())) {
            priceBreakupDAO.setValue(updatedDetails.getValue());
        }
        if(priceBreakupDAO.isQuantityUpdated(updatedDetails.getComponentQuantity())) {
            priceBreakupDAO.setQuantity(updatedDetails.getComponentQuantity());
        }
    }
}
