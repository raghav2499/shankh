package com.darzee.shankh.controller;

import com.darzee.shankh.request.CreatePortfolioOutfitRequest;
import com.darzee.shankh.request.CreatePortfolioRequest;
import com.darzee.shankh.request.UpdatePortfolioRequest;
import com.darzee.shankh.response.*;
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
                                                                               @PathVariable("portfolio_id") Long portfolioId) throws Exception {
        return portfolioService.createPortfolioOutfits(request, portfolioId);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<GetBoutiqueDetailsResponse> getPortfolio(@RequestParam("tailor_id") Long tailorId) {
        return portfolioService.getPortfolio(tailorId);
    }

    @PutMapping(value = "/{tailor_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePortfolio(@PathVariable("tailor_id") Long tailorId, @RequestBody @Valid UpdatePortfolioRequest request){
        return portfolioService.updatePortfolio(tailorId, request);
    }


    @GetMapping(value = "/portfolio_outfit", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<GetPortfolioOutfitsResponse> getPortfolioOutfit(@RequestParam("username") String username,
                                                                          @RequestParam(value = "outfit_type", required = false, defaultValue = "") String outfitType,
                                                                          @RequestParam(value = "sub_outfit", required = false, defaultValue = "") String subOutfit) throws Exception {
        return portfolioService.getPortfolioOutfit(username, outfitType, subOutfit);
    }

    @GetMapping(value = "/filters", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<GetPortfolioFilterResponse> getPortfolioFilters(@RequestParam("tailor_id") Long tailorId)
            throws Exception {
        return portfolioService.getFilters(tailorId);
    }

    @GetMapping(value = "/colors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetPortfolioColorResponse> getPortfolioColors() {
        return portfolioService.getColors();
    }
}
