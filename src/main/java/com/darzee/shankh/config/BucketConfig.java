package com.darzee.shankh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BucketConfig {
    private static String staticBucket;

    @Value("${amazonProperties.s3.staticBucketName:defaultBucketName}")
    private void setStaticBucket(String staticBucket) {
        this.staticBucket = staticBucket;
    }

    public static String getStaticBucket() {
        return staticBucket;
    }
}
