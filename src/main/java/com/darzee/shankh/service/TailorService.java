package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.BoutiqueLedgerDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.entity.Tailor;
import com.darzee.shankh.enums.ImageEntityType;
import com.darzee.shankh.enums.TailorRole;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.CustomerRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.BoutiqueDetails;
import com.darzee.shankh.request.ProfileUpdateRequest;
import com.darzee.shankh.request.TailorLoginRequest;
import com.darzee.shankh.request.TailorSignUpRequest;
import com.darzee.shankh.response.TailorLoginResponse;
import com.darzee.shankh.utils.CommonUtils;
import com.darzee.shankh.utils.jwtutils.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@Service
public class TailorService {

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private TailorRepo tailorRepo;

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private BoutiqueLedgerRepo boutiqueLedgerRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BoutiqueService boutiqueService;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private ObjectImagesService objectImagesService;

    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private CustomerRepo customerRepo;

    @Transactional
    public ResponseEntity tailorSignup(TailorSignUpRequest request) {
        boolean isAdminSignupRequest = request.getBoutiqueDetails().getBoutiqueReferenceId() == null ? true : false;
        BoutiqueDAO boutiqueDAO = null;
        String phoneNumber = CommonUtils.sanitisePhoneNumber(request.getPhoneNumber());
        if (isTailorAlreadySignedUp(phoneNumber)) {
            TailorLoginResponse tailorLoginResponse = new TailorLoginResponse("User already exists. Please login");
            return new ResponseEntity(tailorLoginResponse, HttpStatus.OK);
        }
        if (isAdminSignupRequest) {
            BoutiqueDetails boutiqueDetails = request.getBoutiqueDetails();
            boutiqueDAO = boutiqueService.createNewBoutique(boutiqueDetails);
            BoutiqueLedgerDAO boutiqueLedgerDAO = new BoutiqueLedgerDAO(boutiqueDAO.getId());
            boutiqueLedgerRepo.save(mapper.boutiqueLedgerDAOToObject(boutiqueLedgerDAO, new CycleAvoidingMappingContext()));
        } else {
            boutiqueDAO = mapper.boutiqueObjectToDao(boutiqueRepo
                            .findByBoutiqueReferenceId(request.getBoutiqueDetails().getBoutiqueReferenceId()),
                    new CycleAvoidingMappingContext());
        }

        TailorRole role = isAdminSignupRequest ? TailorRole.ADMIN : TailorRole.EMPLOYEE;
        TailorDAO tailorDAO = new TailorDAO(request.getTailorName(),
                role,
                request.getLanguage(),
                phoneNumber,
                boutiqueDAO);
        tailorDAO = mapper.tailorObjectToDao(tailorRepo.save(mapper.tailorDaoToObject(tailorDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

        if (request.getProfilePicReferenceId() != null) {
            saveTailorReference(request.getProfilePicReferenceId(), tailorDAO.getId());
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(tailorDAO.getPhoneNumber());
        String loginToken = tokenManager.generateJwtToken(userDetails);

        TailorLoginResponse response = new TailorLoginResponse(loginToken, tailorDAO.getId(),
                tailorDAO.getName(),
                "",
                tailorDAO.getBoutique().getId(),
                "Tailor signup is successful");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    /**
     * Validate tailor and generate token
     *
     * @param request
     * @return
     */
    public ResponseEntity tailorLogin(TailorLoginRequest request) {
        String phoneNumber = CommonUtils.sanitisePhoneNumber(request.getPhoneNumber());
        try {
            //todo : check how and where it checks to authenticate the request
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber,
                    ""));
        } catch (AuthenticationException e) {
            TailorLoginResponse response = new TailorLoginResponse("No user found");
            return new ResponseEntity(response, HttpStatus.OK);
        }
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(phoneNumber);

        String loginToken = tokenManager.generateJwtToken(userDetails);
        TailorDAO tailorDAO = mapper.tailorObjectToDao(tailorRepo.findByPhoneNumber(userDetails.getUsername()).get(),
                new CycleAvoidingMappingContext());

        TailorLoginResponse response = mapper.tailorDAOToLoginResponse(tailorDAO, loginToken);
        response.setMessage("Tailor logged in successfully");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public TailorDAO updateProfile(Long tailorId, ProfileUpdateRequest request) {
        Optional<Tailor> optionalTailor = tailorRepo.findById(tailorId);
        if (!optionalTailor.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin tailor_id for this boutique is invalid");
        }
        TailorDAO tailorDAO = mapper.tailorObjectToDao(optionalTailor.get(), new CycleAvoidingMappingContext());
        if (tailorDAO.isNameUpdated(request.getTailorName())) {
            tailorDAO.setName(request.getTailorName());
        }
        if (tailorDAO.isPhoneNumberUpdated(request.getPhoneNumber())) {
            tailorDAO.setPhoneNumber(request.getPhoneNumber());
        }
        TailorDAO updatedTailor = mapper.tailorObjectToDao(tailorRepo.save(mapper.tailorDaoToObject(tailorDAO,
                        new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

        return updatedTailor;
    }

    public ResponseEntity deleteProfile(String phoneNumber) {
        Optional<Tailor> tailor = tailorRepo.findByPhoneNumber(phoneNumber);
        if (tailor.isPresent()) {
            tailorRepo.delete(tailor.get());
            return new ResponseEntity("success", HttpStatus.OK);
        }
        return new ResponseEntity("No profile found", HttpStatus.OK);
    }

    private Boolean isTailorAlreadySignedUp(String phoneNumber) {
        Optional<Tailor> tailor = tailorRepo.findByPhoneNumber(phoneNumber);
        if (tailor.isPresent()) {
            return true;
        }
        return false;
    }

    private void saveTailorReference(String imageReference, Long tailorId) {
        objectImagesService.invalidateExistingReferenceIds(ImageEntityType.TAILOR.getEntityType(), tailorId);
        objectImagesService.saveObjectImages(Arrays.asList(imageReference),
                ImageEntityType.TAILOR.getEntityType(),
                tailorId);
    }
}
