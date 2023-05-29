package com.darzee.shankh.controller;

import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.request.UpdateBoutiqueDetails;
import com.darzee.shankh.service.BoutiqueLedgerService;
import com.darzee.shankh.service.BoutiqueService;
import com.darzee.shankh.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/boutique")
public class BoutiqueController {

    @Autowired
    private BoutiqueService boutiqueService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private BoutiqueLedgerService boutiqueLedgerService;
    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateBoutique(@PathVariable("id") Long boutiqueId,
                                         @Valid @RequestBody UpdateBoutiqueDetails request) {
        return boutiqueService.updateBoutiqueDetails(boutiqueId, request);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBoutiqueDetails(@PathVariable("id") Long id) {
        return boutiqueService.getBoutiqueDetails(id);
    }

    @GetMapping(value = "/{id}/ledger", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getLedgerData(@PathVariable("id") String id) {
        return boutiqueLedgerService.getLedgerData(id);
    }

    @GetMapping(value = "/{id}/report", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBoutiqueReport(@PathVariable("id") String id,
                                            @RequestParam("month") Integer month,
                                            @RequestParam("year") Integer year) {
        return dashboardService.getReportData(id, month, year);
    }


    @PostMapping(value = "/{id}/ledger/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity resetBoutiqueLedgerData(@PathVariable("id") String boutiqueId) {
        return boutiqueLedgerService.resetBoutiqueLedgerData(boutiqueId);
    }

}