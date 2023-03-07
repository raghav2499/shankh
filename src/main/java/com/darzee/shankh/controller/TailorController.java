package com.darzee.shankh.controller;

import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.service.TailorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tailor")
public class TailorController {

    @Autowired
    private TailorService tailorService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addTailor(TailorDAO tailorDAO) {
        return tailorService.addTailor(tailorDAO);
    }
}
