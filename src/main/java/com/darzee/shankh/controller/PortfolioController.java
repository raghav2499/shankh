package com.darzee.shankh.controller;

import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.request.CreateOutfitPortfolioRequest;
import com.darzee.shankh.request.CreatePortfolioRequest;
import com.darzee.shankh.response.CreatePortfolioResponse;
import com.darzee.shankh.response.UsernameAvailableResponse;
import com.darzee.shankh.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.util.StringUtils;

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

    @PostMapping(value = "/portfolio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePortfolioResponse> createPortfolio(@Valid CreatePortfolioRequest request) {
        return portfolioService.createPortfolio(request);
    }

    @PostMapping(value = "/{portfolio_id}/portfolio_outfit", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createPortfolioOutfit(@Valid CreateOutfitPortfolioRequest request,
                                      @PathVariable("portfolio_id") Long portfolioId) {
        portfolioService.createPortfolioOutfits(request, portfolioId);
    }

    @GetMapping("/{portfolio_id}/get_file")
    public void getFiles(@PathVariable("portfolio_id") Long portfolioId,
                         @RequestParam("type") Integer filetype,
                         @RequestParam("name") String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file name");
        }
        if (filetype == null || filetype > OutfitType.values().length) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type");
        }
        portfolioService.getPortfolioOutfitImage(portfolioId, filetype, fileName);
    }
}
