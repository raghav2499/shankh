package com.darzee.shankh.service;

import com.darzee.shankh.dao.ObjectFilesDAO;
import com.darzee.shankh.entity.ObjectFiles;
import com.darzee.shankh.enums.FileEntityType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.ObjectFilesRepo;
import com.darzee.shankh.utils.CommonUtils;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObjectFilesService {

    @Autowired
    private ObjectFilesRepo repo;

    @Autowired
    private DaoEntityMapper mapper;

    @Nullable
    public String getCustomerImageReferenceId(Long customerId) {
        Optional<ObjectFiles> optionalCustomerImage = repo.findByEntityIdAndEntityTypeAndIsValid(customerId,
                FileEntityType.CUSTOMER.getEntityType(),
                Boolean.TRUE);
        if (optionalCustomerImage.isPresent()) {
            ObjectFilesDAO customerImage = mapper.objectImagesToObjectImagesDAO(optionalCustomerImage.get());
            return customerImage.getReferenceId();
        }
        return null;
    }

    @Nullable
    public String getTailorImageReferenceId(Long tailorId) {
        Optional<ObjectFiles> optionalTailorImage = repo.findByEntityIdAndEntityTypeAndIsValid(tailorId,
                FileEntityType.TAILOR.getEntityType(),
                Boolean.TRUE);
        if (optionalTailorImage.isPresent()) {
            ObjectFilesDAO tailorImage = mapper.objectImagesToObjectImagesDAO(optionalTailorImage.get());
            return tailorImage.getReferenceId();
        }
        return null;
    }

    @Nullable
    public List<String> getBoutiqueImageReferenceId(Long boutiqueId) {
        List<ObjectFiles> boutiqueImages = repo.findAllByEntityIdAndEntityTypeAndIsValid(boutiqueId,
                FileEntityType.BOUTIQUE.getEntityType(),
                Boolean.TRUE);
        if (!Collections.isEmpty(boutiqueImages)) {
            List<ObjectFilesDAO> boutiqueImagesDAO = CommonUtils.mapList(boutiqueImages, mapper::objectImagesToObjectImagesDAO);
            List<String> referenceIds = boutiqueImagesDAO.stream()
                    .map(boutiqueImage -> boutiqueImage.getReferenceId())
                    .collect(Collectors.toList());
            return referenceIds;
        }
        return null;
    }

    @Nullable
    public List<String> getClothReferenceIds(Long orderItemId) {
        List<ObjectFiles> clothImages = repo.findAllByEntityIdAndEntityTypeAndIsValid(orderItemId,
                FileEntityType.ORDER_ITEM.getEntityType(),
                Boolean.TRUE);
        if (!Collections.isEmpty(clothImages)) {
            List<ObjectFilesDAO> clothImagesDAO = CommonUtils.mapList(clothImages,
                    mapper::objectImagesToObjectImagesDAO);
            List<String> clothImagesReferenceIds = clothImagesDAO.stream()
                    .map(clothImage -> clothImage.getReferenceId())
                    .collect(Collectors.toList());
            return clothImagesReferenceIds;
        }
        return null;
    }

    @Nullable
    public List<String> getAudioReferenceIds(Long orderItemId) {
        List<ObjectFiles> audioFiles = repo.findAllByEntityIdAndEntityTypeAndIsValid(orderItemId,
                FileEntityType.AUDIO.getEntityType(),
                Boolean.TRUE);
        if (!Collections.isEmpty(audioFiles)) {
            List<ObjectFilesDAO> audioFilesDAO = CommonUtils.mapList(audioFiles,
                    mapper::objectImagesToObjectImagesDAO);
            List<String> audioFileReferenceIds = audioFilesDAO.stream()
                    .map(audio -> audio.getReferenceId())
                    .collect(Collectors.toList());
            return audioFileReferenceIds;
        }
        return null;
    }

    @Nullable
    public String getProfileCoverReference(Long portfolioId) {
        Optional<ObjectFiles> profileCoverImage = repo.findByEntityIdAndEntityTypeAndIsValid(portfolioId,
                FileEntityType.PORTFOLIO_COVER.getEntityType(),
                Boolean.TRUE);
        if(profileCoverImage.isPresent()) {
            ObjectFilesDAO objectImagesDAO = mapper.objectImagesToObjectImagesDAO(profileCoverImage.get());
            return objectImagesDAO.getReferenceId();
        }
        return null;
    }


    @Nullable
    public List<String> getPortfolioOutfitsReferenceIds(Long portfolioOutfitId) {
        List<ObjectFiles> portfolioOutfitImages = repo.findAllByEntityIdAndEntityTypeAndIsValid(portfolioOutfitId,
                FileEntityType.PORTFOLIO_OUTFIT.getEntityType(),
                Boolean.TRUE);
        if (!Collections.isEmpty(portfolioOutfitImages)) {
            List<ObjectFilesDAO> portfolioOutfitImagesDAO = CommonUtils.mapList(portfolioOutfitImages,
                    mapper::objectImagesToObjectImagesDAO);
            List<String> portfolioOutfitImageReferenceIds = portfolioOutfitImagesDAO.stream()
                    .map(portfolioOutfitImage -> portfolioOutfitImage.getReferenceId())
                    .collect(Collectors.toList());
            return portfolioOutfitImageReferenceIds;
        }
        return null;
    }

    @Nullable
    public String getMeasurementRevisionReferenceId(Long measurementRevisionId) {
        Optional<ObjectFiles> measurementRevImage = repo.findByEntityIdAndEntityTypeAndIsValid(measurementRevisionId,
                FileEntityType.MEASUREMENT_REVISION.getEntityType(),
                Boolean.TRUE);
        if (measurementRevImage.isPresent()) {
            ObjectFilesDAO measurementRevImageDAO = mapper.objectImagesToObjectImagesDAO(measurementRevImage.get());
            return measurementRevImageDAO.getReferenceId();
        }
        return null;
    }

    public void invalidateExistingReferenceIds(String entityType, Long entityId) {
        List<ObjectFiles> validImages = repo.findAllByEntityIdAndEntityTypeAndIsValid(entityId,
                entityType,
                Boolean.TRUE);
        if (!CollectionUtils.isEmpty(validImages)) {
            List<ObjectFilesDAO> objectImagesDAOs = CommonUtils.mapList(validImages,
                    mapper::objectImagesToObjectImagesDAO);
            objectImagesDAOs.forEach(objectImage -> objectImage.setIsValid(Boolean.FALSE));
            repo.saveAll(CommonUtils.mapList(objectImagesDAOs, mapper::objectImageDAOToObjectImage));
        }
    }

    public void invalidateExistingReferenceIds(List<String> invalidReferenceIds){
        for (String referenceId : invalidReferenceIds){
            Optional<ObjectFiles> validImage = repo.findByReferenceIdAndIsValid(referenceId,
                    Boolean.TRUE);
            if(validImage.isPresent()){
                ObjectFilesDAO objectImageDao = mapper.objectImagesToObjectImagesDAO(validImage.get());
                objectImageDao.setIsValid(Boolean.FALSE);
                repo.save(mapper.objectImageDAOToObjectImage(objectImageDao));
            }
        }
    }

    public void saveObjectImages(List<String> imageReferences, String entityType, Long entityId) {
        List<ObjectFilesDAO> objectImagesDAOList = imageReferences
                .stream()
                .filter(imageReference -> Boolean.FALSE.equals(StringUtils.isEmpty(imageReference)))
                .map(imageReferenceId -> new ObjectFilesDAO(imageReferenceId, entityType, entityId))
                .collect(Collectors.toList());
        repo.saveAll(CommonUtils.mapList(objectImagesDAOList, mapper::objectImageDAOToObjectImage));
    }

    public void saveObjectImages(Map<String, Long> refEntityIdMap, String entityType) {
        List<ObjectFilesDAO> objectImages = refEntityIdMap.entrySet().stream()
                .map(refEntityIdValue -> new ObjectFilesDAO(refEntityIdValue.getKey(),
                        entityType, refEntityIdValue.getValue()))
                .collect(Collectors.toList());
        repo.saveAll(CommonUtils.mapList(objectImages, mapper::objectImageDAOToObjectImage));
    }
}
