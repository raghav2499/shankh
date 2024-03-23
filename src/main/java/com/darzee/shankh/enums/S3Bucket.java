package com.darzee.shankh.enums;

import java.util.HashMap;
import java.util.Map;

public enum S3Bucket {

    DARZEE_BACKEND_PROD("darzee.backend.prod", "ap-south-1"),
    DARZEE_BACKEND_STAGE("darzee.backend.stage", "us-east-1"),
    DARZEE_BACKEND_STATIC("darzee.backend.static", "us-east-1"),
    DARZEE_BACKEND_STATIC_STAGE("darzee.backend.static.stage", "us-east-1"),
    DARZEE_PORTFOLIO_PROD("darzee.portfolio.prod", "us-east-1"),
    DARZEE_PORTFOLIO_STAGE("darzee.portfolio.stage", "us-east-1"),
    DARZEE_BACKEND_AUDIO_STAGE("darzee.backend.audio.stage", "us-east-1");

    public static Map<String, String> bucketRegionMap = getBucketRegionMap();

    public static Map<String, String> getBucketRegionMap() {
        Map<String, String> bucketRegionMap = new HashMap<>();
        for (S3Bucket s3Bucket : S3Bucket.values()) {
            bucketRegionMap.put(s3Bucket.name, s3Bucket.region);
        }
        return bucketRegionMap;
    }
    private String name;
    private String region;

    S3Bucket(String name, String region) {
        this.name = name;
        this.region = region;
    }
}
