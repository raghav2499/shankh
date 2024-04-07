package com.darzee.shankh.controller;

import com.darzee.shankh.request.DownloadImageRequest;
import com.darzee.shankh.request.GetFileLinkRequest;
import com.darzee.shankh.response.DownloadImageResponse;
import com.darzee.shankh.response.GetSampleImageResponse;
import com.darzee.shankh.response.UploadMultipleFileResponse;
import com.darzee.shankh.service.BucketService;
import com.darzee.shankh.service.FileLinkService;
import com.darzee.shankh.service.SampleImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    private FileLinkService fileLinkService;

    @Autowired
    private SampleImageService sampleImageService;

    //for audio/images file upload endpoint
    @PostMapping("/upload_multiple_files")
    public ResponseEntity<UploadMultipleFileResponse> uploadMultipleFiles(@RequestPart(value = "file") List<MultipartFile> files,
                                                                          @RequestParam(value = "file_type", required = false) String uploadFileType)
            throws Exception {
        return bucketService.uploadMultipleFiles(files, uploadFileType);
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

    @PostMapping(value = "/file/link", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getFileLink(@Valid @RequestBody GetFileLinkRequest request) {
        return fileLinkService.getFileLinkResponse(request);
    }
}