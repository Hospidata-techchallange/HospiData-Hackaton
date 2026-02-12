package br.com.hospidata.auth_service.controller.dto.error;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        //String error,
        String message,
        String path, String method
) {
}
