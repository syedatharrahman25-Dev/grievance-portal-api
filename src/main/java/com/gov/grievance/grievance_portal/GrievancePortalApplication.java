package com.gov.grievance.grievance_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class GrievancePortalApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				GrievancePortalApplication.class, args);
	}
}
