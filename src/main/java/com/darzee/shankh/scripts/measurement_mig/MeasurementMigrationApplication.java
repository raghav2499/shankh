//package com.darzee.shankh.scripts.measurement_mig;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//
//@SpringBootApplication
//@ComponentScan(basePackages = "com.darzee.shankh")
//public class MeasurementMigrationApplication implements CommandLineRunner {
//
//    @Autowired
//    private MeasurementMigrationService measurementMigrationService;
//
//    public static void main(String[] args) {
//        SpringApplication.run(MeasurementMigrationApplication.class, args);
//    }
//
//
//    @Override
//    public void run(String... args) throws Exception {
//        measurementMigrationService.migrateMeasurementsToRevisions();
//    }
//}
