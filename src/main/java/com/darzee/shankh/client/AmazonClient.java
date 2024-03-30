package com.darzee.shankh.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.darzee.shankh.enums.S3Bucket;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${amazonProperties.s3.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.s3.bucketName}")
    private String privateBucketName;

    @Value("${amazonProperties.s3.portfolioBucketName}")
    private String portfolioBucketName;

    @Value("${amazonProperties.s3.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.s3.secretKey}")
    private String secretKey;

    @Value("${amazonProperties.s3.audioBucketName}")
    private String audioBucketName;

    @Value("measurement_revision/")
    private String measurementRevisionDirectory;

    @Value("${amazonProperties.s3.staticBucketName:defaultBucketName}")
    private String staticBucket;

    @Value("/Measurement")
    private static String measurementDirectory;

    @Value("/OutfitType/OutfitType")
    private static String outfitsDirectory;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    public ImmutablePair<String, String> uploadMeasurementFile(File file, String fileName) {
        s3client.putObject(new PutObjectRequest(privateBucketName, measurementRevisionDirectory + fileName, file));
        String referenceId = UUID.randomUUID().toString();
        String shortLivedUrl = generateShortLivedUrl(privateBucketName, measurementRevisionDirectory + fileName);
        return new ImmutablePair(referenceId, shortLivedUrl);
    }

    public ImmutablePair<String, String> uploadFile(File file, String fileName) {
        s3client.putObject(new PutObjectRequest(privateBucketName, fileName, file));
        String referenceId = UUID.randomUUID().toString();
        String shortLivedUrl = generateShortLivedUrl(privateBucketName, fileName);
        return new ImmutablePair(referenceId, shortLivedUrl);
    }

    public ImmutablePair<String, String> uploadPortfolioFile(File file, String fileName) {
        s3client.putObject(new PutObjectRequest(portfolioBucketName, fileName, file));
        String referenceId = UUID.randomUUID().toString();
        String shortLivedUrl = generateShortLivedUrl(portfolioBucketName, fileName);
        return new ImmutablePair(referenceId, shortLivedUrl);
    }

    //to upload audio to amazon s3 bucket
    public ImmutablePair<String, String> uploadAudioFile(File file, String fileName) {
        s3client.putObject(new PutObjectRequest(audioBucketName, fileName, file));
        String referenceId = UUID.randomUUID().toString();
        String shortLivedUrl = generateShortLivedUrl(audioBucketName, fileName);
        return new ImmutablePair(referenceId, shortLivedUrl);
    }

    public List<String> generateShortLivedUrlForPortfolio(List<String> fileNames) {
        List<String> urlList = new ArrayList<>(fileNames.size());
        for (String fileName : fileNames) {
            String url = generateShortLivedUrl(portfolioBucketName, fileName);
            urlList.add(url);
        }
        return urlList;
    }

    public String generateShortLivedUrlForMeasurementRevision(String fileName) {
        String shortLivedUrl = generateShortLivedUrl(privateBucketName, measurementRevisionDirectory + fileName);
        return shortLivedUrl;
    }

    public String generateShortLivedUrl(String fileName) {
        return generateShortLivedUrl(privateBucketName, fileName);
    }

    public String generateShortLivedUrlForAudio(String fileName) {
        return generateShortLivedUrl(audioBucketName, fileName);
    }

    public String generateShortLivedUrlForPortfolio(String fileName) {
        return generateShortLivedUrl(portfolioBucketName, fileName);
    }

    public String generateShortLivedUrlForMeasurement(String fileName) {
        return generateShortLivedUrl(staticBucket, measurementDirectory + fileName);
    }

    public String generateShortLivedUrlForOutfit(String fileName) {
        return generateShortLivedUrl(staticBucket, outfitsDirectory + fileName);
    }

    public List<String> generateShortLivedUrls(List<String> fileNames) {
        List<String> urlList = new ArrayList<>(fileNames.size());
        for (String fileName : fileNames) {
            String url = generateShortLivedUrl(privateBucketName, fileName);
            urlList.add(url);
        }
        return urlList;
    }

    private String generateShortLivedUrl(String bucketName, String fileName) {
        String region = S3Bucket.getBucketRegionMap().get(bucketName);
        String shortLivedUrl = "https://s3." + region + ".amazonaws.com/" + bucketName + "/" + fileName;
        return shortLivedUrl;
    }
}