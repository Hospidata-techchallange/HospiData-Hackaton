package br.com.hospidata.stock_service.controller.dto;

import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,

        String name,

        String description,

        LocalDateTime createdAt,

        LocalDateTime lastUpdatedAt,

        Boolean active
) {
}
