package br.com.hospidata.work_order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "br.com.hospidata.work_order_service",
                "br.com.hospidata.common"
        }
)
public class WorkOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkOrderServiceApplication.class, args);
	}

}
