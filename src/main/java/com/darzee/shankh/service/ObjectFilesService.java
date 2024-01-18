package com.darzee.shankh.service;

import com.darzee.shankh.dao.ObjectImagesDAO;
import com.darzee.shankh.entity.ObjectImages;
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
        Optional<ObjectImages> optionalCustomerImage = repo.findByEntityIdAndEntityTypeAndIsValid(customerId,
                FileEntityType.CUSTOMER.getEntityType(),
                Boolean.TRUE);
        if (optionalCustomerImage.isPresent()) {
            ObjectImagesDAO customerImage = mapper.objectImagesToObjectImagesDAO(optionalCustomerImage.get());
            return customerImage.getReferenceId();
        }
        return null;
    }

    @Nullable
    public String getTailorImageReferenceId(Long tailorId) {
        Optional<ObjectImages> optionalTailorImage = repo.findByEntityIdAndEntityTypeAndIsValid(tailorId,
                FileEntityType.TAILOR.getEntityType(),
                Boolean.TRUE);
        if (optionalTailorImage.isPresent()) {
            ObjectImagesDAO tailorImage = mapper.objectImagesToObjectImagesDAO(optionalTailorImage.get());
            return tailorImage.getReferenceId();
        }
        return null;
    }

    @Nullable
    public List<String> getBoutiqueImageReferenceId(Long boutiqueId) {
        List<ObjectImages> boutiqueImages = repo.findAllByEntityIdAndEntityTypeAndIsValid(boutiqueId,
                FileEntityType.BOUTIQUE.getEntityType(),
                Boolean.TRUE);
        if (!Collections.isEmpty(boutiqueImages)) {
            List<ObjectImagesDAO> boutiqueImagesDAO = CommonUtils.mapList(boutiqueImages, mapper::objectImagesToObjectImagesDAO);
            List<String> referenceIds = boutiqueImagesDAO.stream()
                    .map(boutiqueImage -> boutiqueImage.getReferenceId())
                    .collect(Collectors.toList());
            return referenceIds;
        }
        return null;
    }

    @Nullable
    public List<String> getClothReferenceIds(Long orderId) {
        List<ObjectImages> clothImages = repo.findAllByEntityIdAndEntityTypeAndIsValid(orderId,
                FileEntityType.ORDER_ITEM.getEntityType(),
                Boolean.TRUE);
        if (!Collections.isEmpty(clothImages)) {
            List<ObjectImagesDAO> clothImagesDAO = CommonUtils.mapList(clothImages,
                    mapper::objectImagesToObjectImagesDAO);
            List<String> clothImagesReferenceIds = clothImagesDAO.stream()
                    .map(clothImage -> clothImage.getReferenceId())
                    .collect(Collectors.toList());
            return clothImagesReferenceIds;
        }
        return null;
    }

    @Nullable
    public String getProfileCoverReference(Long portfolioId) {
        Optional<ObjectImages> profileCoverImage = repo.findByEntityIdAndEntityTypeAndIsValid(portfolioId,
                FileEntityType.PORTFOLIO_COVER.getEntityType(),
                Boolean.TRUE);
        if(profileCoverImage.isPresent()) {
            ObjectImagesDAO objectImagesDAO = mapper.objectImagesToObjectImagesDAO(profileCoverImage.get());
            return objectImagesDAO.getReferenceId();
        }
        return null;
    }


    @Nullable
    public List<String> getPortfolioOutfitsReferenceIds(Long portfolioOutfitId) {
        List<ObjectImages> portfolioOutfitImages = repo.findAllByEntityIdAndEntityTypeAndIsValid(portfolioOutfitId,
                FileEntityType.PORTFOLIO_OUTFIT.getEntityType(),
                Boolean.TRUE);
        if (!Collections.isEmpty(portfolioOutfitImages)) {
            List<ObjectImagesDAO> portfolioOutfitImagesDAO = CommonUtils.mapList(portfolioOutfitImages,
                    mapper::objectImagesToObjectImagesDAO);
            List<String> portfolioOutfitImageReferenceIds = portfolioOutfitImagesDAO.stream()
                    .map(portfolioOutfitImage -> portfolioOutfitImage.getReferenceId())
                    .collect(Collectors.toList());
            return portfolioOutfitImageReferenceIds;
        }
        return null;
    }

    public void invalidateExistingReferenceIds(String entityType, Long entityId) {
        List<ObjectImages> validImages = repo.findAllByEntityIdAndEntityTypeAndIsValid(entityId,
                entityType,
                Boolean.TRUE);
        if (!CollectionUtils.isEmpty(validImages)) {
            List<ObjectImagesDAO> objectImagesDAOs = CommonUtils.mapList(validImages,
                    mapper::objectImagesToObjectImagesDAO);
            objectImagesDAOs.forEach(objectImage -> objectImage.setIsValid(Boolean.FALSE));
            repo.saveAll(CommonUtils.mapList(objectImagesDAOs, mapper::objectImageDAOToObjectImage));
        }
    }

    public void invalidateExistingReferenceIds(List<String> invalidReferenceIds){
        for (String referenceId : invalidReferenceIds){
            Optional<ObjectImages> validImage = repo.findByReferenceIdAndIsValid(referenceId,
                    Boolean.TRUE);
            if(validImage.isPresent()){
                ObjectImagesDAO objectImageDao = mapper.objectImagesToObjectImagesDAO(validImage.get());
                objectImageDao.setIsValid(Boolean.FALSE);
                repo.save(mapper.objectImageDAOToObjectImage(objectImageDao));
            }
        }
    }

    public void saveObjectImages(List<String> imageReferences, String entityType, Long entityId) {
        List<ObjectImagesDAO> objectImagesDAOList = imageReferences
                .stream()
                .filter(imageReference -> Boolean.FALSE.equals(StringUtils.isEmpty(imageReference)))
                .map(imageReferenceId -> new ObjectImagesDAO(imageReferenceId, entityType, entityId))
                .collect(Collectors.toList());
        repo.saveAll(CommonUtils.mapList(objectImagesDAOList, mapper::objectImageDAOToObjectImage));
    }

    public void saveObjectImages(Map<String, Long> refEntityIdMap, String entityType) {
        List<ObjectImagesDAO> objectImages = refEntityIdMap.entrySet().stream()
                .map(refEntityIdValue -> new ObjectImagesDAO(refEntityIdValue.getKey(),
                        entityType, refEntityIdValue.getValue()))
                .collect(Collectors.toList());
        repo.saveAll(CommonUtils.mapList(objectImages, mapper::objectImageDAOToObjectImage));
    }
}
