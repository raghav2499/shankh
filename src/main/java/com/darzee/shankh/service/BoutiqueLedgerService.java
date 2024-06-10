package com.darzee.shankh.service;

import com.darzee.shankh.constants.ErrorMessages;
import com.darzee.shankh.constants.SuccesssMessages;
import com.darzee.shankh.dao.BoutiqueLedgerDAO;
import com.darzee.shankh.dao.BoutiqueLedgerSnapshotDAO;
import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.entity.BoutiqueLedger;
import com.darzee.shankh.entity.BoutiqueLedgerSnapshot;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.repo.BoutiqueLedgerSnapshotRepo;
import com.darzee.shankh.response.GetBoutiqueLedgerDataResponse;
import com.darzee.shankh.response.LedgerDashboardData;
import com.darzee.shankh.service.translator.ErrorMessageTranslator;
import com.darzee.shankh.service.translator.SuccessMessageTranslator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static com.darzee.shankh.constants.Constants.ACTIVE_ORDER_STATUS_LIST;
import static com.darzee.shankh.constants.Constants.CLOSED_ORDER_STATUS_LIST;

@Service
public class BoutiqueLedgerService {

    @Autowired
    private BoutiqueLedgerRepo repo;

    @Autowired
    private BoutiqueLedgerSnapshotRepo snapshotRepo;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private ErrorMessageTranslator errorMessageTranslator;

    @Autowired
    private SuccessMessageTranslator successMessageTranslator;

    public ResponseEntity getLedgerData(String boutiqueIdString) {
        Long boutiqueId = Long.parseLong(boutiqueIdString);
        BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
        if (boutiqueLedger != null) {
            BoutiqueLedgerDAO boutiqueLedgerObject = mapper.boutiqueLedgerObjectToDAO(boutiqueLedger, new CycleAvoidingMappingContext());
            GetBoutiqueLedgerDataResponse response = new GetBoutiqueLedgerDataResponse();
            response.setMonthlyLedgerDashboardData(boutiqueLedgerObject.getMonthlyAmountRecieved(),
                    boutiqueLedgerObject.getMonthlyPendingAmount(), boutiqueLedgerObject.getMonthlyActiveOrders(),
                    boutiqueLedgerObject.getMonthlyClosedOrders());
            response.setOverallLedgerDashboardData(boutiqueLedgerObject.getTotalAmountRecieved(),
                    boutiqueLedgerObject.getTotalPendingAmount(), boutiqueLedgerObject.getTotalActiveOrders(),
                    boutiqueLedgerObject.getTotalClosedOrders());
            return new ResponseEntity(response, HttpStatus.OK);
        }
        String errorMessage =errorMessageTranslator.getTranslatedMessage(ErrorMessages.BOUTIQUE_LEDGER_NOT_FOUND_ERROR);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    /*
     * Boutique Ledger amount updates in 4 scenarios as of today
     * 1. Order confirmation
     * a. + Total amount -> Order Amount
     * b. + Amount Recieved -> Amount Recieved
     * 2. Order price updation for any of the items
     * 3. Recieving some order payment
     * 4. Order deletion
     * a. - Total amount -> Order Amount
     * b. - Amount Recieved -> Order Amount
     */
    @Transactional
    public BoutiqueLedgerDAO updateBoutiqueLedgerAmountDetails(BoutiqueLedgerDAO boutiqueLedgerDAO,
                                                               Long boutiqueId, Double deltaPendingAmount,
                                                               Double deltaAmountRecieved) {
        if (boutiqueLedgerDAO == null) {
            BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
            if (boutiqueLedger != null) {
                boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
            } else {
                String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.BOUTIQUE_LEDGER_NOT_EXIST_ERROR_MSG + boutiqueId)+boutiqueId;
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
            }
        }
        boutiqueLedgerDAO.addOrderAmountToBoutiqueLedger(deltaPendingAmount, deltaAmountRecieved);
        boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return boutiqueLedgerDAO;
    }

    @Transactional
    public BoutiqueLedgerDAO updateCountOnOrderConfirmation(BoutiqueLedgerDAO boutiqueLedgerDAO, Long boutiqueId) {
        if (boutiqueLedgerDAO == null) {
            BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
            if (boutiqueLedger != null) {
                boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
            } else {
                String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.BOUTIQUE_LEDGER_NOT_EXIST_ERROR_MSG + boutiqueId)+boutiqueId;
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
            }
        }
        boutiqueLedgerDAO.incrementActiveOrderCount(1);
        boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return boutiqueLedgerDAO;
    }

    /**
     * 2 cases :
     * 1. Order Confirmation
     * 2. Order delivered
     */
    @Transactional
    public BoutiqueLedgerDAO handleBoutiqueLedgerOnOrderUpdation(BoutiqueLedgerDAO boutiqueLedgerDAO,
                                                                 Long boutiqueId,
                                                                 Integer activeOrderCountChange,
                                                                 Integer closedOrderCountChange) {
        if (boutiqueLedgerDAO == null) {
            BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
            if (boutiqueLedger != null) {
                boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
            } else {
                String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.BOUTIQUE_LEDGER_NOT_EXIST_ERROR_MSG + boutiqueId)+boutiqueId;
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
            }
        }

        if (activeOrderCountChange != 0) {
            boutiqueLedgerDAO.incrementActiveOrderCount(activeOrderCountChange);
        }
        if (closedOrderCountChange != 0) {
            boutiqueLedgerDAO.incrementClosedOrderCount(closedOrderCountChange);
        }
        boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return boutiqueLedgerDAO;
    }

    @Transactional
    public BoutiqueLedgerDAO handleBoutiqueLedgerOnOrderDeletion(BoutiqueLedgerDAO boutiqueLedgerDAO,
                                                                 Long boutiqueId,
                                                                 OrderDAO orderDAO) {
        if (boutiqueLedgerDAO == null) {
            BoutiqueLedger boutiqueLedger = repo.findByBoutiqueId(boutiqueId);
            if (boutiqueLedger != null) {
                boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
            } else {
                String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.BOUTIQUE_LEDGER_NOT_EXIST_ERROR_MSG + boutiqueId)+boutiqueId;
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
            }
        }
        if (ACTIVE_ORDER_STATUS_LIST.contains(orderDAO.getOrderStatus())) {
            boutiqueLedgerDAO.decrementActiveOrderCount(1);
        }
        if (CLOSED_ORDER_STATUS_LIST.contains(orderDAO.getOrderStatus())) {
            boutiqueLedgerDAO.decrementClosedOrderCount(1);
        }
        boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return boutiqueLedgerDAO;

    }

    @Transactional
    public ResponseEntity resetBoutiqueLedgerData(String boutiqueIdString) {
        Long boutiqueId = Long.parseLong(boutiqueIdString);
        BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
        if (boutiqueLedgerDAO != null) {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousMonthDate = currentDate.minusMonths(1);
            Integer month = previousMonthDate.getMonthValue();
            Integer year = previousMonthDate.getYear();

            BoutiqueLedgerSnapshotDAO boutiqueLedgerSnapshotDAO = new BoutiqueLedgerSnapshotDAO(boutiqueLedgerDAO.getMonthlyPendingAmount(), boutiqueLedgerDAO.getMonthlyAmountRecieved(), boutiqueLedgerDAO.getMonthlyActiveOrders(),boutiqueLedgerDAO.getMonthlyClosedOrders(), boutiqueId, month, year);
            boutiqueLedgerDAO.resetMonthlyDetailsInLedger();
            snapshotRepo.save(mapper.boutiqueLedgerSnapshotDAOToSnapshot(boutiqueLedgerSnapshotDAO));
            repo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO, new CycleAvoidingMappingContext()));
            String successMessage = successMessageTranslator.getTranslatedMessage(SuccesssMessages.BOUTIQUE_DATA_RESET_SUCCESS);
            return new ResponseEntity(successMessage, HttpStatus.OK);
        }
        String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.BOUTIQUE_LEDGER_NOT_EXIST_ERROR_MSG + boutiqueIdString);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    public LedgerDashboardData getLedgerDashboardDetails(Long boutiqueId, int month, int year) {
        BoutiqueLedgerDAO boutiqueLedgerDAO = mapper.boutiqueLedgerObjectToDAO(repo.findByBoutiqueId(boutiqueId), new CycleAvoidingMappingContext());
        if (boutiqueLedgerDAO == null) {
            String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_BOUTIQUE_ID_ERROR + boutiqueId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        LedgerDashboardData ledgerDashboardData = null;
        LocalDate currentDate = LocalDate.now();
        if (currentDate.getMonthValue() == month && currentDate.getYear() == year) {
            ledgerDashboardData = new LedgerDashboardData(boutiqueLedgerDAO.getMonthlyAmountRecieved(),
                    boutiqueLedgerDAO.getTotalPendingAmount(), boutiqueLedgerDAO.getTotalActiveOrders(),
                    boutiqueLedgerDAO.getMonthlyClosedOrders());
        } else {
            Optional<BoutiqueLedgerSnapshot> ledgerSnapshot = snapshotRepo.findByBoutiqueIdAndMonthAndYear(boutiqueId, month, year);
            if (ledgerSnapshot.isPresent()) {
                BoutiqueLedgerSnapshotDAO ledgerSnapshotDAO = mapper.boutiqueLedgerSnapshotToSnapshotDAO(ledgerSnapshot.get());
                ledgerDashboardData = new LedgerDashboardData(ledgerSnapshotDAO, boutiqueLedgerDAO.getTotalPendingAmount(),
                        boutiqueLedgerDAO.getTotalActiveOrders());
            }
        }
        return ledgerDashboardData;
    }
}
