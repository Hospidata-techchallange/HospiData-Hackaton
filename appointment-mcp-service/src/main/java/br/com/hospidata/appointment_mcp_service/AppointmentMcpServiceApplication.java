package br.com.hospidata.appointment_mcp_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AppointmentMcpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentMcpServiceApplication.class, args);
	}

}
