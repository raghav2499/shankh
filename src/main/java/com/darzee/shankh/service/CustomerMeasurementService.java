package com.darzee.shankh.service;

import com.darzee.shankh.dao.MeasurementsDAO;
import com.darzee.shankh.entity.Measurements;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.MeasurementsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerMeasurementService {

    @Autowired
    private MeasurementsRepo measurementsRepo;

    @Autowired
    private DaoEntityMapper mapper;

    public MeasurementsDAO getCustomerMeasurements(Long customerId, OutfitType outfitType) {
        Optional<Measurements> optionalMeasurementsDAO = measurementsRepo.findOneByCustomerIdAndOutfitType(customerId, outfitType);
        if(optionalMeasurementsDAO.isPresent()) {
            return mapper.measurementsToMeasurementDAO(optionalMeasurementsDAO.get(), new CycleAvoidingMappingContext());
        }
        return new MeasurementsDAO();
    }
}
