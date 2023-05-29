package com.darzee.shankh.service;

import com.darzee.shankh.constants.Constants;
import com.darzee.shankh.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private BoutiqueLedgerService boutiqueLedgerService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    public ResponseEntity getReportData(String boutiqueIdString, Integer month, Integer year) {
        Long boutiqueId = Long.parseLong(boutiqueIdString);
        LocalDate reportingStartDate = Constants.REPORTING_START_DATE;
        LocalDate currentDate = LocalDate.now();
        if (year < reportingStartDate.getYear() ||
                (year == reportingStartDate.getYear() &&
                        month < reportingStartDate.getMonthValue())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Reporting data is available after " + Constants.REPORTING_START_DATE);
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
        String successMessage = "Reporting data fetched successfully";
        BoutiqueReportResponse response = new BoutiqueReportResponse(ledgerDashboardData,
                customerDashboard,
                topCustomerData,
                weekwiseSalesSplit,
                orderTypeSalesSplit, successMessage);
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
