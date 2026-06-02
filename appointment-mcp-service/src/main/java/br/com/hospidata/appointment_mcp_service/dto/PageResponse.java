package br.com.hospidata.appointment_mcp_service.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        boolean empty,
        boolean first,
        boolean last,
        int number,
        int numberOfElements,
        int size,
        long totalElements,
        int totalPages
) {}
