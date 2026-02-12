package br.com.hospidata.common.dto.error;

import java.time.Instant;

public record ErrorResponseInternal(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path, String method
) {
}
