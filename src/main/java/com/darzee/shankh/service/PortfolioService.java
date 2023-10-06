package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.entity.Portfolio;
import com.darzee.shankh.entity.PortfolioOutfits;
import com.darzee.shankh.entity.Tailor;
import com.darzee.shankh.enums.*;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.CreatePortfolioOutfitRequest;
import com.darzee.shankh.request.CreatePortfolioRequest;
import com.darzee.shankh.response.*;
import com.darzee.shankh.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    private ObjectImagesService objectImagesService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private OutfitService outfitService;

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

    @Autowired
    private ImageReferenceRepo imageReferenceRepo;

    @Autowired
    private AmazonClient s3Client;

    public ResponseEntity<UsernameAvailableResponse> isUsernameAvailable(String username) {
        boolean isUsernameAvailable = usernameAvailable(username);
        String message = "Details fetched successfully";
        UsernameAvailableResponse response = new UsernameAvailableResponse(message, username, isUsernameAvailable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CreatePortfolioResponse> createPortfolio(CreatePortfolioRequest request) {
        CreatePortfolioResponse response = new CreatePortfolioResponse();
        Optional<Tailor> tailor = tailorRepo.findById(request.getTailorId());
        TailorDAO tailorDAO = mapper.tailorObjectToDao(tailor.get(), new CycleAvoidingMappingContext());
        BoutiqueDAO boutique = tailorDAO.getBoutique();

        if (!tailor.isPresent()) {
            String invalidTailorMessage = "Invalid Tailor ID";
            response.setMessage(invalidTailorMessage);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!usernameAvailable(request.getUsername())) {
            String usernameUnavailable = "Username not available";
            response.setMessage(usernameUnavailable);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Optional<Portfolio> optionalExistingPortfolio = portfolioRepo.findByTailorId(request.getTailorId());
        if (optionalExistingPortfolio.isPresent()) {
            PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(optionalExistingPortfolio.get(),
                    new CycleAvoidingMappingContext());
            String portfolioExistsMessage = "Portfolio exists for this tailor";
            response = new CreatePortfolioResponse(portfolioExistsMessage, tailorDAO.getName(), boutique.getName(),
                    portfolioDAO.getAboutDetails(), portfolioDAO.getSocialMedia());
            return new ResponseEntity<CreatePortfolioResponse>(response, HttpStatus.OK);
        }
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
        String boutiqueName = boutique.getName();
        response = new CreatePortfolioResponse(successfulMessage, tailorName, boutiqueName,
                portfolio.getAboutDetails(), portfolio.getSocialMedia());
        return new ResponseEntity<CreatePortfolioResponse>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<CreatePortfolioOutfitResponse> createPortfolioOutfits(CreatePortfolioOutfitRequest request,
                                                                                Long portfolioId) throws Exception {
        CreatePortfolioOutfitResponse response = new CreatePortfolioOutfitResponse();
        Optional<Portfolio> portfolio = portfolioRepo.findById(portfolioId);
        if (!portfolio.isPresent()) {
            response.setMessage("Portfolio for this tailor doesn't exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(portfolio.get(), new CycleAvoidingMappingContext());
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(request.getOutfitType());
        Integer subOutfitOrdinal = request.getSubOutfit();
        Set<Integer> possibleSubOutfits = outfitService.getSubOutfitMap(outfitType).keySet();
        List<String> portfolioOutfitReferenceIds = request.getReferenceIds();
        if (!possibleSubOutfits.contains(subOutfitOrdinal)) {
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
        response = new CreatePortfolioOutfitResponse(successMessage,
                portfolioOutfit.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<GetBoutiqueDetailsResponse> getPortfolio(Long tailorId) {
        Optional<Portfolio> portfolio = portfolioRepo.findByTailorId(tailorId);
        GetPortfolioDetailsResponse response = new GetPortfolioDetailsResponse();
        if (portfolio.isPresent()) {
            PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(portfolio.get(),
                    new CycleAvoidingMappingContext());
            String tailorName = tailorRepo.findNameById(tailorId);
            String boutiqueName = boutiqueRepo.findNameByAdminTailorId(tailorId);
            String portfolioProfileReference = objectImagesService.getProfileProfileReference(portfolioDAO.getId());
            String portfolioCoverReference = objectImagesService.getProfileCoverReference(portfolioDAO.getId());
            String portfolioProfileImageUrl = null;
            String portfolioCoverImageUrl = null;
            if (portfolioProfileReference != null) {
                portfolioProfileImageUrl = bucketService.getPortfolioImageShortLivedUrl(portfolioProfileReference);
            }
            if (portfolioCoverReference != null) {
                portfolioCoverImageUrl = bucketService.getPortfolioImageShortLivedUrl(portfolioCoverReference);
            }
            String successMessage = "Portfolio details fetched successfully";
            response = new GetPortfolioDetailsResponse(successMessage, tailorName,
                    boutiqueName, portfolioDAO.getId(), portfolioDAO.getSocialMedia(), portfolioDAO.getAboutDetails(),
                    portfolioDAO.getUsername(), portfolioProfileImageUrl, portfolioCoverImageUrl);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        String message = "Tailor's portfolio doesn't exist";
        response.setMessage(message);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public ResponseEntity<GetPortfolioOutfitsResponse> getPortfolioOutfit(String username, String outfitTypes,
                                                                          String subOutfits) throws Exception {
        GetPortfolioOutfitsResponse response = new GetPortfolioOutfitsResponse();
        List<Integer> outfitTypeOrdinals = new ArrayList<>();
        List<Integer> subOutfitTypeOrdinals = new ArrayList<>();

        Optional<Portfolio> portfolio = portfolioRepo.findByUsername(username);
        if (!portfolio.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Portfolio Id");
        }
        PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(portfolio.get(), new CycleAvoidingMappingContext());
        List<OutfitType> outfits = null;
        if (!StringUtils.isEmpty(outfitTypes)) {
            outfitTypeOrdinals = Arrays.stream(outfitTypes.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            outfits = outfitTypeOrdinals.stream()
                    .map(outfitTypeOrdinal -> OutfitType.getOutfitOrdinalEnumMap().get(outfitTypeOrdinal))
                    .collect(Collectors.toList());
        } else {
            outfits = Arrays.asList(OutfitType.values());
        }
        if (!StringUtils.isEmpty(subOutfits)) {
            subOutfitTypeOrdinals = Arrays.stream(subOutfits.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } else {
            OutfitTypeService outfitTypeService = null;
            for (OutfitType outfitType : outfits) {
                Map<Integer, String> subOutfitMap = outfitService.getSubOutfitMap(outfitType);
                if (subOutfitMap.size() > 0) {
                    subOutfitTypeOrdinals.addAll(subOutfitMap.keySet());
                }
            }
        }
        List<PortfolioOutfits> portfolioOutfits =
                portfolioOutfitsRepo.findAllByPortfolioIdAndOutfitTypeInAndSubOutfitTypeIn(portfolioDAO.getId(),
                        outfits,
                        subOutfitTypeOrdinals);
        if (CollectionUtils.isEmpty(portfolioOutfits)) {
            return new ResponseEntity(response, HttpStatus.OK);
        }

        List<PortfolioOutfitsDAO> portfolioOutfitsDAOs = mapper.portfolioObjectListToDAOList(portfolioOutfits,
                new CycleAvoidingMappingContext());
        Map<Integer, List<PortfolioOutfitDetails>> outfitDetails = new HashMap<>();
        for (PortfolioOutfitsDAO portfolioOutfit : portfolioOutfitsDAOs) {
            List<String> portfolioOutfitReferences =
                    objectImagesService.getPortfolioOutfitsReferenceIds(portfolioOutfit.getId());
            List<String> portfolioOutfitImageLinks = getPortfolioOutfitImageLinks(portfolioOutfitReferences);

            PortfolioOutfitDetails portfolioOutfitDetails =
                    new PortfolioOutfitDetails(portfolioOutfit.getSubOutfitType(), portfolioOutfit.getTitle(),
                            portfolioOutfitImageLinks, portfolioOutfit.getCreatedAt().toLocalDate());
            List<PortfolioOutfitDetails> portfolioOutfitDetailsList =
                    Optional.ofNullable(outfitDetails.get(portfolioOutfit.getOutfitType().getOrdinal())).orElse(new ArrayList<>());
            portfolioOutfitDetailsList.add(portfolioOutfitDetails);
            outfitDetails.put(portfolioOutfit.getOutfitType().getOrdinal(), portfolioOutfitDetailsList);
        }
        response.setOutfitDetails(outfitDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<String> getPortfolioOutfitImageLinks(List<String> portfolioOutfitReferenceIds) {
        if (CollectionUtils.isEmpty(portfolioOutfitReferenceIds)) {
            return new ArrayList<>();
        }
        List<ImageReference> portfolioOutfitReferences =
                imageReferenceRepo.findAllByReferenceIdIn(portfolioOutfitReferenceIds);
        if (!CollectionUtils.isEmpty(portfolioOutfitReferences)) {
            List<ImageReferenceDAO> imageReferenceDAOs =
                    CommonUtils.mapList(portfolioOutfitReferences, mapper::imageReferenceToImageReferenceDAO);
            List<String> portfolioOutfitImageFileNames =
                    imageReferenceDAOs.stream().map(imageRef -> imageRef.getImageName()).collect(Collectors.toList());
            return s3Client.generateShortLivedUrlForPortfolio(portfolioOutfitImageFileNames);
        }
        return new ArrayList<>();
    }

    private boolean usernameAvailable(String username) {
        return (!portfolioRepo.findByUsername(username).isPresent());
    }

    public ResponseEntity<GetPortfolioFilterResponse> getFilters(Long tailorId) throws Exception {
        Optional<Portfolio> portfolio = portfolioRepo.findByTailorId(tailorId);
        GetPortfolioFilterResponse response = null;
        if (!portfolio.isPresent()) {
            String message = "Portfolio doesn't exist";
            response = new GetPortfolioFilterResponse(message);
        }
        PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(portfolio.get(),
                new CycleAvoidingMappingContext());
        List<PortfolioOutfitsDAO> portfolioOutfits = portfolioDAO.getPortfolioOutfits();
        Map<String, List<String>> subOutfitMap = new HashMap<>();
        List<String> colorFilterList = new ArrayList<>();
        for (PortfolioOutfitsDAO portfolioOutfitsDAO : portfolioOutfits) {
            OutfitType outfitType = portfolioOutfitsDAO.getOutfitType();
            Integer subOutfitIdx = portfolioOutfitsDAO.getSubOutfitType();
            String portfolioSubOutfit = outfitService.getSubOutfitName(outfitType, subOutfitIdx);
            List<String> subOutfitList = Optional.ofNullable(subOutfitMap.get(outfitType.getName())).orElse(new ArrayList<>());

            if (!subOutfitList.contains(portfolioSubOutfit)) {
                subOutfitList.add(portfolioSubOutfit);
                subOutfitMap.put(outfitType.getName(), subOutfitList);
            }
            colorFilterList.add(portfolioOutfitsDAO.getColor().getName());
        }
        List<OutfitFilter> outfitFilters = new ArrayList<>();
        for (Map.Entry<String, List<String>> pair : subOutfitMap.entrySet()) {
            OutfitFilter outfitFilter = new OutfitFilter(pair.getKey(), pair.getValue());
            outfitFilters.add(outfitFilter);
        }
        String successMessage = "Filters fetched successfully";
        response = new GetPortfolioFilterResponse(successMessage, outfitFilters, colorFilterList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
