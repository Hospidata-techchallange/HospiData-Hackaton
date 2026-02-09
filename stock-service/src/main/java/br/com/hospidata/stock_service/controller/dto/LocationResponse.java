package br.com.hospidata.stock_service.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LocationResponse(

        UUID id,
        String aisle,
        String shelf,
        String bin,
        String description,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt

) {
}
