package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.BoutiqueLedgerDAO;
import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.enums.OrderStatus;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.OrderRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.CreateBoutiqueRequest;
import com.darzee.shankh.response.CreateBoutiqueResponse;
import com.darzee.shankh.response.GetBoutiqueDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class BoutiqueService {

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;

    @Autowired
    private DaoEntityMapper mapper;
    @Autowired
    private TailorRepo tailorRepo;
    @Autowired
    private OrderRepo orderRepo;

    @Transactional
    public ResponseEntity addBoutique(CreateBoutiqueRequest request) {
        BoutiqueDAO boutiqueDAO = new BoutiqueDAO(request.getBoutiqueName(),
                request.getBoutiqueType(),
                request.getTailorCount());
        boutiqueDAO = mapper.boutiqueObjectToDao(boutiqueRepo.save(mapper.boutiqueDaoToObject(boutiqueDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

        BoutiqueLedgerDAO boutiqueLedgerDAO = new BoutiqueLedgerDAO(boutiqueDAO.getId());
        boutiqueLedgerRepo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO, new CycleAvoidingMappingContext()));

        TailorDAO tailorDAO = new TailorDAO(request.getTailorName(), Integer.MAX_VALUE, Boolean.TRUE, boutiqueDAO);
        tailorDAO = mapper.tailorObjectToDao(tailorRepo.save(mapper.tailorDaoToObject(tailorDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());

        CreateBoutiqueResponse response = new CreateBoutiqueResponse(boutiqueDAO.getId(), boutiqueDAO.getName(),
                tailorDAO.getName(),
                "Boutique created successfully");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    public ResponseEntity getBoutiqueData(String boutiqueIdString) {
        Long boutiqueId = Long.parseLong(boutiqueIdString);
        BoutiqueLedgerDAO boutiqueLedgerObject =
                mapper.boutiqueLedgerObjectToDAO(boutiqueLedgerRepo.findById(boutiqueId).get(),
                        new CycleAvoidingMappingContext());

        GetBoutiqueDataResponse response = new GetBoutiqueDataResponse();
        response.setMonthlyLedgerData(boutiqueLedgerObject.getMonthlyAmountRecieved(),
                boutiqueLedgerObject.getMonthlyPendingAmount());
        response.setWeeklyLedgerData(boutiqueLedgerObject.getWeeklyAmountRecieved(),
                boutiqueLedgerObject.getWeeklyPendingAmount());
        response.setOverallLedgerData(boutiqueLedgerObject.getMonthlyAmountRecieved(),
                boutiqueLedgerObject.getMonthlyPendingAmount());

        List<OrderStatus> activeOrderStatusList = Arrays.asList(OrderStatus.ORDER_READY_FOR_TRIAL,
                OrderStatus.ORDER_READY_FOR_TRIAL,
                OrderStatus.STITCHING_IN_PROGRESS);
        List<OrderStatus> closedOrderStatusList = Arrays.asList(OrderStatus.ORDER_COMPLETED);
        List<OrderDAO> activeOrderList = mapper.orderObjectListToDAOList(orderRepo.findAllByBoutiqueIdAndOrderStatusIn(boutiqueId,
                activeOrderStatusList), new CycleAvoidingMappingContext());
        List<OrderDAO> closedOrderList = mapper.orderObjectListToDAOList(orderRepo.findAllByBoutiqueIdAndOrderStatusIn(boutiqueId,
                closedOrderStatusList), new CycleAvoidingMappingContext());
        response.setActiveOrderCount(activeOrderList.size());
        response.setClosedOrderCount(closedOrderList.size());

        TailorDAO tailor = mapper.tailorObjectToDao(tailorRepo.findByBoutiqueIdAndIsOwner(boutiqueId, Boolean.TRUE),
                new CycleAvoidingMappingContext());
        response.setOwnerTailorName(tailor.getName());
        return new ResponseEntity(response, HttpStatus.OK);
    }


}
