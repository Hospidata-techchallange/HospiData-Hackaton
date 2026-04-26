package br.com.hospidata.appointment_mcp_service.dto.category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryClientResponse(
        UUID id,
        String name,
        String description,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {
}
