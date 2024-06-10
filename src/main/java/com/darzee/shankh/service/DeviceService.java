package com.darzee.shankh.service;

import com.darzee.shankh.constants.ErrorMessages;
import com.darzee.shankh.constants.SuccesssMessages;
import com.darzee.shankh.dao.DeviceInfoDAO;
import com.darzee.shankh.entity.DeviceInfo;
import com.darzee.shankh.entity.Tailor;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.DeviceInfoRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.DeviceInfoRequest;
import com.darzee.shankh.response.DeviceInfoResponse;
import com.darzee.shankh.service.translator.ErrorMessageTranslator;
import com.darzee.shankh.service.translator.SuccessMessageTranslator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private TailorRepo tailorRepo;

    @Autowired
    private DeviceInfoRepo repo;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private SuccessMessageTranslator successMessageTranslator;

    @Autowired
    private ErrorMessageTranslator errorMessageTranslator;

    public ResponseEntity saveDeviceInfo(DeviceInfoRequest request) {
        Long tailorId = request.getTailorId();
        Optional<Tailor> optionalTailor = tailorRepo.findById(tailorId);
        if (optionalTailor.isPresent()) {
            Optional<DeviceInfo> optionalDeviceInfo = repo.findByTailorId(tailorId);
            DeviceInfoDAO deviceInfoDAO = new DeviceInfoDAO();
            if (optionalDeviceInfo.isPresent()) {
                deviceInfoDAO = mapper.deviceInfoToDAO(optionalDeviceInfo.get());
            }
//            deviceInfoDAO.setBoutiqueId(boutiqueId);
            deviceInfoDAO.setTailorId(tailorId);
            if (request.getDeviceToken() != null) {
                deviceInfoDAO.setDeviceToken(request.getDeviceToken());
            }
            if (request.getAppVersion() != null) {
                deviceInfoDAO.setAppVersion(request.getAppVersion());
            }
            repo.save(mapper.deviceInfoDAOToDeviceInfo(deviceInfoDAO));
            String successMessage = successMessageTranslator.getTranslatedMessage(SuccesssMessages.CUSTOMER_DETAILS_SAVE_SUCCESS);
            DeviceInfoResponse response = new DeviceInfoResponse(successMessage);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.INVALID_TAILOR_ID );
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
