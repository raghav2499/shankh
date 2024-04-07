package com.darzee.shankh;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.KafkaMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, KafkaAutoConfiguration.class,
		KafkaMetricsAutoConfiguration.class})
public class ShankhApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ShankhApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
