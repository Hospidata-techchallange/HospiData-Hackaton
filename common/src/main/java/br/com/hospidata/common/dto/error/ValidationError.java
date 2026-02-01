package br.com.hospidata.common.dto.error;

import java.util.List;

public record ValidationError(
        List<String> erros,
        int status,
        String method,
        String path
) {
}
