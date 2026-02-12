package br.com.hospidata.stock_service.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record BatchResponse(
        UUID id,
        String batchNumber,

        UUID productId,
        String productName,

        UUID locationId,
        String locationDescription,

        LocalDate expirationDate,
        LocalDate manufacturingDate,

        BigDecimal unitPrice,
        Integer quantityAvailable,
        Integer initialQuantity,

        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt,

        Boolean active
) {
}