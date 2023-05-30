package com.darzee.shankh.client;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

    @Value("${amazonProperties.s3.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.s3.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    public ImmutablePair<String, String> uploadFile(File file, String fileName) {
        s3client.putObject(new PutObjectRequest(privateBucketName, fileName, file));
        String referenceId = UUID.randomUUID().toString();
        String shortLivedUrl = generateShortLivedUrl(privateBucketName, fileName);
        return new ImmutablePair(referenceId, shortLivedUrl);
    }

    public String generateShortLivedUrl(String fileName) {
        return generateShortLivedUrl(privateBucketName, fileName);
    }

    public List<String> generateShortLivedUrls(List<String> fileNames) {
        List<String> urlList = new ArrayList<>(fileNames.size());
        for(String fileName : fileNames) {
            String url = generateShortLivedUrl(privateBucketName, fileName);
            urlList.add(url);
        }
        return urlList;
    }

    private String generateShortLivedUrl(String bucketName, String fileName) {
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60 * 15; // 15 hour
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        String shortLivedUrl = s3client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        return shortLivedUrl;
    }


}