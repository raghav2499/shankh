package com.darzee.shankh.service;

import com.darzee.shankh.dao.PortfolioDAO;
import com.darzee.shankh.dao.PortfolioOutfitsDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.entity.Portfolio;
import com.darzee.shankh.entity.Tailor;
import com.darzee.shankh.enums.*;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.PortfolioOutfitsRepo;
import com.darzee.shankh.repo.PortfolioRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.CreatePortfolioOutfitRequest;
import com.darzee.shankh.request.CreatePortfolioRequest;
import com.darzee.shankh.response.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class PortfolioService {

    @Autowired
    private ObjectImagesService objectImagesService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private SubOutfitTypeFactory subOutfitTypeFactory;

    @Autowired
    private PortfolioRepo portfolioRepo;

    @Autowired
    private TailorRepo tailorRepo;

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private DaoEntityMapper mapper;
    @Autowired
    private PortfolioOutfitsRepo portfolioOutfitsRepo;

    public ResponseEntity<UsernameAvailableResponse> isUsernameAvailable(String username) {
        boolean isUsernameAvailable = (!portfolioRepo.findByUsername(username).isPresent());
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
        List<String> requestSocialMedia = request.getSocialMedia();
        Map<String, String> socialMediaMap = new HashMap<>();
        for (Integer idx = 0; idx < requestSocialMedia.size(); idx++) {
            if (requestSocialMedia.get(idx) != null) {
                socialMediaMap.put(
                        SocialMedia.getSocialMediaOrdinalEnumMap().get(idx + 1).getName(),
                        requestSocialMedia.get(idx));
            }
        }
        PortfolioDAO portfolio = new PortfolioDAO(request.getUsername(), request.getAbout(), socialMediaMap, tailorDAO);
        portfolio = mapper.portfolioToPortfolioDAO(portfolioRepo.save(mapper.portfolioDAOToPortfolio(portfolio,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

        if (!StringUtils.isEmpty(request.getCoverImageReference())) {
            objectImagesService.saveObjectImages(Arrays.asList(request.getCoverImageReference()),
                    ImageEntityType.PORTFOLIO_COVER.getEntityType(),
                    portfolio.getId());
        }
        if (!StringUtils.isEmpty(request.getProfileImageReference())) {
            objectImagesService.saveObjectImages(Arrays.asList(request.getProfileImageReference()),
                    ImageEntityType.PORTFOLIO_PROFILE.getEntityType(),
                    portfolio.getId());
        }
        String successfulMessage = "Portfolio created successfully";
        String tailorName = tailorDAO.getName();
        String boutiqueName = tailorDAO.getBoutique().getName();
        CreatePortfolioResponse response = new CreatePortfolioResponse(successfulMessage, tailorName, boutiqueName,
                portfolio.getAboutDetails(), portfolio.getSocialMedia());
        return new ResponseEntity<CreatePortfolioResponse>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<CreatePortfolioOutfitResponse> createPortfolioOutfits(CreatePortfolioOutfitRequest request,
                                                                                Long portfolioId) {
        Optional<Portfolio> portfolio = portfolioRepo.findById(portfolioId);
        if (!portfolio.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Portfolio ID");
        }
        PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(portfolio.get(), new CycleAvoidingMappingContext());
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(request.getOutfitType());
        Integer subOutfitOrdinal = request.getSubOutfit();
        SubOutfitType subOutfitType = subOutfitTypeFactory.getSubOutfit(outfitType, subOutfitOrdinal);
        List<String> portfolioOutfitReferenceIds = request.getReferenceIds();
        if (subOutfitType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Sub Outfit Type");
        }
        ColorEnum color = ColorEnum.getColorOrdinalEnumMap().get(request.getColor());
        PortfolioOutfitsDAO portfolioOutfit = new PortfolioOutfitsDAO(request.getTitle(), outfitType, subOutfitOrdinal,
                color, portfolioDAO);
        portfolioOutfit = mapper.portfolioOutfitsToPortfolioOutfitsDAO(
                portfolioOutfitsRepo.save(mapper.portfolioOutfitsDAOToPortfolioOutfits(portfolioOutfit,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        if (!CollectionUtils.isEmpty(portfolioOutfitReferenceIds)) {
            objectImagesService.saveObjectImages(portfolioOutfitReferenceIds,
                    ImageEntityType.PORTFOLIO_OUTFIT.getEntityType(),
                    portfolioOutfit.getId());
        }
        String successMessage = "Portfolio outfit saved successfully";
        CreatePortfolioOutfitResponse response = new CreatePortfolioOutfitResponse(successMessage,
                portfolioOutfit.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<GetBoutiqueDetailsResponse> getPortfolio(Long tailorId) {
        Optional<Portfolio> portfolio = portfolioRepo.findByTailorId(tailorId);
        if(portfolio.isPresent()) {
            PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(portfolio.get(),
                    new CycleAvoidingMappingContext());
            String tailorName = tailorRepo.findNameById(tailorId);
            String boutiqueName = boutiqueRepo.findNameByAdminTailorId(tailorId);
            String portfolioProfileReference = objectImagesService.getProfileProfileReference(portfolioDAO.getId());
            String portfolioCoverReference = objectImagesService.getProfileCoverReference(portfolioDAO.getId());
            String portfolioProfileImageUrl = null;
            String portfolioCoverImageUrl = null;
            if(portfolioProfileReference != null) {
                portfolioProfileImageUrl = bucketService.getPortfolioImageShortLivedUrl(portfolioProfileReference);
            }
            if(portfolioCoverReference != null) {
                portfolioCoverImageUrl = bucketService.getPortfolioImageShortLivedUrl(portfolioCoverReference);
            }
            String successMessage = "Portfolio details fetched successfully";
            GetPortfolioDetailsResponse response = new GetPortfolioDetailsResponse(successMessage, tailorName,
                    boutiqueName, portfolioDAO.getId(), portfolioDAO.getSocialMedia(), portfolioDAO.getAboutDetails(),
                    portfolioDAO.getUsername(), portfolioProfileImageUrl, portfolioCoverImageUrl);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tailor's portfolio diesn't exist");
    }

}
