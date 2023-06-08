package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.BoutiqueLedgerDAO;
import com.darzee.shankh.dao.BoutiqueLedgerSnapshotDAO;
import com.darzee.shankh.entity.BoutiqueLedger;
import com.darzee.shankh.entity.BoutiqueLedgerSnapshot;
import com.darzee.shankh.enums.OrderStage;
import com.darzee.shankh.enums.OrderStatus;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.repo.BoutiqueLedgerSnapshotRepo;
import com.darzee.shankh.response.GetBoutiqueLedgerDataResponse;
import com.darzee.shankh.response.LedgerDashboardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class BoutiqueLedgerService {

    @Autowired
    private BoutiqueLedgerRepo repo;

    @Autowired
    private BoutiqueLedgerSnapshotRepo snapshotRepo;

    @Autowired
    private DaoEntityMapper mapper;

    public ResponseEntity getLedgerData(String boutiqueIdString) {
        Long boutiqueId = Long.parseLong(boutiqueIdString);
        BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
        if (boutiqueLedger != null) {
            BoutiqueLedgerDAO boutiqueLedgerObject = mapper.boutiqueLedgerObjectToDAO(boutiqueLedger,
                    new CycleAvoidingMappingContext());
            GetBoutiqueLedgerDataResponse response = new GetBoutiqueLedgerDataResponse();
            response.setMonthlyLedgerDashboardData(boutiqueLedgerObject.getMonthlyAmountRecieved(),
                    boutiqueLedgerObject.getMonthlyPendingAmount(), boutiqueLedgerObject.getMonthlyActiveOrders(),
                    boutiqueLedgerObject.getMonthlyClosedOrders());
            response.setOverallLedgerDashboardData(boutiqueLedgerObject.getTotalAmountRecieved(),
                    boutiqueLedgerObject.getTotalPendingAmount(), boutiqueLedgerObject.getTotalActiveOrders(),
                    boutiqueLedgerObject.getTotalClosedOrders());

            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Boutique Ledger for this id doesn't exist");
    }

    @Transactional
    public BoutiqueLedgerDAO handleBoutiqueLedgerOnOrderUpdation(Long boutiqueId,
                                                                 OrderStatus initialStatus,
                                                                 OrderStatus currentStatus) {
        BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
        if (boutiqueLedger != null) {
            BoutiqueLedgerDAO ledgerDAO = mapper.boutiqueLedgerObjectToDAO(boutiqueLedger, new CycleAvoidingMappingContext());
            setUpdatedActiveOrderCount(ledgerDAO, initialStatus, currentStatus);
            setUpdatedClosedOrderCount(ledgerDAO, initialStatus, currentStatus);
            repo.save(mapper.boutiqueLedgerDAOToObject(ledgerDAO, new CycleAvoidingMappingContext()));
            return ledgerDAO;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Boutique ledger doesn't exist for boutique " + boutiqueId);
    }

    @Transactional
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

    @Transactional
    public BoutiqueLedgerDAO handleBoutiqueLedgerForDeletedOrder(Long boutiqueId, Double pendingOrderAmount,
                                                                 Double amountRecieved,
                                                                 OrderStage orderStage) {
        BoutiqueLedgerDAO boutiqueLedgerDAO = updateBoutiqueLedgerAmountDetails(-pendingOrderAmount,
                -amountRecieved,
                boutiqueId);

        boutiqueLedgerDAO = updateOrderCountInBoutiqueLedger(boutiqueLedgerDAO, orderStage);

        boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return boutiqueLedgerDAO;
    }

    @Transactional
    public ResponseEntity resetBoutiqueLedgerData(String boutiqueIdString) {
        Long boutiqueId = Long.parseLong(boutiqueIdString);
        BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId),
                new CycleAvoidingMappingContext());
        if (boutiqueLedgerDAO != null) {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousMonthDate = currentDate.minusMonths(1);
            Integer month = previousMonthDate.getMonthValue();
            Integer year = previousMonthDate.getYear();

            BoutiqueLedgerSnapshotDAO boutiqueLedgerSnapshotDAO = new BoutiqueLedgerSnapshotDAO(boutiqueLedgerDAO.getMonthlyPendingAmount(),
                    boutiqueLedgerDAO.getMonthlyAmountRecieved(),
                    boutiqueLedgerDAO.getMonthlyActiveOrders(),
                    boutiqueId,
                    month,
                    year);
            boutiqueLedgerDAO.resetMonthlyDetailsInLedger();
            snapshotRepo.save(mapper.boutiqueLedgerSnapshotDAOToSnapshot(boutiqueLedgerSnapshotDAO));
            repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO, new CycleAvoidingMappingContext()));
            return new ResponseEntity("Boutique data reset successfully", HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ledger doesn't exist for boutique " + boutiqueIdString);
    }

    public LedgerDashboardData getLedgerDashboardDetails(Long boutiqueId, int month, int year) {
        BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId),
                new CycleAvoidingMappingContext());
        if (boutiqueLedgerDAO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid boutique id");
        }

        LedgerDashboardData ledgerDashboardData = null;
        LocalDate currentDate = LocalDate.now();
        if (currentDate.getMonthValue() == month && currentDate.getYear() == year) {
            ledgerDashboardData = new LedgerDashboardData(boutiqueLedgerDAO.getMonthlyAmountRecieved(),
                    boutiqueLedgerDAO.getMonthlyPendingAmount(),
                    boutiqueLedgerDAO.getMonthlyActiveOrders(),
                    boutiqueLedgerDAO.getMonthlyClosedOrders());
        } else {
            Optional<BoutiqueLedgerSnapshot> ledgerSnapshot = snapshotRepo.findByBoutiqueIdAndMonthAndYear(boutiqueId,
                    month, year);
            if (ledgerSnapshot.isPresent()) {
                BoutiqueLedgerSnapshotDAO ledgerSnapshotDAO =
                        mapper.boutiqueLedgerSnapshotToSnapshotDAO(ledgerSnapshot.get());
                ledgerDashboardData = new LedgerDashboardData(ledgerSnapshotDAO);
            }
        }
        return ledgerDashboardData;
    }

    @Transactional
    private BoutiqueLedgerDAO updateOrderCountInBoutiqueLedger(BoutiqueLedgerDAO ledger, OrderStage orderStage) {
        if (OrderStage.ACTIVE.equals(orderStage)) {
            ledger.decrementActiveOrderCount();
        } else if (OrderStage.CLOSED.equals(orderStage)) {
            ledger.decrementClosedOrderCount();
        }
        return ledger;
    }

    private void setUpdatedActiveOrderCount(BoutiqueLedgerDAO boutiqueLedgerDAO,
                                            OrderStatus initialStatus,
                                            OrderStatus currentStatus) {
        Integer activeOrderCount = boutiqueLedgerDAO.getTotalActiveOrders();
        if (Constants.ACTIVE_ORDER_STATUS_LIST.contains(currentStatus)
                && !Constants.ACTIVE_ORDER_STATUS_LIST.contains(initialStatus)) {
            activeOrderCount++;
        } else if (!Constants.ACTIVE_ORDER_STATUS_LIST.contains(currentStatus)
                && Constants.ACTIVE_ORDER_STATUS_LIST.contains(initialStatus)) {
            activeOrderCount = Math.max(activeOrderCount - 1, 0);
        }
        boutiqueLedgerDAO.setTotalActiveOrders(activeOrderCount);
        boutiqueLedgerDAO.setMonthlyActiveOrders(activeOrderCount);
    }

    private void setUpdatedClosedOrderCount(BoutiqueLedgerDAO boutiqueLedgerDAO,
                                            OrderStatus initialStatus,
                                            OrderStatus currentStatus) {
        if (Constants.CLOSED_ORDER_STATUS_LIST.contains(currentStatus)
                && !Constants.CLOSED_ORDER_STATUS_LIST.contains(initialStatus)) {
            boutiqueLedgerDAO.setMonthlyClosedOrders(boutiqueLedgerDAO.getMonthlyClosedOrders() + 1);
            boutiqueLedgerDAO.setTotalClosedOrders(boutiqueLedgerDAO.getTotalClosedOrders() + 1);
        } else if (!Constants.CLOSED_ORDER_STATUS_LIST.contains(currentStatus)
                && Constants.CLOSED_ORDER_STATUS_LIST.contains(initialStatus)) {
            boutiqueLedgerDAO.setMonthlyClosedOrders(Math.max(boutiqueLedgerDAO.getMonthlyClosedOrders() - 1, 0));
            boutiqueLedgerDAO.setTotalClosedOrders(Math.max(boutiqueLedgerDAO.getTotalClosedOrders() - 1, 0));
        }
    }

}
