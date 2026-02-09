package br.com.hospidata.work_order_service.integration.stock.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BatchDTO {
    private UUID id;
    private UUID productId;
    private Integer quantityAvailable;
    private LocalDate expirationDate;
    private String aisle; // Corredor
    private String shelf; // Prateleira
    private String section;
}