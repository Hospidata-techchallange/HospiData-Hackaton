package br.com.hospidata.stock_mcp_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BatchDto(

        UUID idBatch,
        String batchNumber,
        Integer quantityAvailable,
        Integer initialQuantity,
        LocalDate manufacturingDate,
        LocalDate expirationDate,
        UUID productId,
        String productName,
        UUID locationId,
        String aisle,
        String bin,
        String shelf,
        String description,
        BigDecimal unitPrice,
        Boolean active

) {}