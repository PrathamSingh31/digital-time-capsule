package com.capsule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // ✅ This enables scheduling tasks like @Scheduled
public class DigitalTimeCapsuleApplication {
	public static void main(String[] args) {
		SpringApplication.run(DigitalTimeCapsuleApplication.class, args);
	}
}
