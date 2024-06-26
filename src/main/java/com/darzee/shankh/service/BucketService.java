package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.constants.ErrorMessages;
import com.darzee.shankh.dao.ImageReferenceDAO;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.enums.FileType;
import com.darzee.shankh.enums.Language;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.FileReferenceRepo;
import com.darzee.shankh.request.DownloadImageRequest;
import com.darzee.shankh.response.DownloadImageResponse;
import com.darzee.shankh.response.FileDetail;
import com.darzee.shankh.response.UploadMultipleFileResponse;
import com.darzee.shankh.service.translator.ErrorMessageTranslator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BucketService {

    @Autowired
    private AmazonClient client;

    @Autowired
    private FileReferenceRepo fileReferenceRepo;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    ErrorMessageTranslator errorMessageTranslator;

    @Value("invoice/")
    private String invoiceDirectory;

    @Value("items/")
    private String itemDetailsDirectory;


    public ResponseEntity<UploadMultipleFileResponse> uploadMultipleFiles(List<MultipartFile> files, String uploadFileType) {
        List<FileDetail> uploadImageResultList = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                Pair<String, String> fileUploadResult = uploadFile(file, uploadFileType);
                uploadImageResultList.add(new FileDetail(fileUploadResult.getKey(), fileUploadResult.getValue()));
            }
            UploadMultipleFileResponse response = new UploadMultipleFileResponse(uploadImageResultList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.FILE_UPLOAD_ERROR);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }

    public DownloadImageResponse getFileUrls(DownloadImageRequest request) {
        List<String> fileReferenceIds = request.getFileReferenceIds();
        List<ImageReferenceDAO> imageReferences = CommonUtils.mapList(
                fileReferenceRepo.findAllByReferenceIdIn(fileReferenceIds),
                mapper::imageReferenceToImageReferenceDAO);
        List<String> shortLivedUrls = imageReferences.stream()
                .map(imageReference -> client.generateShortLivedUrl(imageReference.getImageName(), false))
                .collect(Collectors.toList());
        return new DownloadImageResponse(shortLivedUrls);
    }

    public List<String> getShortLivedUrls(List<String> imageReferenceIds) {
        List<ImageReferenceDAO> imageReferences = CommonUtils.mapList(
                fileReferenceRepo.findAllByReferenceIdIn(imageReferenceIds),
                mapper::imageReferenceToImageReferenceDAO);
        List<String> shortLivedUrls = imageReferences.stream()
                .map(imageReference -> client.generateShortLivedUrl(imageReference.getImageName(), false))
                .collect(Collectors.toList());
        return shortLivedUrls;
    }

    public String getShortLivedUrl(String imageReferenceId) {
        Optional<ImageReference> imageReference = fileReferenceRepo.findByReferenceId(imageReferenceId);
        if (imageReference.isPresent()) {
            ImageReferenceDAO imageReferenceDAO = mapper.imageReferenceToImageReferenceDAO(imageReference.get());
            String shortLivedUrl = client.generateShortLivedUrl(imageReferenceDAO.getImageName(), false);
            return shortLivedUrl;
        }
        return null;
    }

    public String getPortfolioImageShortLivedUrl(String imageReferenceId) {
        Optional<ImageReference> imageReference = fileReferenceRepo.findByReferenceId(imageReferenceId);
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

    public String uploadItemDetailsPDF(File file, Long orderItemId) {
        String fileName = String.valueOf(orderItemId);
        ImmutablePair<String, String> fileUploadResult = client.uploadFile(file, itemDetailsDirectory + fileName);
        return fileUploadResult.getValue();
    }

    public String getInvoiceShortLivedLink(Long orderId, Long boutiqueId) {
        String fileLocation = invoiceDirectory + "bill" + orderId;
        return client.generateShortLivedUrl(fileLocation, false);
    }

    public Pair<String, String> uploadFile(MultipartFile multipartFile, String uploadFileTypeOrdinal) {
        File file = null;
        try {
            file = FileUtil.convertMultiPartToFile(multipartFile);
            String fileName = FileUtil.generateFileName(multipartFile);
            ImmutablePair<String, String> fileUploadResult = null;
            FileType fileType = FileType.getFileTypeFromOrdinal(uploadFileTypeOrdinal);
            switch (fileType) {
                case PORTFOLIO:
                    // for portfolio file
                    fileUploadResult = client.uploadPortfolioFile(file, fileName);
                    break;
                case AUDIO:
                    // for audio file
                    fileUploadResult = client.uploadAudioFile(file, fileName);
                    break;
                case MEASUREMENT:
                    //for measurement file
                    fileUploadResult = client.uploadMeasurementFile(file, fileName);
                    break;
                default:
                    // for image file
                    fileUploadResult = client.uploadFile(file, fileName);
                    break;
            }
            ImageReferenceDAO imageReference = new ImageReferenceDAO(fileUploadResult.getKey(), fileName);
            fileReferenceRepo.save(mapper.imageReferenceDAOToImageReference(imageReference));
            file.delete();
            return fileUploadResult;
        } catch (Exception e) {
            if (file != null) {
                file.delete();
            }
            String errorMessage = errorMessageTranslator.getTranslatedMessage(ErrorMessages.FILE_UPLOAD_ERROR_MSG)+e.getMessage();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }


}
