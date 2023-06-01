package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.enums.BoutiqueType;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.enums.Language;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.OrderRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.BoutiqueDetails;
import com.darzee.shankh.request.UpdateBoutiqueDetails;
import com.darzee.shankh.request.UpdateTailorRequest;
import com.darzee.shankh.response.GetBoutiqueDetailsResponse;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BoutiqueService {

    @Autowired
    private ObjectImagesService objectImagesService;

    @Autowired
    private BoutiqueLedgerService boutiqueLedgerService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private TailorRepo tailorRepo;

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;

    public BoutiqueDAO createNewBoutique(BoutiqueDetails boutiqueDetails) {
        String boutiqueReferenceId = generateUniqueBoutiqueReferenceId();
        BoutiqueType boutiqueType = BoutiqueType.getOrdinalEnumMap().get(boutiqueDetails.getBoutiqueType());
        BoutiqueDAO boutiqueDAO = new BoutiqueDAO(boutiqueDetails.getBoutiqueName(),
                boutiqueType.getName(),
                boutiqueReferenceId);
        boutiqueDAO = mapper.boutiqueObjectToDao(boutiqueRepo.save(mapper.boutiqueDaoToObject(boutiqueDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        if (!CollectionUtils.isEmpty(boutiqueDetails.getShopImageReferenceIds())) {
            saveBoutiqueReferences(boutiqueDetails.getShopImageReferenceIds(), boutiqueDAO);
        }
        return boutiqueDAO;
    }

    @Transactional
    public ResponseEntity updateBoutiqueDetails(Long boutiqueId, UpdateBoutiqueDetails request) {
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(boutiqueId);
        GetBoutiqueDetailsResponse response = null;
        if (optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(),
                    new CycleAvoidingMappingContext());
            TailorDAO adminTailor = boutiqueDAO.getAdminTailor();

            if (boutiqueDAO.isNameUpdated(request.getName())) {
                boutiqueDAO.setName(request.getName());
            }

            if (boutiqueDAO.isBoutiqueTypeUpdated(request.getBoutiqueType())) {
                BoutiqueType updatedBoutiqueType = BoutiqueType.getOrdinalEnumMap().get(request.getBoutiqueType());
                if (updatedBoutiqueType == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid boutique type");
                }
                boutiqueDAO.setBoutiqueType(updatedBoutiqueType);
            }

            if (boutiqueDAO.isTailorCountUpdated(request.getTailorCount())) {
                boutiqueDAO.setTailorCount(request.getTailorCount());
            }

            if(!Collections.isEmpty(request.getBoutiqueImageReferenceId())) {
                saveBoutiqueReferences(request.getBoutiqueImageReferenceId(), boutiqueDAO);
            }

            if (request.getTailor() != null) {
                adminTailor = updateAdminTailorProfile(adminTailor, request.getTailor());
            }

            boutiqueDAO = mapper.boutiqueObjectToDao(boutiqueRepo.save(mapper.boutiqueDaoToObject(boutiqueDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());

            response = generateBoutiqueDetailResponse(boutiqueDAO, adminTailor);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid boutique_id");
    }

    public ResponseEntity getBoutiqueDetails(Long boutiqueId) {
        Optional<Boutique> boutique = boutiqueRepo.findById(boutiqueId);
        if (boutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(boutique.get(), new CycleAvoidingMappingContext());
            TailorDAO tailorDAO = boutiqueDAO.getAdminTailor();

            GetBoutiqueDetailsResponse response = generateBoutiqueDetailResponse(boutiqueDAO, tailorDAO);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid boutique id");
    }

    //todo : check if there's no boutique with same reference id
    public String generateUniqueBoutiqueReferenceId() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    private List<String> getBoutiqueImagesReferenceIds(Long boutiqueId) {
        List<String> boutiqueImages = objectImagesService.getBoutiqueImageReferenceId(boutiqueId);
        if (Collections.isEmpty(boutiqueImages)) {
            return new ArrayList<>();
        }
        return boutiqueImages;
    }

    private void saveBoutiqueReferences(List<String> imageReferences, BoutiqueDAO boutique) {
        objectImagesService.invalidateExistingReferenceIds(ImageEntityType.BOUTIQUE.getEntityType(), boutique.getId());
        objectImagesService.saveObjectImages(imageReferences,
                ImageEntityType.BOUTIQUE.getEntityType(),
                boutique.getId());
    }

    private GetBoutiqueDetailsResponse generateBoutiqueDetailResponse(BoutiqueDAO boutiqueDAO, TailorDAO tailorDAO) {
        List<String> shopImageReferenceIds = getBoutiqueImagesReferenceIds(boutiqueDAO.getId());
        String adminTailorImageReferenceId = objectImagesService.getTailorImageReferenceId(boutiqueDAO.getAdminTailor().getId());

        List<String> shopImageUrls = new ArrayList<>();
        String adminTailorImageUrl = null;
        if (!Collections.isEmpty(shopImageReferenceIds)) {
            shopImageUrls = bucketService.getShortLivedUrls(shopImageReferenceIds);
        }
        if (adminTailorImageReferenceId != null) {
            adminTailorImageUrl = bucketService.getShortLivedUrl(adminTailorImageReferenceId);
        }
        GetBoutiqueDetailsResponse response = new GetBoutiqueDetailsResponse(boutiqueDAO, tailorDAO,
                shopImageReferenceIds, shopImageUrls, adminTailorImageReferenceId, adminTailorImageUrl);
        return response;
    }

    public TailorDAO updateAdminTailorProfile(TailorDAO tailorDAO, UpdateTailorRequest request) {
        if (tailorDAO.isNameUpdated(request.getTailorName())) {
            tailorDAO.setName(request.getTailorName());
        }
        if (tailorDAO.isPhoneNumberUpdated(request.getPhoneNumber())) {
            tailorDAO.setPhoneNumber(request.getPhoneNumber());
        }
        if(tailorDAO.isLanguageUpdated(request.getLanguage())) {
            Language updatedLanguage = Language.getOrdinalEnumMap().get(request.getLanguage());
            tailorDAO.setLanguage(updatedLanguage);
        }
        if(request.getTailorProfilePicReferenceId() != null) {
            saveTailorImageReference(request.getTailorProfilePicReferenceId(), tailorDAO.getId());
        }
        TailorDAO updatedTailor = mapper.tailorObjectToDao(tailorRepo.save(mapper.tailorDaoToObject(tailorDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

        return updatedTailor;
    }

    public void saveTailorImageReference(String imageReference, Long tailorId) {
        objectImagesService.invalidateExistingReferenceIds(ImageEntityType.TAILOR.getEntityType(), tailorId);
        objectImagesService.saveObjectImages(Arrays.asList(imageReference),
                ImageEntityType.TAILOR.getEntityType(),
                tailorId);
    }


}
