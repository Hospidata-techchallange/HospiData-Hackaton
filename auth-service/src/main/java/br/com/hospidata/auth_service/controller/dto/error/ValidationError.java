package br.com.hospidata.auth_service.controller.dto.error;

import java.util.List;

public record ValidationError(
        List<String> erros,
        int status,
        String method,
        String path
) {
}
