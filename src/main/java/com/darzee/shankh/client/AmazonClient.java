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
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

    @Value("Measurement")
    private String measurementDirectory;

    @Value("OutfitType/OutfitType")
    private String outfitsDirectory;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    public ImmutablePair<String, String> uploadMeasurementFile(File file, String fileName) {
        s3client.putObject(new PutObjectRequest(privateBucketName, measurementRevisionDirectory + fileName, file));
        String referenceId = UUID.randomUUID().toString();
        String shortLivedUrl = generateShortLivedUrl(privateBucketName, measurementRevisionDirectory + fileName, false);
        return new ImmutablePair(referenceId, shortLivedUrl);
    }

    public ImmutablePair<String, String> uploadFile(File file, String fileName) {
        s3client.putObject(new PutObjectRequest(privateBucketName, fileName, file));
        String referenceId = UUID.randomUUID().toString();
        String shortLivedUrl = generateShortLivedUrl(privateBucketName, fileName, false);
        return new ImmutablePair(referenceId, shortLivedUrl);
    }

    public ImmutablePair<String, String> uploadPortfolioFile(File file, String fileName) {
        s3client.putObject(new PutObjectRequest(portfolioBucketName, fileName, file));
        String referenceId = UUID.randomUUID().toString();
        String shortLivedUrl = generateShortLivedUrl(portfolioBucketName, fileName, false);
        return new ImmutablePair(referenceId, shortLivedUrl);
    }

    //to upload audio to amazon s3 bucket
    public ImmutablePair<String, String> uploadAudioFile(File file, String fileName) {
        s3client.putObject(new PutObjectRequest(audioBucketName, fileName, file));
        String referenceId = UUID.randomUUID().toString();
        String shortLivedUrl = generateShortLivedUrl(audioBucketName, fileName, false);
        return new ImmutablePair(referenceId, shortLivedUrl);
    }

    public List<String> generateShortLivedUrlForPortfolio(List<String> fileNames) {
        List<String> urlList = new ArrayList<>(fileNames.size());
        for (String fileName : fileNames) {
            String url = generateShortLivedUrl(portfolioBucketName, fileName, true);
            urlList.add(url);
        }
        return urlList;
    }

    public String generateShortLivedUrlForMeasurementRevision(String fileName) {
        String shortLivedUrl = generateShortLivedUrl(privateBucketName, measurementRevisionDirectory + fileName, false);
        return shortLivedUrl;
    }

    public String generateShortLivedUrl(String fileName, Boolean tinyUrlReqd) {
        return generateShortLivedUrl(privateBucketName, fileName, tinyUrlReqd);
    }

    public String generateShortLivedUrlForAudio(String fileName) {
        return generateShortLivedUrl(audioBucketName, fileName, true);
    }

    public String generateShortLivedUrlForPortfolio(String fileName) {
        return generateShortLivedUrl(portfolioBucketName, fileName, true);
    }

    public String generateShortLivedUrlForMeasurement(String fileName) {
        return generateShortLivedUrl(staticBucket, measurementDirectory + "/" + fileName, false);
    }

    public String generateShortLivedUrlForOutfit(String fileName) {
        return generateShortLivedUrl(staticBucket, outfitsDirectory + "/" + fileName, false);
    }

    private String generateShortLivedUrl(String bucketName, String fileName, Boolean isTinyUrlRequired) {
        String region = S3Bucket.getBucketRegionMap().get(bucketName);
        String url = "https://s3." + region + ".amazonaws.com/" + bucketName + "/" + fileName;
        if(isTinyUrlRequired) {
            try {
                URL tinyUrlEndpoint = new URL("http://tinyurl.com/api-create.php?url=" + URLEncoder.encode(url, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) tinyUrlEndpoint.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                url = rd.readLine();
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate short URL", e);
            }
        }
        return url;
    }
}