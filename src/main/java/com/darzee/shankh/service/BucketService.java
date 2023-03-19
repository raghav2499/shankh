package com.darzee.shankh.service;

import com.darzee.shankh.client.AmazonClient;
import com.darzee.shankh.utils.s3utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class BucketService {

    @Autowired
    private AmazonClient client;

    public String uploadPhoto(MultipartFile multipartFile) throws Exception {
        String fileUrl = "";
        try {
            File file = FileUtil.convertMultiPartToFile(multipartFile);
            String fileName = FileUtil.generateFileName(multipartFile);
            fileUrl = client.uploadFile(file, fileName);
            file.delete();
            return fileUrl;
        } catch (Exception e) {
            throw new Exception("File upload failed with exception {}", e);
        }
    }

    public List<byte[]> getFile(List<String> urls) throws Exception {
        List<byte[]> images = new ArrayList<>(urls.size());
        for(String url : urls) {
            byte[] image = client.downloadFile(url);
            images.add(image);
        }
        return images;
    }
}
