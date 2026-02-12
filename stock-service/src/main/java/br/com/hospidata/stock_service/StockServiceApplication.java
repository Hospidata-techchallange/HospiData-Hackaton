package br.com.hospidata.stock_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "br.com.hospidata.stock_service",
                "br.com.hospidata.common",
                "br.com.hospidata.common_security"
        }
)
public class StockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockServiceApplication.class, args);
    }

}
