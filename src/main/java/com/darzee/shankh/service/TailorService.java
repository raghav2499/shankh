package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.BoutiqueLedgerDAO;
import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.enums.TailorRole;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueLedgerRepo;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.TailorLoginRequest;
import com.darzee.shankh.request.TailorSignUpRequest;
import com.darzee.shankh.request.TailorSignUpRequest.BoutiqueDetails;
import com.darzee.shankh.response.TailorLoginResponse;
import com.darzee.shankh.utils.jwtutils.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
    private TokenManager tokenManager;

    public ResponseEntity tailorSignup(TailorSignUpRequest request) {
        boolean isAdminSignupRequest = request.getBoutiqueDetails().getBoutiqueReferenceId() == null ? true : false;
        BoutiqueDAO boutiqueDAO = null;
        if(isAdminSignupRequest) {
            BoutiqueDetails boutiqueDetails = request.getBoutiqueDetails();
            String boutiqueReferenceId = boutiqueService.generateUniqueBoutiqueReferenceId();
            boutiqueDAO = new BoutiqueDAO(boutiqueDetails.getBoutiqueName(),
                    boutiqueDetails.getBoutiqueType(),
                    boutiqueReferenceId);
            boutiqueDAO = mapper.boutiqueObjectToDao(boutiqueRepo.save(mapper.boutiqueDaoToObject(boutiqueDAO,
                            new CycleAvoidingMappingContext())),
                    new CycleAvoidingMappingContext());

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
                request.getPhoneNumber(),
                request.getProfilePicUrl(),
                boutiqueDAO);
        tailorDAO = mapper.tailorObjectToDao(tailorRepo.save(mapper.tailorDaoToObject(tailorDAO,
                new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());

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
     * @param request
     * @return
     */
    public ResponseEntity tailorLogin(TailorLoginRequest request) {
        String phoneNumber = request.getPhoneNumber();
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
}
