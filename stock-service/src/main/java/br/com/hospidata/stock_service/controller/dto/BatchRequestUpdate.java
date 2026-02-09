package br.com.hospidata.stock_service.controller.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BatchRequestUpdate(
        String batchNumber,
        UUID productId,
        UUID locationId,
        LocalDate expirationDate,
        LocalDate manufacturingDate,
        BigDecimal unitPrice
//        Integer initialQuantity
) {
}
