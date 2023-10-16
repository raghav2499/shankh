package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.PortfolioDAO;
import com.darzee.shankh.dao.PortfolioOutfitsDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.ImageReferenceDAO;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.entity.Portfolio;
import com.darzee.shankh.entity.PortfolioOutfits;
import com.darzee.shankh.entity.Tailor;
import com.darzee.shankh.enums.ColorEnum;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.enums.SocialMedia;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.CreatePortfolioOutfitRequest;
import com.darzee.shankh.request.CreatePortfolioRequest;
import com.darzee.shankh.request.UpdatePortfolioRequest;
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
            objectImagesService.saveObjectImages(Collections.singletonList(request.getCoverImageReference()),
                    ImageEntityType.PORTFOLIO_COVER.getEntityType(),
                    portfolio.getId());
        }
        if (!StringUtils.isEmpty(request.getProfileImageReference())) {
            objectImagesService.saveObjectImages(Collections.singletonList(request.getProfileImageReference()),
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

    public ResponseEntity updatePortfolio(Long tailorId, UpdatePortfolioRequest request) {
        Optional<Portfolio> portfolio = portfolioRepo.findByTailorId(tailorId);
        GetPortfolioDetailsResponse response = new GetPortfolioDetailsResponse();
        if (portfolio.isPresent()) {
            Optional<Tailor> tailor = tailorRepo.findById(tailorId);
            TailorDAO tailorDAO = mapper.tailorObjectToDao(tailor.get(), new CycleAvoidingMappingContext());
            BoutiqueDAO boutique = tailorDAO.getBoutique();
            PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(portfolio.get(), new CycleAvoidingMappingContext());

            if (!StringUtils.isEmpty(request.getUsername())) {
                if (portfolioDAO.getUsernameUpdatesCounts() >= 2) {
                    String usernameUpdateNotAllowed = "Portfolio username update is not allowed as you have changed your username 2 times already!";
                    response.setMessage(usernameUpdateNotAllowed);
                    return new ResponseEntity(response, HttpStatus.OK);
                }
                portfolioDAO.setUsername(request.getUsername());
                portfolioDAO.updateUsernameCount();
            }
            if (!StringUtils.isEmpty(request.getAbout())) {
                portfolioDAO.setAboutDetails(request.getAbout());
            }

            List<String> updatedSocialMedia = request.getSocialMedia();
            Map<String, String> updatedSocialMediaMap = new HashMap<>();
            if (updatedSocialMedia != null && !updatedSocialMedia.isEmpty()) {
                for (Integer idx = 0; idx < updatedSocialMedia.size(); idx++) {
                    if (updatedSocialMedia.get(idx) != null) {
                        updatedSocialMediaMap.put(SocialMedia.getSocialMediaOrdinalEnumMap().get(idx + 1).getName(), updatedSocialMedia.get(idx));
                    }
                }
                portfolioDAO.setSocialMedia(updatedSocialMediaMap);
            }
            PortfolioDAO updatedPortfolio = mapper.portfolioToPortfolioDAO(portfolioRepo.save(mapper.portfolioDAOToPortfolio(portfolioDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
            if (!StringUtils.isEmpty(request.getCoverImageReference())) {
                objectImagesService.invalidateExistingReferenceIds(ImageEntityType.PORTFOLIO_COVER.getEntityType(), portfolioDAO.getId());
                objectImagesService.saveObjectImages(Collections.singletonList(request.getProfileImageReference()), ImageEntityType.PORTFOLIO_COVER.getEntityType(), portfolioDAO.getId());
            }
            if (!StringUtils.isEmpty(request.getProfileImageReference())) {
                objectImagesService.invalidateExistingReferenceIds(ImageEntityType.PORTFOLIO_PROFILE.getEntityType(), portfolioDAO.getId());
                objectImagesService.saveObjectImages(Collections.singletonList(request.getProfileImageReference()), ImageEntityType.PORTFOLIO_PROFILE.getEntityType(), portfolioDAO.getId());
            }

            String portfolioProfileImageUrl = null;
            String portfolioCoverImageUrl = null;

            if(request.getProfileImageReference() != null){
                portfolioProfileImageUrl = bucketService.getPortfolioImageShortLivedUrl(request.getProfileImageReference());
            }
            if(request.getCoverImageReference() != null){
                portfolioCoverImageUrl = bucketService.getPortfolioImageShortLivedUrl(request.getCoverImageReference());
            }

            String successfulMessage = "Portfolio updated successfully";
            String tailorName = tailorDAO.getName();
            String boutiqueName = boutique.getName();

            response = new GetPortfolioDetailsResponse(successfulMessage, tailorName, boutiqueName, portfolioDAO.getId(), updatedPortfolio.getSocialMedia(),updatedPortfolio.getAboutDetails(),updatedPortfolio.getUsername(), portfolioProfileImageUrl,portfolioCoverImageUrl);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        response.setMessage("Sorry! Portfolio doesn't exist!");
        return new ResponseEntity(response, HttpStatus.OK);
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
            String portfolioProfileReference = objectImagesService.getPortfiolioProfileReference(portfolioDAO.getId());
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
        Map<OutfitType, List<PortfolioOutfitsDAO>> portfolioOutfitMap = portfolioOutfitsDAOs
                .stream()
                .collect(
                        Collectors.groupingBy(PortfolioOutfitsDAO::getOutfitType)
                );
        List<OutfitTypeGroupedPortfolioDetails> outfitDetails = new ArrayList<>();
        for (Map.Entry<OutfitType, List<PortfolioOutfitsDAO>> entrySet : portfolioOutfitMap.entrySet()) {
            OutfitType outfitType = entrySet.getKey();
            List<PortfolioOutfitDetails> portfolioOutfitDetails = getPortfolioOutfitDetails(entrySet.getValue());
            OutfitTypeGroupedPortfolioDetails groupedPortfolioDetails = new OutfitTypeGroupedPortfolioDetails(
                    outfitType.getDisplayString(),
                    outfitType.getOrdinal(),
                    portfolioOutfitDetails);
            outfitDetails.add(groupedPortfolioDetails);
        }
        response.setOutfitTypeGroupedPortfolioDetails(outfitDetails);
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
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        PortfolioDAO portfolioDAO = mapper.portfolioToPortfolioDAO(portfolio.get(),
                new CycleAvoidingMappingContext());
        List<PortfolioOutfitsDAO> portfolioOutfits = portfolioDAO.getPortfolioOutfits();
        Map<OutfitType, OutfitFilter> outfitFilterMap = new HashMap<>();
        Map<Integer, String> colorFilterMap = new HashMap<>();
        for (PortfolioOutfitsDAO portfolioOutfitsDAO : portfolioOutfits) {
            OutfitType outfitType = portfolioOutfitsDAO.getOutfitType();
            OutfitFilter outfitFilter = null;
            if (outfitFilterMap.containsKey(outfitType)) {
                outfitFilter = outfitFilterMap.get(outfitType);
            } else {
                outfitFilter = new OutfitFilter(outfitType.getDisplayString(), outfitType.getOrdinal(), new HashMap<>());
            }
            Map<Integer, String> totalSuboutfits = outfitService.getSubOutfitMap(outfitType);
            String portfolioSubOutfitString = totalSuboutfits.get(portfolioOutfitsDAO.getSubOutfitType());
            Map<Integer, String> filteredSubOutfits = outfitFilter.getSubOutfits();
            filteredSubOutfits.put(portfolioOutfitsDAO.getSubOutfitType(), portfolioSubOutfitString);
            outfitFilter.setSubOutfits(filteredSubOutfits);
            outfitFilterMap.put(outfitType, outfitFilter);
            colorFilterMap.put(portfolioOutfitsDAO.getColor().getOrdinal(), portfolioOutfitsDAO.getColor().getName());
        }

        String successMessage = "Filters fetched successfully";
        List<OutfitFilter> outfitFilterList = new ArrayList<>(outfitFilterMap.values());
        response = new GetPortfolioFilterResponse(successMessage, outfitFilterList, colorFilterMap);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<PortfolioOutfitDetails> getPortfolioOutfitDetails(List<PortfolioOutfitsDAO> portfolioOutfitsDAOs) {
        List<PortfolioOutfitDetails> portfolioOutfitDetails = new ArrayList<>();
        for (PortfolioOutfitsDAO portfolioOutfit : portfolioOutfitsDAOs) {
            PortfolioOutfitDetails outfitDetail = new PortfolioOutfitDetails(portfolioOutfit);
            List<String> portfolioOutfitReferences =
                    objectImagesService.getPortfolioOutfitsReferenceIds(portfolioOutfit.getId());
            List<String> portfolioOutfitImageLinks = getPortfolioOutfitImageLinks(portfolioOutfitReferences);
            outfitDetail.setImageUrl(portfolioOutfitImageLinks);
            portfolioOutfitDetails.add(outfitDetail);
        }
        return portfolioOutfitDetails;
    }

    public ResponseEntity<GetPortfolioColorResponse> getColors() {
        Map<Integer, ColorEnum> colorEnumMap = ColorEnum.getColorOrdinalEnumMap();
        Map<Integer, String> responseMap = colorEnumMap.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> e.getValue().getName()));
        GetPortfolioColorResponse response = new GetPortfolioColorResponse(responseMap);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
