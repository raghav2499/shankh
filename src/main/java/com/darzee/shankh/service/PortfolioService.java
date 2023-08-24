package com.darzee.shankh.service;

import com.darzee.shankh.dao.PortfolioDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.entity.Portfolio;
import com.darzee.shankh.entity.Tailor;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.enums.SocialMedia;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.PortfolioRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.CreateOutfitPortfolioRequest;
import com.darzee.shankh.request.CreatePortfolioRequest;
import com.darzee.shankh.response.CreatePortfolioResponse;
import com.darzee.shankh.response.UsernameAvailableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.util.MapUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    private ObjectImagesService objectImagesService;

    @Autowired
    private PortfolioRepo portfolioRepo;

    @Autowired
    private TailorRepo tailorRepo;

    @Autowired
    private DaoEntityMapper mapper;

    public ResponseEntity<UsernameAvailableResponse> isUsernameAvailable(String username) {
        boolean isUsernameAvailable = (portfolioRepo.findByUsername(username) != null);
        String message = "Details fetched successfully";
        UsernameAvailableResponse response = new UsernameAvailableResponse(message, username, isUsernameAvailable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CreatePortfolioResponse> createPortfolio(CreatePortfolioRequest request) {
        Optional<Tailor> tailor = tailorRepo.findById(request.getTailorId());
        if (!tailor.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Tailor ID");
        }
        TailorDAO tailorDAO = mapper.tailorObjectToDao(tailor.get(), new CycleAvoidingMappingContext());
        Map<String, String> socialMediaMap = new HashMap<>();
        if (!MapUtils.isEmpty(request.getSocialMedia())) {
            socialMediaMap = request.getSocialMedia()
                    .entrySet()
                    .stream()
                    .filter(es -> SocialMedia.getSocialMediaOrdinalEnumMap().containsKey(es.getKey()))
                    .collect(Collectors.toMap(es -> SocialMedia.getSocialMediaOrdinalEnumMap().get(es.getKey()).toString(),
                            es -> es.getValue().toString()));
        }
        PortfolioDAO portfolio = new PortfolioDAO(request.getUsername(), request.getAbout(), socialMediaMap, tailorDAO);
        portfolio = mapper.portfolioToPortfolioDAO(portfolioRepo.save(mapper.portfolioDAOToPortfolio(portfolio,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

        if (request.getCoverImageReference() != null) {
            objectImagesService.saveObjectImages(Arrays.asList(request.getCoverImageReference()),
                    ImageEntityType.PORTFOLIO_COVER.getEntityType(),
                    portfolio.getId());
        }
        String successfulMessage = "Portfolio created successfully";
        String tailorName = tailorDAO.getName();
        String boutiqueName = tailorDAO.getBoutique().getName();
        CreatePortfolioResponse response = new CreatePortfolioResponse(successfulMessage, tailorName, boutiqueName,
                portfolio.getAboutDetails(), portfolio.getSocialMedia());
        return new ResponseEntity<CreatePortfolioResponse>(response, HttpStatus.CREATED);
    }

    public void createPortfolioOutfits(CreateOutfitPortfolioRequest request, Long portfolioId) {
        Optional<Portfolio> portfolio = portfolioRepo.findById(portfolioId);
        if (!portfolio.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Portfolio ID");
        }
        PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(portfolio.get(), new CycleAvoidingMappingContext());
        Port
    }

    public void getPortfolioOutfitImage(Long portfolioId, Integer fileType, String fileName) {

    }


    public void updatePortfolio(Long tailorId, CreatePortfolioRequest request) {

    }

}
