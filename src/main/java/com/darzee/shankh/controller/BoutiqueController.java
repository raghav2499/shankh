package com.darzee.shankh.controller;

import com.darzee.shankh.request.CreateBoutiqueRequest;
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

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBoutique(@Valid @RequestBody CreateBoutiqueRequest request) {
        return boutiqueService.addBoutique(request);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBoutiqueData(@PathVariable("id") String id) {
        return boutiqueService.getBoutiqueData(id);
    }

}