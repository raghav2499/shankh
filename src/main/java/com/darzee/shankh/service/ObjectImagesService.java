package com.darzee.shankh.service;

import com.darzee.shankh.dao.ObjectImagesDAO;
import com.darzee.shankh.entity.ObjectImages;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.ObjectImagesRepo;
import com.darzee.shankh.utils.CommonUtils;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObjectImagesService {

    @Autowired
    private ObjectImagesRepo repo;

    @Autowired
    private DaoEntityMapper mapper;

    @Nullable
    public String getCustomerImageReferenceId(Long customerId) {
        Optional<ObjectImages> optionalCustomerImage = repo.findByEntityIdAndEntityTypeAndIsValid(customerId,
                ImageEntityType.CUSTOMER.getEntityType(),
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
                ImageEntityType.TAILOR.getEntityType(),
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
                ImageEntityType.BOUTIQUE.getEntityType(),
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
                ImageEntityType.ORDER.getEntityType(),
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
                ImageEntityType.PORTFOLIO_COVER.getEntityType(),
                Boolean.TRUE);
        if(profileCoverImage.isPresent()) {
            ObjectImagesDAO objectImagesDAO = mapper.objectImagesToObjectImagesDAO(profileCoverImage.get());
            return objectImagesDAO.getReferenceId();
        }
        return null;
    }

    @Nullable
    public String getPortfiolioProfileReference(Long portfolioId) {
        Optional<ObjectImages> profileCoverImage = repo.findByEntityIdAndEntityTypeAndIsValid(portfolioId,
                ImageEntityType.PORTFOLIO_PROFILE.getEntityType(),
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
                ImageEntityType.PORTFOLIO_OUTFIT.getEntityType(),
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

    public void saveObjectImages(List<String> imageReferences, String entityType, Long entityId) {
        List<ObjectImagesDAO> objectImagesDAOList = imageReferences
                .stream()
                .filter(imageReference -> Boolean.FALSE.equals(StringUtils.isEmpty(imageReference)))
                .map(imageReferenceId -> new ObjectImagesDAO(imageReferenceId, entityType, entityId))
                .collect(Collectors.toList());
        repo.saveAll(CommonUtils.mapList(objectImagesDAOList, mapper::objectImageDAOToObjectImage));
    }
}
