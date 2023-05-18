package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueLedgerDAO;
import com.darzee.shankh.dao.OrderAmountDAO;
import com.darzee.shankh.entity.BoutiqueLedger;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.response.GetBoutiqueLedgerDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BoutiqueLedgerService {

    @Autowired
    private BoutiqueLedgerRepo repo;

    @Autowired
    private DaoEntityMapper mapper;

    public ResponseEntity getLedgerData(String boutiqueIdString) {
        Long boutiqueId = Long.parseLong(boutiqueIdString);
        BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
        if (boutiqueLedger != null) {
            BoutiqueLedgerDAO boutiqueLedgerObject = mapper.boutiqueLedgerObjectToDAO(boutiqueLedger,
                    new CycleAvoidingMappingContext());
            GetBoutiqueLedgerDataResponse response = new GetBoutiqueLedgerDataResponse();
            response.setMonthlyLedgerData(boutiqueLedgerObject.getMonthlyAmountRecieved(),
                    boutiqueLedgerObject.getMonthlyPendingAmount(), boutiqueLedgerObject.getMonthlyActiveOrders(),
                    boutiqueLedgerObject.getMonthlyClosedOrders());
            response.setOverallLedgerData(boutiqueLedgerObject.getTotalAmountRecieved(),
                    boutiqueLedgerObject.getTotalPendingAmount(), boutiqueLedgerObject.getTotalActiveOrders(),
                    boutiqueLedgerObject.getTotalClosedOrders());

            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Boutique Ledger for this id doesn't exist");
    }

    public BoutiqueLedgerDAO updateBoutiqueLedgerOnStatusActivation(Long boutiqueId) {
        BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
        if (boutiqueLedger != null) {
            BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
//            updateBoutiqueLedgerAmountDetails(pendingAmount, recievedAmount, boutiqueLedgerDAO);
            incrementActiveOrders(boutiqueLedgerDAO);
            boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
            return boutiqueLedgerDAO;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Boutique ledger doesn't exist for boutique " + boutiqueId);
    }


    public BoutiqueLedgerDAO updateBoutiqueLedgerOnStatusClosure(Long boutiqueId) {
        BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
        if (boutiqueLedger != null) {
            BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
//            updateBoutiqueLedgerAmountDetails(pendingAmount, recievedAmount, boutiqueLedgerDAO);
            incrementClosedOrders(boutiqueLedgerDAO);
            boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
            return boutiqueLedgerDAO;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Boutique ledger doesn't exist for boutique " + boutiqueId);
    }

    public BoutiqueLedgerDAO updateBoutiqueLedgerAmountDetails(Double deltaPendingAmount, Double deltaAmountRecieved,
                                                               Long boutiqueId) {
        BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
        if (boutiqueLedger != null) {
            BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId),
                    new CycleAvoidingMappingContext());
            boutiqueLedgerDAO.addOrderAmountToBoutiqueLedger(deltaPendingAmount, deltaAmountRecieved);
            boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());
            return boutiqueLedgerDAO;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Boutique ledger doesn't exist for boutique " + boutiqueId);
    }

    public BoutiqueLedgerDAO handleBoutiqueLedgerForDeletedOrder(Long boutiqueId, OrderAmountDAO orderAmountDAO) {
        Double pendingOrderAmount = orderAmountDAO.getTotalAmount() - orderAmountDAO.getAmountRecieved();
        return updateBoutiqueLedgerAmountDetails(-pendingOrderAmount, -orderAmountDAO.getAmountRecieved(), boutiqueId);
    }

    private void incrementActiveOrders(BoutiqueLedgerDAO ledgerDAO) {
        ledgerDAO.incrementActiveOrdersInLedger();
        repo.save(mapper.boutiqueLedgerDAOToObject(ledgerDAO, new CycleAvoidingMappingContext()));
    }

    private void incrementClosedOrders(BoutiqueLedgerDAO ledgerDAO) {
        ledgerDAO.incrementClosedOrdersInLedger();
        repo.save(mapper.boutiqueLedgerDAOToObject(ledgerDAO, new CycleAvoidingMappingContext()));
    }

}
