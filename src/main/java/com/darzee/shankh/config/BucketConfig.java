package com.darzee.shankh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component("bucketConfig")
public class BucketConfig {
    private static String staticBucket;

    @Value("${amazonProperties.s3.staticBucketName:defaultBucketName}")
    private void setStaticBucket(String staticBucket) {
        this.staticBucket = staticBucket;
    }

    @Value("/Outfits")
    private static String outfitsDirectory;

    public static String getStaticBucket() {
        return staticBucket;
    }

    public static String getOutfitsFolder() {
        return getStaticBucket() + outfitsDirectory;
    }
}
