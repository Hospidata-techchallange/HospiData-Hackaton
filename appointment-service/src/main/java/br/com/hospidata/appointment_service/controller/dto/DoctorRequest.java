package br.com.hospidata.appointment_service.controller.dto;

import java.time.LocalDate;
import java.util.List;

public record DoctorRequest(
        String name,
        String crm,
        String crmUf,
        String email,
        String phone,
        LocalDate birthDate,
        List<String> categoryIds
) {

}
