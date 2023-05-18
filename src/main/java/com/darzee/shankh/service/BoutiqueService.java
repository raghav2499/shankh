package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.OrderRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.AddBoutiqueDetailsRequest;
import com.darzee.shankh.response.GetBoutiqueDetailsResponse;
import com.darzee.shankh.response.UpdateBoutiqueResponse;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private TailorRepo tailorRepo;

    @Autowired
    private OrderRepo orderRepo;

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
            GetBoutiqueDetailsResponse response = new GetBoutiqueDetailsResponse(boutiqueDAO, tailorDAO,
                    shopImageReferenceIds);
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

}
