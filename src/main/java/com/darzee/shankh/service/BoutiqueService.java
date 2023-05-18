package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.OrderRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.AddBoutiqueDetailsRequest;
import com.darzee.shankh.request.BoutiqueDetails;
import com.darzee.shankh.response.GetBoutiqueDetailsResponse;
import com.darzee.shankh.response.UpdateBoutiqueResponse;
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
import java.util.List;
import java.util.Optional;

@Service
public class BoutiqueService {

    @Autowired
    private ObjectImagesService objectImagesService;

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

    public BoutiqueDAO createNewBoutique(BoutiqueDetails boutiqueDetails) {
        String boutiqueReferenceId = generateUniqueBoutiqueReferenceId();
        BoutiqueDAO boutiqueDAO = new BoutiqueDAO(boutiqueDetails.getBoutiqueName(),
                boutiqueDetails.getBoutiqueType(),
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
    public ResponseEntity updateBoutiqueDetails(AddBoutiqueDetailsRequest request) {
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(request.getBoutiqueId());
        UpdateBoutiqueResponse response = new UpdateBoutiqueResponse();
        if (optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(boutiqueRepo.findById(request.getBoutiqueId()).get(),
                    new CycleAvoidingMappingContext());
            boutiqueDAO.setTailorCount(request.getTailorCount());
            boutiqueRepo.save(mapper.boutiqueDaoToObject(boutiqueDAO, new CycleAvoidingMappingContext()));
            response.setMessage("Boutique details updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setMessage("Boutique not found");
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity getBoutiqueDetails(Long boutiqueId) {
        Optional<Boutique> boutique = boutiqueRepo.findById(boutiqueId);
        if (boutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(boutique.get(), new CycleAvoidingMappingContext());
            TailorDAO tailorDAO = boutiqueDAO.getAdminTailor();
            List<String> shopImageReferenceIds = getBoutiqueImagesReferenceIds(boutiqueId);
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
                    shopImageUrls, adminTailorImageUrl);
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

}
