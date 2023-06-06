package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {

    @Autowired
    private BoutiqueLedgerService boutiqueLedgerService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    public ResponseEntity getReportData(String boutiqueIdString, Integer month, Integer year) {
        Long boutiqueId = Long.parseLong(boutiqueIdString);
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(boutiqueId);
        BoutiqueDAO boutiqueDAO = null;
        if(optionalBoutique.isPresent()) {
             boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(), new CycleAvoidingMappingContext());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid boutique Id");
        }
        LocalDate currentDate = LocalDate.now();
        Integer startYear = boutiqueDAO.getCreatedAt() == null ? 2023 : boutiqueDAO.getCreatedAt().getYear();
        Integer startMonth = boutiqueDAO.getCreatedAt() == null ? 5 : boutiqueDAO.getCreatedAt().getMonthValue();
        if (year < startYear ||
                (year == startYear &&
                        month < startMonth)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Reporting data is available after year"
                            + startYear
                            + " and month "
                            + startMonth);
        } else if (year > currentDate.getYear() || (year == currentDate.getYear() && month > currentDate.getMonthValue())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Reporting of future dates is not supported");
        }

        LedgerDashboardData ledgerDashboardData = boutiqueLedgerService.getLedgerDashboardDetails(boutiqueId,
                month,
                year);
        CustomerDashboard customerDashboard = customerService.getCustomerDashboardDetails(boutiqueId, month, year);
        List<TopCustomerData> topCustomerData = orderService.getTopCustomerData(boutiqueId, month, year);
        SalesDashboard weekwiseSalesSplit = orderService.getWeekWiseSales(boutiqueId, month, year);
        List<OrderTypeDashboardData> orderTypeSalesSplit = orderService.getOrderTypeWiseSales(boutiqueId, month, year);
        Integer activeSinceMonth = startMonth;
        Integer activeSinceYear = startYear;

        String successMessage = "Reporting data fetched successfully";
        BoutiqueReportResponse response = new BoutiqueReportResponse(ledgerDashboardData,
                customerDashboard,
                topCustomerData,
                weekwiseSalesSplit,
                orderTypeSalesSplit,
                activeSinceMonth,
                activeSinceYear,
                successMessage);
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
