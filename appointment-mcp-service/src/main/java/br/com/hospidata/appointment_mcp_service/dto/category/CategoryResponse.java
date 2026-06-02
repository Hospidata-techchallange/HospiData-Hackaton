package br.com.hospidata.appointment_mcp_service.dto.category;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String description,
        Boolean active
) {
}
