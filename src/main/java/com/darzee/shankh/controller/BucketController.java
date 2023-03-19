package com.darzee.shankh.controller;

import com.darzee.shankh.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/storage/")
public class BucketController {

    @Autowired
    private BucketService bucketService;

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) throws Exception {
        return bucketService.uploadPhoto(file);
    }

    @GetMapping("/")
    public List<byte[]> getFiles(@RequestBody List<String> fileUrls) throws Exception {
        return bucketService.getFile(fileUrls);
    }
}