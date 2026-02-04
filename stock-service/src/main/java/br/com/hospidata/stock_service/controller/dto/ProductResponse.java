package br.com.hospidata.stock_service.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse (

        UUID id,
        String name,
        String description,
        String skuCode,

        UUID categoryId,
        String categoryName,

        Integer minStockAlert,

        Boolean active,

        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt

) {

}
