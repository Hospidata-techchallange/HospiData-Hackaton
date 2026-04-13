package br.com.hospidata.appointment_service.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DoctorResponse(
        UUID id,
        String name,
        String crm,
        String crmUf,
        String email,
        String phone,
        LocalDate birthDate,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt,
        List<CategoryResponse> categories
) {

}
