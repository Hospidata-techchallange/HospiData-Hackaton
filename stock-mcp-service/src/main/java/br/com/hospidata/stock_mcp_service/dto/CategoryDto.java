package br.com.hospidata.stock_mcp_service.dto;

import java.util.UUID;

public record CategoryDto(
        UUID idCategory,
        String name,
        String description,
        Boolean active
) {
}
