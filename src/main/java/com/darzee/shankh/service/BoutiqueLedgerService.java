package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.dao.BoutiqueLedgerDAO;
import com.darzee.shankh.dao.BoutiqueLedgerSnapshotDAO;
import com.darzee.shankh.dao.OrderItemDAO;
import com.darzee.shankh.entity.BoutiqueLedger;
import com.darzee.shankh.entity.BoutiqueLedgerSnapshot;
import com.darzee.shankh.enums.OrderItemStatus;
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

import static com.darzee.shankh.constants.Constants.ACTIVE_ORDER_ITEM_STATUS_LIST;
import static com.darzee.shankh.constants.Constants.CLOSED_ORDER_ITEM_STATUS_LIST;

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
    public BoutiqueLedgerDAO handleBoutiqueLedgerOnOrderItemUpdation(Long boutiqueId,
                                                                     OrderItemStatus initialStatus,
                                                                     OrderItemStatus currentStatus) {
        BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
        if (boutiqueLedger != null) {
            BoutiqueLedgerDAO ledgerDAO = mapper.boutiqueLedgerObjectToDAO(boutiqueLedger, new CycleAvoidingMappingContext());
            setUpdatedActiveOrderCountOnStatusUpdate(ledgerDAO, initialStatus, currentStatus);
            setUpdatedClosedOrderCountOnStatusUpdate(ledgerDAO, initialStatus, currentStatus);
            repo.save(mapper.boutiqueLedgerDAOToObject(ledgerDAO, new CycleAvoidingMappingContext()));
            return ledgerDAO;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Boutique ledger doesn't exist for boutique " + boutiqueId);
    }

    @Transactional
    public BoutiqueLedgerDAO handleBoutiqueLedgerOnOrderItemDeletion(Long boutiqueId,
                                                                     OrderItemDAO orderItemDAO) {
        BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
        if (boutiqueLedger != null) {
            BoutiqueLedgerDAO ledgerDAO = mapper.boutiqueLedgerObjectToDAO(boutiqueLedger, new CycleAvoidingMappingContext());
            if (ACTIVE_ORDER_ITEM_STATUS_LIST.contains(orderItemDAO.getOrderItemStatus())) {
                ledgerDAO.setMonthlyActiveOrders(ledgerDAO.getMonthlyActiveOrders() - 1);
                ledgerDAO.setTotalActiveOrders(ledgerDAO.getTotalActiveOrders() - 1);
            }
            if (CLOSED_ORDER_ITEM_STATUS_LIST.contains(orderItemDAO.getOrderItemStatus())) {
                ledgerDAO.setMonthlyClosedOrders(ledgerDAO.getMonthlyClosedOrders() - 1);
                ledgerDAO.setTotalClosedOrders(ledgerDAO.getTotalClosedOrders() - 1);
            }
            Double itemPrice = orderItemDAO.getPriceBreakup().stream()
                    .mapToDouble(pb ->
                            Optional.ofNullable(pb.getQuantity()).orElse(0) * Optional.ofNullable(pb.getValue()).orElse(0d))
                    .sum();
            ledgerDAO.setMonthlyPendingAmount(ledgerDAO.getMonthlyPendingAmount() - itemPrice);
            ledgerDAO.setTotalPendingAmount(ledgerDAO.getTotalPendingAmount() - itemPrice);

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
                                                                 OrderStatus status) {
        BoutiqueLedgerDAO boutiqueLedgerDAO = updateBoutiqueLedgerAmountDetails(-pendingOrderAmount,
                -amountRecieved,
                boutiqueId);

        boutiqueLedgerDAO = updateOrderCountInBoutiqueLedger(boutiqueLedgerDAO, status);

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
    private BoutiqueLedgerDAO updateOrderCountInBoutiqueLedger(BoutiqueLedgerDAO ledger, OrderStatus status) {
        if (OrderStatus.ACCEPTED.equals(status)) {
            ledger.decrementActiveOrderCount();
        } else if (OrderStatus.DELIVERED.equals(status)) {
            ledger.decrementClosedOrderCount();
        }
        return ledger;
    }

    private void setUpdatedActiveOrderCountOnStatusUpdate(BoutiqueLedgerDAO boutiqueLedgerDAO,
                                                          OrderItemStatus initialStatus,
                                                          OrderItemStatus currentStatus) {
        Integer activeOrderCount = boutiqueLedgerDAO.getTotalActiveOrders();
        if (ACTIVE_ORDER_ITEM_STATUS_LIST.contains(currentStatus)
                && !ACTIVE_ORDER_ITEM_STATUS_LIST.contains(initialStatus)) {
            activeOrderCount++;
        } else if (!ACTIVE_ORDER_ITEM_STATUS_LIST.contains(currentStatus)
                && ACTIVE_ORDER_ITEM_STATUS_LIST.contains(initialStatus)) {
            activeOrderCount = Math.max(activeOrderCount - 1, 0);
        }
        boutiqueLedgerDAO.setTotalActiveOrders(activeOrderCount);
        boutiqueLedgerDAO.setMonthlyActiveOrders(activeOrderCount);
    }

    private void setUpdatedClosedOrderCountOnStatusUpdate(BoutiqueLedgerDAO boutiqueLedgerDAO,
                                                          OrderItemStatus initialStatus,
                                                          OrderItemStatus currentStatus) {
        if (Constants.CLOSED_ORDER_ITEM_STATUS_LIST.contains(currentStatus)
                && !Constants.CLOSED_ORDER_ITEM_STATUS_LIST.contains(initialStatus)) {
            boutiqueLedgerDAO.setMonthlyClosedOrders(boutiqueLedgerDAO.getMonthlyClosedOrders() + 1);
            boutiqueLedgerDAO.setTotalClosedOrders(boutiqueLedgerDAO.getTotalClosedOrders() + 1);
        } else if (!Constants.CLOSED_ORDER_ITEM_STATUS_LIST.contains(currentStatus)
                && Constants.CLOSED_ORDER_ITEM_STATUS_LIST.contains(initialStatus)) {
            boutiqueLedgerDAO.setMonthlyClosedOrders(Math.max(boutiqueLedgerDAO.getMonthlyClosedOrders() - 1, 0));
            boutiqueLedgerDAO.setTotalClosedOrders(Math.max(boutiqueLedgerDAO.getTotalClosedOrders() - 1, 0));
        }
    }

    private void setUpdatedActiveOrderCountOnDeletion(BoutiqueLedgerDAO boutiqueLedgerDAO,
                                                      OrderStatus status) {
        Integer activeOrderCount = boutiqueLedgerDAO.getTotalActiveOrders();
        Integer closedOrderCount = boutiqueLedgerDAO.getTotalClosedOrders();
        if (Constants.ACTIVE_ORDER_STATUS_LIST.contains(status)) {
            activeOrderCount = Math.max(activeOrderCount - 1, 0);
        }
        if (Constants.CLOSED_ORDER_STATUS_LIST.contains(status)) {
            closedOrderCount = Math.max(closedOrderCount - 1, 0);
        }
        boutiqueLedgerDAO.setTotalActiveOrders(activeOrderCount);
        boutiqueLedgerDAO.setMonthlyActiveOrders(activeOrderCount);
        boutiqueLedgerDAO.setTotalClosedOrders(closedOrderCount);
        boutiqueLedgerDAO.setMonthlyClosedOrders(closedOrderCount);

    }

}
