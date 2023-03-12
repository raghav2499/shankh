package com.darzee.shankh.controller;

import com.darzee.shankh.request.TailorLoginRequest;
import com.darzee.shankh.request.TailorSignUpRequest;
import com.darzee.shankh.service.TailorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tailor")
public class TailorController {

    @Autowired
    private TailorService tailorService;

    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity boutiqueLogin(@Valid @RequestBody TailorLoginRequest request) {
        return tailorService.tailorLogin(request);
    }
    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity tailorSignUp(@Valid @RequestBody TailorSignUpRequest request) {
        return tailorService.tailorSignup(request);
    }
}
