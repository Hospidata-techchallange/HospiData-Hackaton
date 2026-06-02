package br.com.hospidata.stock_mcp_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("br.com.hospidata.stock_mcp_service.mapper")
public class StockMcpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMcpServiceApplication.class, args);
	}

}
