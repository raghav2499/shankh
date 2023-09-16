package com.darzee.shankh.repo;

import com.darzee.shankh.entity.SampleImageReference;
import com.darzee.shankh.enums.BucketName;
import com.darzee.shankh.enums.SampleImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SampleImageRefRepo extends JpaRepository<SampleImageReference, Long> {
    public List<SampleImageReference> findAllByBucketNameAndImageType(BucketName bucketName, SampleImageType sampleImageType);
}
