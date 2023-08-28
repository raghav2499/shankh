package com.darzee.shankh.controller;

import com.darzee.shankh.request.CreatePortfolioOutfitRequest;
import com.darzee.shankh.request.CreatePortfolioRequest;
import com.darzee.shankh.response.CreatePortfolioOutfitResponse;
import com.darzee.shankh.response.CreatePortfolioResponse;
import com.darzee.shankh.response.GetBoutiqueDetailsResponse;
import com.darzee.shankh.response.UsernameAvailableResponse;
import com.darzee.shankh.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping(value = "/username_available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsernameAvailableResponse> usernameAvailable(@RequestParam("username") String username) {
        return portfolioService.isUsernameAvailable(username);
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePortfolioResponse> createPortfolio(@RequestBody @Valid CreatePortfolioRequest request) {
        return portfolioService.createPortfolio(request);
    }

    @PostMapping(value = "/{portfolio_id}/portfolio_outfit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePortfolioOutfitResponse> createPortfolioOutfit(@RequestBody @Valid CreatePortfolioOutfitRequest request,
                                                                               @PathVariable("portfolio_id") Long portfolioId) {
        return portfolioService.createPortfolioOutfits(request, portfolioId);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetBoutiqueDetailsResponse> getPortfolio(@RequestParam("tailor_id") Long tailorId) {
        return portfolioService.getPortfolio(tailorId);
    }


    @GetMapping(value = "/{portfolio_id}/portfolio_outfit", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getPortfolioOutfit(@RequestParam("sub_outfit") Integer subOutfit) {

    }
}
