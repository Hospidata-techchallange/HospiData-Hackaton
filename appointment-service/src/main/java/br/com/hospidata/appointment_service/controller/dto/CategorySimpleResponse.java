package br.com.hospidata.appointment_service.controller.dto;

import java.util.UUID;

public record CategorySimpleResponse(
        UUID id,
        String name
) {
}
