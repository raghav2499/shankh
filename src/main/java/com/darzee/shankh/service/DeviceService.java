package com.darzee.shankh.service;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.DeviceInfoDAO;
import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.DeviceInfo;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.DeviceInfoRepo;
import com.darzee.shankh.request.DeviceInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private DeviceInfoRepo repo;

    @Autowired
    private DaoEntityMapper mapper;

    public ResponseEntity saveDeviceInfo(DeviceInfoRequest request) {
        Long boutiqueId = request.getBoutiqueId();
        Optional<Boutique> optionalBoutique = boutiqueRepo.findById(boutiqueId);
        if (optionalBoutique.isPresent()) {
            BoutiqueDAO boutiqueDAO = mapper.boutiqueObjectToDao(optionalBoutique.get(),
                    new CycleAvoidingMappingContext());
            Optional<DeviceInfo> optionalDeviceInfo = repo.findByBoutiqueId(boutiqueId);
            DeviceInfoDAO deviceInfoDAO = new DeviceInfoDAO();
            if (optionalDeviceInfo.isPresent()) {
                deviceInfoDAO = mapper.deviceInfoToDAO(optionalDeviceInfo.get());
            }
            deviceInfoDAO.setBoutiqueId(boutiqueId);
            deviceInfoDAO.setTailorId(boutiqueDAO.getAdminTailor().getId());//todo get tailorId in request
            if (request.getDeviceToken() != null) {
                deviceInfoDAO.setDeviceToken(request.getDeviceToken());
            }
            if (request.getAppVersion() != null) {
                deviceInfoDAO.setAppVersion(request.getAppVersion());
            }
            repo.save(mapper.deviceInfoDAOToDeviceInfo(deviceInfoDAO));
            return new ResponseEntity("Details saved successfully", HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid boutique Id");
    }
}
