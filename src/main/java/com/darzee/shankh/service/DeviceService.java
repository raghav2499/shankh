package com.darzee.shankh.service;

import com.darzee.shankh.dao.DeviceInfoDAO;
import com.darzee.shankh.entity.DeviceInfo;
import com.darzee.shankh.entity.Tailor;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.DeviceInfoRepo;
import com.darzee.shankh.repo.TailorRepo;
import com.darzee.shankh.request.DeviceInfoRequest;
import com.darzee.shankh.response.DeviceInfoResponse;
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
            DeviceInfoResponse response = new DeviceInfoResponse("Details saved successfully");
            return new ResponseEntity(response, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid tailor Id");
    }
}
