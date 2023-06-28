package com.darzee.shankh.controller;

import com.darzee.shankh.request.DeviceInfoRequest;
import com.darzee.shankh.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveDeviceInformation(@Valid @RequestBody DeviceInfoRequest request) {
        return deviceService.saveDeviceInfo(request);
    }
}
