package com.darzee.shankh.service;

import com.darzee.shankh.dao.ObjectImagesDAO;
import com.darzee.shankh.entity.ObjectImages;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.ObjectImagesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.util.Optional;

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
        if(optionalCustomerImage.isPresent()){
            ObjectImagesDAO customerImage = mapper.objectImagesToObjectImagesDAO(optionalCustomerImage.get());
            return customerImage.getReferenceId();
        }
        return null;
    }
}
