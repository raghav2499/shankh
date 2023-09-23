package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.dao.ImageReferenceDAO;
import com.darzee.shankh.dao.SampleImageReferenceDAO;
import com.darzee.shankh.entity.ImageReference;
import com.darzee.shankh.entity.SampleImageReference;
import com.darzee.shankh.enums.BucketName;
import com.darzee.shankh.enums.SampleImageType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.ImageReferenceRepo;
import com.darzee.shankh.repo.SampleImageRefRepo;
import com.darzee.shankh.response.GetSampleImageResponse;
import com.darzee.shankh.response.SampleImageDetail;
import com.darzee.shankh.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SampleImageService {

    @Autowired
    private SampleImageRefRepo sampleImageRefRepo;

    @Autowired
    private ImageReferenceRepo imageReferenceRepo;

    @Autowired
    private DaoEntityMapper mapper;

    @Autowired
    private AmazonClient amazonClient;

    public ResponseEntity<GetSampleImageResponse> getSampleImages(String type) {
        SampleImageType sampleImageType = SampleImageType.sampleImageNameEnumMap.get(type);
        if (!SampleImageType.PORTFOLIO_COVER.equals(sampleImageType)) {
            throw new ResponseStatusException(HttpStatus.OK, "Image type not supported for sample image API");
        }
        GetSampleImageResponse imageResponse = new GetSampleImageResponse();
        List<SampleImageReference> sampleImageReferences =
                sampleImageRefRepo.findAllByBucketNameAndImageType(BucketName.PORTFOLIO,
                        SampleImageType.PORTFOLIO_COVER);
        List<SampleImageReferenceDAO> sampleImageReferenceDAOs = mapper.sampleImageRefToDAOList(sampleImageReferences);
        if (!CollectionUtils.isEmpty(sampleImageReferenceDAOs)) {
            List<String> imageReferenceIds = sampleImageReferenceDAOs
                    .stream()
                    .map(imageRef -> imageRef.getImageReferenceId())
                    .collect(Collectors.toList());
            List<ImageReference> imageReferences = imageReferenceRepo.findAllByReferenceIdIn(imageReferenceIds);
            List<ImageReferenceDAO> imageReferenceDAOs = CommonUtils.mapList(imageReferences,
                    mapper::imageReferenceToImageReferenceDAO);
            List<SampleImageDetail> imageDetails = new ArrayList<>(imageReferenceDAOs.size());
            for (ImageReferenceDAO referenceDAO : imageReferenceDAOs) {
                String imageUrl = amazonClient.generateShortLivedUrlForPortfolio(referenceDAO.getImageName());
                SampleImageDetail imageDetail = new SampleImageDetail(referenceDAO.getReferenceId(), imageUrl);
                imageDetails.add(imageDetail);
            }
            imageResponse.setImageDetails(imageDetails);
        }
        return new ResponseEntity<>(imageResponse, HttpStatus.OK);
    }
}
