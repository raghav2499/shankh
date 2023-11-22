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

    @PutMapping(value = "/portfolio_outfit/{portfolio_outfit_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePortfolioOutfitResponse> updatePortfolioOutfit(@RequestBody @Valid CreatePortfolioOutfitRequest request,
                                                                             @PathVariable("portfolio_outfit_id") Long portfolioOutfitId) throws Exception {
        return portfolioService.updatePortfolioOutfits(request, portfolioOutfitId);
    }

    @PutMapping(value = "/invalidate_portfolio_outfit/{portfolio_outfit_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePortfolioOutfitResponse> invalidatePortfolioOutfit(@PathVariable("portfolio_outfit_id") Long portfolioOutfitId) throws Exception {
        return portfolioService.invalidatePortfolioOutfit(portfolioOutfitId);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<GetBoutiqueDetailsResponse> getPortfolio(@RequestParam(value = "tailor_id", required = false) Long tailorId,
                                                                   @RequestParam(value = "username", required = false) String username) {
        return portfolioService.getPortfolio(tailorId, username);
    }

    @PutMapping(value = "/{portfolio_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePortfolio(@PathVariable("portfolio_id") Long portfolioId, @RequestBody @Valid UpdatePortfolioRequest request){
        return portfolioService.updatePortfolio(portfolioId, request);
    }

    @GetMapping(value = "/{id}/portfolio_outfit", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<GetPortfolioOutfitsResponse> getPortfolioOutfit(@PathVariable("id") Long portfolioId,
                                                                          @RequestParam(value = "outfit_type", required = false, defaultValue = "") String outfitType,
                                                                          @RequestParam(value = "sub_outfit", required = false, defaultValue = "") String subOutfit,
                                                                          @RequestParam(value = "color", required = false, defaultValue = "") String color) throws Exception {
        return portfolioService.getPortfolioOutfit(portfolioId, outfitType, subOutfit, color);
    }

    @GetMapping(value = "/filters", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<GetPortfolioFilterResponse> getPortfolioFilters(@RequestParam(value = "tailor_id", required = false) Long tailorId,
                                                                          @RequestParam(value = "username", required = false) String username)
            throws Exception {
        return portfolioService.getFilters(tailorId, username);
    }

    @GetMapping(value = "/colors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetPortfolioColorResponse> getPortfolioColors() {
        return portfolioService.getColors();
    }
}
