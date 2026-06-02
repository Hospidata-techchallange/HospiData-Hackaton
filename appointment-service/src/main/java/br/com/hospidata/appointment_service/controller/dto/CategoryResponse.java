package br.com.hospidata.appointment_service.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String description,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {
}
