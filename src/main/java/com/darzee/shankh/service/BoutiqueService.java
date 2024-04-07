package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.BoutiqueMeasurementDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.BoutiqueMeasurement;
import com.darzee.shankh.enums.BoutiqueType;
import com.darzee.shankh.enums.FileEntityType;
import com.darzee.shankh.enums.Language;
import com.darzee.shankh.enums.OutfitSide;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.repo.BoutiqueMeasurementRepo;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.OrderRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.UpdateBoutiqueDetails;
import com.darzee.shankh.request.UpdateTailorRequest;
import com.darzee.shankh.response.GetBoutiqueDetailsResponse;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BoutiqueService {

    @Autowired
    private ObjectFilesService objectFilesService;

    @Autowired
    private BoutiqueLedgerService boutiqueLedgerService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private BoutiqueTailorCommonService boutiqueTailorService;

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private TailorRepo tailorRepo;

    @Autowired
    private BoutiqueMeasurementRepo boutiqueMeasurementRepo;

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;

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

            if (!Collections.isEmpty(request.getBoutiqueImageReferenceId())) {
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

    private List<String> getBoutiqueImagesReferenceIds(Long boutiqueId) {
        List<String> boutiqueImages = objectFilesService.getBoutiqueImageReferenceId(boutiqueId);
        if (Collections.isEmpty(boutiqueImages)) {
            return new ArrayList<>();
        }
        return boutiqueImages;
    }

    private void saveBoutiqueReferences(List<String> imageReferences, BoutiqueDAO boutique) {
        objectFilesService.invalidateExistingReferenceIds(FileEntityType.BOUTIQUE.getEntityType(), boutique.getId());
        objectFilesService.saveObjectFiles(imageReferences,
                FileEntityType.BOUTIQUE.getEntityType(),
                boutique.getId());
    }

    private GetBoutiqueDetailsResponse generateBoutiqueDetailResponse(BoutiqueDAO boutiqueDAO, TailorDAO tailorDAO) {
        List<String> shopImageReferenceIds = getBoutiqueImagesReferenceIds(boutiqueDAO.getId());
        String adminTailorImageReferenceId = objectFilesService
                .getTailorImageReferenceId(boutiqueDAO.getAdminTailor().getId());

        List<String> shopImageUrls = new ArrayList<>();
        String adminTailorImageUrl = null;
        if (!Collections.isEmpty(shopImageReferenceIds)) {
            shopImageUrls = bucketService.getShortLivedUrls(shopImageReferenceIds);
        }
        if (adminTailorImageReferenceId != null) {
            adminTailorImageUrl = bucketService.getShortLivedUrl(adminTailorImageReferenceId);
        }
        String portfolioLink = boutiqueTailorService.getTailorPortfolioLink(tailorDAO);
        GetBoutiqueDetailsResponse response = new GetBoutiqueDetailsResponse(boutiqueDAO, tailorDAO,
                shopImageReferenceIds, shopImageUrls, adminTailorImageReferenceId, adminTailorImageUrl,
                portfolioLink);
        return response;
    }

    public TailorDAO updateAdminTailorProfile(TailorDAO tailorDAO, UpdateTailorRequest request) {
        if (tailorDAO.isNameUpdated(request.getTailorName())) {
            tailorDAO.setName(request.getTailorName());
        }
        if (tailorDAO.isPhoneNumberUpdated(request.getPhoneNumber())) {
            tailorDAO.setPhoneNumber(request.getPhoneNumber());
        }
        if (tailorDAO.isLanguageUpdated(request.getLanguage())) {
            Language updatedLanguage = Language.getOrdinalEnumMap().get(request.getLanguage());
            tailorDAO.setLanguage(updatedLanguage);
        }
        if (request.getTailorProfilePicReferenceId() != null) {
            boutiqueTailorService.saveTailorImageReference(request.getTailorProfilePicReferenceId(), tailorDAO.getId());
        }
        TailorDAO updatedTailor = mapper.tailorObjectToDao(tailorRepo.save(mapper.tailorDaoToObject(tailorDAO,
                new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

        return updatedTailor;
    }

    public ResponseEntity createOrUpdateMeasurementParams(Long boutiqueId, Integer outfitType, Integer outfitSide,
            List<Map<String, Object>> measurementPramList) {

        // Validate boutiqueId and outfit type...
        Optional<Boutique> boutique = boutiqueRepo.findById(boutiqueId);
        if (boutique.isPresent()) {
            // creat a new list of measurement params
            List<String> measurementParams = new ArrayList<>();

            // iterate over the list of measurement params and add them to the new list
            for (Map<String, Object> measurementParam : measurementPramList) {
                Object paramNameObject = measurementParam.get("name");
                if (paramNameObject == null) {
                    return ResponseEntity.badRequest().body("Measurement param name is missing");
                }
                String paramName = (String) paramNameObject;
                measurementParams.add(paramName);
            }

            // get the outfit type from the ordinal
            OutfitType outfitTypeEnum = OutfitType.getOutfitOrdinalEnumMap().get(outfitType);

            // if the outfit type is null, return a bad request
            if (outfitTypeEnum == null) {
                return ResponseEntity.badRequest().body("Invalid outfit type");
            }

            // get outfit side from the ordinal
            OutfitSide outfitSideEnum = OutfitSide.getEnumByOrdinal(outfitSide);

            if (outfitSideEnum == null) {
                return ResponseEntity.badRequest().body("Invalid outfit side");
            }

            // create a new BoutiqueMeasurement with the boutiqueId, outfitType and
            // outfitSide

            BoutiqueMeasurement boutiqueMeasurement = boutiqueMeasurementRepo
                    .findUniqueByBoutiqueIdOutfitTypeOutfitSide(boutiqueId, outfitTypeEnum, outfitSideEnum);

            if (boutiqueMeasurement == null) {
                boutiqueMeasurement = new BoutiqueMeasurement();
                boutiqueMeasurement.setBoutiqueId(boutiqueId);
                boutiqueMeasurement.setOutfitType(outfitTypeEnum);
                boutiqueMeasurement.setOutfitSide(outfitSideEnum);
                boutiqueMeasurement.setCreatedAt(LocalDateTime.now());
            }

            boutiqueMeasurement.setParam(measurementParams);

            boutiqueMeasurementRepo.save(boutiqueMeasurement);

           

            return new ResponseEntity<>(boutiqueMeasurement, HttpStatus.OK);

        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid boutique id");

    }

}
