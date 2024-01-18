//package com.darzee.shankh.scripts.measurement_mig;
//
//import com.darzee.shankh.entity.MeasurementRevisions;
//import com.darzee.shankh.entity.Measurements;
//import com.darzee.shankh.repo.MeasurementRevisionsRepo;
//import com.darzee.shankh.repo.MeasurementsRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//public class MeasurementMigrationService {
//
//    @Autowired
//    private MeasurementsRepo measurementsRepository;
//
//    @Autowired
//    private MeasurementRevisionsRepo measurementRevisionsRepository;
//
//    @Transactional
//    public void migrateMeasurementsToRevisions() {
//        List<Measurements> measurementsList = measurementsRepository.findAll();
//
//        for (Measurements measurement : measurementsList) {
//            MeasurementRevisions revision = new MeasurementRevisions();
//
//            revision.setMeasurementValue(measurement.getMeasurementValue());
//            revision.setOutfitType(measurement.getOutfitType());
//            revision.setCustomerId(measurement.getCustomer().getId());
//            revision.setCreatedAt(measurement.getCreatedAt());
//
//            revision = measurementRevisionsRepository.save(revision);
//
//            measurement.setMeasurementRevision(revision);
//            measurementsRepository.save(measurement);
//        }
//    }
//}