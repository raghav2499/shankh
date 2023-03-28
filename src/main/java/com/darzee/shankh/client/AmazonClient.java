package com.darzee.shankh.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${amazonProperties.s3.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.s3.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.s3.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.s3.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    public String uploadFile(File file, String fileName) {
        String imagePath = bucketName + UUID.randomUUID().toString();
        String fileUrl = endpointUrl + "/" + imagePath + "/" + fileName;
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
        return fileUrl;
    }

    public byte[] downloadFile(String url) throws Exception {
        String [] fileNameArray = url.split("/");
        String fileName = fileNameArray[fileNameArray.length -1];
        String filePath = url.replace(fileName, "");
        S3Object object = s3client.getObject(filePath, fileName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw new Exception("Failed to download file "+ e);
        }
    }


}