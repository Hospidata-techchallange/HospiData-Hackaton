package br.com.hospidata.appointment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = {
				"br.com.hospidata.appointment_service",
				"br.com.hospidata.common",
				"br.com.hospidata.common_security"
		}
)
public class AppointmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentServiceApplication.class, args);
	}

}
