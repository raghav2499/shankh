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
                .map(clothImageReferenceId -> new ObjectImagesDAO(clothImageReferenceId, entityType, entityId))
                .collect(Collectors.toList());
        repo.saveAll(CommonUtils.mapList(objectImagesDAOList, mapper::objectImageDAOToObjectImage));
    }
}
