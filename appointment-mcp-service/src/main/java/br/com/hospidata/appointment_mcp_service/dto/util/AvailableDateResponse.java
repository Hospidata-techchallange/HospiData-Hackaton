package br.com.hospidata.appointment_mcp_service.dto.util;

import java.time.LocalDate;

public record AvailableDateResponse(
        LocalDate date,
        String dayOfWeek
) {}
