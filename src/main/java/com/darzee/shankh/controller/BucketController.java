package com.darzee.shankh.controller;

import com.darzee.shankh.request.DownloadImageRequest;
import com.darzee.shankh.response.DownloadImageResponse;
import com.darzee.shankh.response.UploadImageResponse;
import com.darzee.shankh.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/storage")
public class BucketController {

    @Autowired
    private BucketService bucketService;

    @PostMapping("/uploadFile")
    public UploadImageResponse uploadFile(@RequestPart(value = "file") MultipartFile file,
                                          @RequestPart(value = "file_type", required = false) String uploadFileType)
            throws Exception {
        return bucketService.uploadPhoto(file, uploadFileType);
    }

    @GetMapping("/downloadFile")
    public DownloadImageResponse getFiles(@Valid @RequestBody DownloadImageRequest request) throws Exception {
        return bucketService.getFileUrls(request);
    }
}