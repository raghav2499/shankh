package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.ImageReferenceDAO;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.FileReferenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeasurementRevisionService {

    @Autowired
    private ObjectFilesService objectFilesService;

    @Autowired
    private FileReferenceRepo fileReferenceRepo;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private AmazonClient s3Client;

    public String getMeasurementRevisionImageLink(Long revisionId) {
        String referenceId = objectFilesService.getMeasurementRevisionReferenceId(revisionId);
        return getMeasurementRevisionImageLink(referenceId);
    }

    public String getMeasurementRevisionImageLink(String measurementRevisionReferenceId) {
        Optional<ImageReference> measurementRevisionRef = fileReferenceRepo.findByReferenceId(measurementRevisionReferenceId);
        if (measurementRevisionRef.isPresent()) {
            ImageReferenceDAO imageReferenceDAO = mapper.imageReferenceToImageReferenceDAO(measurementRevisionRef.get());
            String measurementRevRefFileName = imageReferenceDAO.getImageName();
            return s3Client.generateShortLivedUrlForMeasurementRevision(measurementRevRefFileName);
        }
        return null;
    }
}
