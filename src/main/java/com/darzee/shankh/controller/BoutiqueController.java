package com.darzee.shankh.controller;

import com.darzee.shankh.request.AddBoutiqueDetailsRequest;
import com.darzee.shankh.service.BoutiqueLedgerService;
import com.darzee.shankh.service.BoutiqueService;
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
    private BoutiqueLedgerService boutiqueLedgerService;

    @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBoutiqueDetails(@Valid @RequestBody AddBoutiqueDetailsRequest request) {
        return boutiqueService.addBoutiqueDetails(request);
    }

    @GetMapping(value = "/{id}/ledger", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getLedgerData(@PathVariable("id") String id) {
        return boutiqueLedgerService.getLedgerData(id);
    }

}