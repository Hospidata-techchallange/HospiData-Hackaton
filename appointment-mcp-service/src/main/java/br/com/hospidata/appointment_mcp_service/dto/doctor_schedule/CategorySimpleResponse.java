package br.com.hospidata.appointment_mcp_service.dto.doctor_schedule;

import java.util.UUID;

public record CategorySimpleResponse(
        UUID id,
        String name
) {
}
