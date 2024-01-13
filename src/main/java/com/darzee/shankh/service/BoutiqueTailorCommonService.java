package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.enums.BoutiqueType;
import com.darzee.shankh.enums.FileEntityType;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.BoutiqueDetails;
import com.darzee.shankh.utils.jwtutils.TokenManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class BoutiqueTailorCommonService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private ObjectFilesService objectFilesService;

    @Autowired
    private TailorRepo tailorRepo;

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private TokenManager tokenManager;

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
    public String getTailorPortfolioLink(TailorDAO tailorDAO) {
        return portfolioService.getPortfolioLink(tailorDAO.getPortfolio());
    }

    public void saveTailorImageReference(String imageReference, Long tailorId) {
        objectFilesService.invalidateExistingReferenceIds(FileEntityType.TAILOR.getEntityType(), tailorId);
        objectFilesService.saveObjectFiles(Arrays.asList(imageReference),
                FileEntityType.TAILOR.getEntityType(),
                tailorId);
    }

    //todo : check if there's no boutique with same reference id
    private String generateUniqueBoutiqueReferenceId() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    private void saveBoutiqueReferences(List<String> imageReferences, BoutiqueDAO boutique) {
        objectFilesService.invalidateExistingReferenceIds(FileEntityType.BOUTIQUE.getEntityType(), boutique.getId());
        objectFilesService.saveObjectFiles(imageReferences,
                FileEntityType.BOUTIQUE.getEntityType(),
                boutique.getId());
    }


}
