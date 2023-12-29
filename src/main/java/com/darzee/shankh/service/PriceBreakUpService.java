package com.darzee.shankh.service;

import com.darzee.shankh.dao.PriceBreakupDAO;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.PriceBreakUpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceBreakUpService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private PriceBreakUpRepo priceBreakUpRepo;

    public void addPriceBreakUp(List<PriceBreakupDAO> priceBreakupDAOList) {
        priceBreakupDAOList = mapper.priceBreakUpListToPriceBreakUpDAOList(
                priceBreakUpRepo.saveAll(mapper.priceBreakUpDAOListToPriceBreakUpList(priceBreakupDAOList,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

    }
}
