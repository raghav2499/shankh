package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.ImageReferenceDAO;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.enums.UploadFileType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.ImageReferenceRepo;
import com.darzee.shankh.request.DownloadImageRequest;
import com.darzee.shankh.response.DownloadImageResponse;
import com.darzee.shankh.response.UploadImageResponse;
import com.darzee.shankh.response.UploadMultipleImageResponse;
import com.darzee.shankh.utils.CommonUtils;
import com.darzee.shankh.utils.s3utils.FileUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BucketService {

    @Autowired
    private AmazonClient client;

    @Autowired
    private ImageReferenceRepo imageReferenceRepo;

    @Autowired
    private DaoEntityMapper mapper;

    @Value("invoice/")
    private String invoiceDirectory;

    public UploadImageResponse uploadSingleImage(MultipartFile multipartFile, String uploadFileTypeOrdinal) {
        try {
            Pair<String, String> fileUploadResult = uploadPhoto(multipartFile, uploadFileTypeOrdinal);
            return new UploadImageResponse(fileUploadResult.getKey(), fileUploadResult.getValue());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed with exception {}", e);
        }
    }

    public ResponseEntity<UploadMultipleImageResponse> uploadMultipleImages(List<MultipartFile> files, String uploadFileType) {
        List<UploadImageResponse> uploadImageResultList = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                Pair<String, String> fileUploadResult = uploadPhoto(file, uploadFileType);
                uploadImageResultList.add(new UploadImageResponse(fileUploadResult.getKey(), fileUploadResult.getValue()));
            }
            UploadMultipleImageResponse response = new UploadMultipleImageResponse(uploadImageResultList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed with exception {}", e);
        }

    }

    public DownloadImageResponse getFileUrls(DownloadImageRequest request) {
        List<String> fileReferenceIds = request.getFileReferenceIds();
        List<ImageReferenceDAO> imageReferences = CommonUtils.mapList(
                imageReferenceRepo.findAllByReferenceIdIn(fileReferenceIds),
                mapper::imageReferenceToImageReferenceDAO);
        List<String> shortLivedUrls = imageReferences.stream()
                .map(imageReference -> client.generateShortLivedUrl(imageReference.getImageName()))
                .collect(Collectors.toList());
        return new DownloadImageResponse(shortLivedUrls);
    }

    public List<String> getShortLivedUrls(List<String> imageReferenceIds) {
        List<ImageReferenceDAO> imageReferences = CommonUtils.mapList(
                imageReferenceRepo.findAllByReferenceIdIn(imageReferenceIds),
                mapper::imageReferenceToImageReferenceDAO);
        List<String> shortLivedUrls = imageReferences.stream()
                .map(imageReference -> client.generateShortLivedUrl(imageReference.getImageName()))
                .collect(Collectors.toList());
        return shortLivedUrls;
    }

    public String getShortLivedUrl(String imageReferenceId) {
        Optional<ImageReference> imageReference = imageReferenceRepo.findByReferenceId(imageReferenceId);
        if (imageReference.isPresent()) {
            ImageReferenceDAO imageReferenceDAO = mapper.imageReferenceToImageReferenceDAO(imageReference.get());
            String shortLivedUrl = client.generateShortLivedUrl(imageReferenceDAO.getImageName());
            return shortLivedUrl;
        }
        return null;
    }

    public String getPortfolioImageShortLivedUrl(String imageReferenceId) {
        Optional<ImageReference> imageReference = imageReferenceRepo.findByReferenceId(imageReferenceId);
        if (imageReference.isPresent()) {
            ImageReferenceDAO imageReferenceDAO = mapper.imageReferenceToImageReferenceDAO(imageReference.get());
            String shortLivedUrl = client.generateShortLivedUrlForPortfolio(imageReferenceDAO.getImageName());
            return shortLivedUrl;
        }
        return null;
    }

    public String uploadInvoice(File file, Long orderId) {
        String fileName = "bill" + orderId;
        ImmutablePair<String, String> fileUploadResult = client.uploadFile(file, invoiceDirectory + fileName);
        return fileUploadResult.getValue();
    }

    public String getInvoiceShortLivedLink(Long orderId) {
        String fileLocation = invoiceDirectory + "bill" + orderId;
        return client.generateShortLivedUrl(fileLocation);
    }

    private Pair<String, String> uploadPhoto(MultipartFile multipartFile, String uploadFileTypeOrdinal) throws IOException {
        File file = FileUtil.convertMultiPartToFile(multipartFile);
        String fileName = FileUtil.generateFileName(multipartFile);
        ImmutablePair<String, String> fileUploadResult = null;
        UploadFileType uploadFileType = UploadFileType.uploadFileOrdinalEnumMap.get(CommonUtils.stringToInt(uploadFileTypeOrdinal));
        if (UploadFileType.PORTFOLIO.equals(uploadFileType)) {
            fileUploadResult = client.uploadPortfolioFile(file, fileName);
        } else {
            fileUploadResult = client.uploadFile(file, fileName);
        }
        ImageReferenceDAO imageReference = new ImageReferenceDAO(fileUploadResult.getKey(),
                fileName);
        imageReferenceRepo.save(mapper.imageReferenceDAOToImageReference(imageReference));
        file.delete();
        return fileUploadResult;
    }
}
