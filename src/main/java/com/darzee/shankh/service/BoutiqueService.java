package com.darzee.shankh.service;

import com.amazonaws.services.dynamodbv2.xspec.B;
import com.darzee.shankh.constants.ErrorMessages;
import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.BoutiqueMeasurement;
import com.darzee.shankh.enums.*;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.*;
import com.darzee.shankh.request.UpdateBoutiqueDetails;
import com.darzee.shankh.request.UpdateTailorRequest;
import com.darzee.shankh.response.GetBoutiqueDetailsResponse;
import com.darzee.shankh.response.GetCustomInvoiceDetailResponse;
import com.darzee.shankh.service.translator.ErrorMessageTranslator;
import com.darzee.shankh.service.translator.SuccessMessageTranslator;

import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    private SuccessMessageTranslator successMessageTranslator;

    @Autowired
    private ErrorMessageTranslator        errorMessageTranslator;

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
                    String errorMessage =errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_BOUTIQUE_ID_ERROR);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
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
        String errorMessage =errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_BOUTIQUE_ID_ERROR); 
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    public ResponseEntity getBoutiqueDetails(Long boutiqueId) {
        Optional<Boutique> boutique = boutiqueRepo.findById(boutiqueId);
        if (boutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(boutique.get(), new CycleAvoidingMappingContext());
            TailorDAO tailorDAO = boutiqueDAO.getAdminTailor();

            GetBoutiqueDetailsResponse response = generateBoutiqueDetailResponse(boutiqueDAO, tailorDAO);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        String errorMessage =errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_BOUTIQUE_ID_ERROR); 
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
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
                    String errorMessage =errorMessageTranslator.getTranslatedMessage(ErrorMessages.MEASUREMENT_PARAM_NAME_MISSING);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
                }
                String paramName = (String) paramNameObject;
                measurementParams.add(paramName);
            }

            // get the outfit type from the ordinal
            OutfitType outfitTypeEnum = OutfitType.getOutfitOrdinalEnumMap().get(outfitType);

            // if the outfit type is null, return a bad request
            if (outfitTypeEnum == null) {
                String errorMessage =errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_OUTFIT_TYPE_ERROR);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
            }

            // get outfit side from the ordinal
            OutfitSide outfitSideEnum = OutfitSide.getEnumByOrdinal(outfitSide);

            if (outfitSideEnum == null) {
                String errorMessage =errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_OUTFIT_SIDE_ERROR);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
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
        String errorMessage =errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_BOUTIQUE_ID_ERROR); 
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);

    }

    public ResponseEntity getCustomInvoiceDetail(Long boutiqueId) {
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(boutiqueId);
        if (optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(),
                    new CycleAvoidingMappingContext());

            GetCustomInvoiceDetailResponse response = generateCustomInvoiceDetailResponse(boutiqueDAO); 

            return new ResponseEntity(response, HttpStatus.OK);
        }
        return null;
    }

    public GetCustomInvoiceDetailResponse generateCustomInvoiceDetailResponse(BoutiqueDAO boutiqueDAO) {
        GetCustomInvoiceDetailResponse response = new GetCustomInvoiceDetailResponse();
        response.setBoutiqueName(boutiqueDAO.getName());
        response.setAdminTailorPhoneNumber(boutiqueDAO.getAdminTailor().getPhoneNumber());
        if(boutiqueDAO.getAddress()!=null){
            response.setAddressLine1(boutiqueDAO.getAddress().getAddressLine1());
            response.setAddressLine2(boutiqueDAO.getAddress().getAddressLine2());
            response.setCity(boutiqueDAO.getAddress().getCity());
            response.setCountry(boutiqueDAO.getAddress().getCountry());
            response.setPostalCode(boutiqueDAO.getAddress().getPostalCode());
        }
        response.setAdminTailorProfilePicUrl(getAdminTailorProfilePictureUrl(boutiqueDAO));
        return response;
    }
    public String getAdminTailorProfilePictureUrl(BoutiqueDAO boutiqueDAO) {
        List<String> boutiqueProfilePictureRef= objectFilesService.getBoutiqueImageReferenceId(boutiqueDAO.getId());

       if(boutiqueProfilePictureRef!=null && !boutiqueProfilePictureRef.isEmpty()){
           return bucketService.getShortLivedUrl(boutiqueProfilePictureRef.get(0));
       }
       return null;
    }

    // public updateCustomInvoiceDetail()

}
