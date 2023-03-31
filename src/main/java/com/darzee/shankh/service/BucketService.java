package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.ImageReferenceDAO;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.ImageReferenceRepo;
import com.darzee.shankh.request.DownloadImageRequest;
import com.darzee.shankh.response.DownloadImageResponse;
import com.darzee.shankh.response.UploadImageResponse;
import com.darzee.shankh.utils.CommonUtils;
import com.darzee.shankh.utils.s3utils.FileUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BucketService {

    @Autowired
    private AmazonClient client;

    @Autowired
    private ImageReferenceRepo imageReferenceRepo;

    @Autowired
    private DaoEntityMapper mapper;

    public UploadImageResponse uploadPhoto(MultipartFile multipartFile) throws Exception {
        try {
            File file = FileUtil.convertMultiPartToFile(multipartFile);
            String fileName = FileUtil.generateFileName(multipartFile);
            ImmutablePair<String, String> fileUploadResult = client.uploadFile(file, fileName);
            ImageReferenceDAO imageReference = new ImageReferenceDAO(fileUploadResult.getKey(),
                    fileName);
            imageReferenceRepo.save(mapper.imageReferenceDAOToImageReference(imageReference));
            file.delete();
            return new UploadImageResponse(fileUploadResult.getKey(), fileUploadResult.getValue());
        } catch (Exception e) {
            throw new Exception("File upload failed with exception {}", e);
        }
    }

    public DownloadImageResponse getFileUrls(DownloadImageRequest request) {
        List<String> fileReferenceIds = request.getFileReferenceIds();
        List<ImageReferenceDAO> imageReferences = CommonUtils.mapList(
                imageReferenceRepo.findAllByReferenceIdIn(fileReferenceIds),
                mapper::imageReferenceToImageReferenceDAO);
        List<String> shortLivedUrls = imageReferences.stream()
                .map(imageReference -> client.downloadFile(imageReference.getImageName()))
                .collect(Collectors.toList());
        return new DownloadImageResponse(shortLivedUrls);

    }
}