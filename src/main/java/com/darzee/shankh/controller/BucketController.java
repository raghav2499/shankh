package com.darzee.shankh.controller;

import com.darzee.shankh.request.DownloadImageRequest;
import com.darzee.shankh.response.DownloadImageResponse;
import com.darzee.shankh.response.GetSampleImageResponse;
import com.darzee.shankh.response.UploadMultipleFileResponse;
import com.darzee.shankh.service.BucketService;
import com.darzee.shankh.service.SampleImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/storage")
public class BucketController {

    @Autowired
    private BucketService bucketService;

    @Autowired
    private SampleImageService sampleImageService;

//    @PostMapping("/uploadFile")
//    @CrossOrigin
//    public UploadFileResponse uploadFile(@RequestPart(value = "file") MultipartFile file,
//                                          @RequestPart(value = "file_type", required = false) String uploadFileType)
//            throws Exception {
//        return bucketService.uploadSingleImage(file, uploadFileType);
//    }

    @PostMapping("/upload_multiple")
    @CrossOrigin
    public ResponseEntity<UploadMultipleFileResponse> uploadMultiple(@RequestPart(value = "files") List<MultipartFile> files,
                                                                     @RequestPart(value = "file_type", required = false) String uploadFileType)
            throws Exception {
        return bucketService.uploadMultipleImages(files, uploadFileType);
    }

    //for audio/images file upload endpoint
    @PostMapping("/upload_multiple_files")
    public ResponseEntity<UploadMultipleFileResponse> uploadMultipleFiles(@RequestPart(value = "file") List<MultipartFile> files,
                                          @RequestPart(value = "file_type", required = false) String uploadFileType)
            throws Exception {
        return bucketService.uploadMultipleFile(files, uploadFileType);
    }

    @GetMapping("/downloadFile")
    @CrossOrigin
    public DownloadImageResponse getFiles(@Valid @RequestBody DownloadImageRequest request) throws Exception {
        return bucketService.getFileUrls(request);
    }

    @GetMapping("/sample_images")
    @CrossOrigin
    public ResponseEntity<GetSampleImageResponse> getSampleCoverImages(@RequestParam(value = "type") String type) {
        return sampleImageService.getSampleImages(type);
    }
}