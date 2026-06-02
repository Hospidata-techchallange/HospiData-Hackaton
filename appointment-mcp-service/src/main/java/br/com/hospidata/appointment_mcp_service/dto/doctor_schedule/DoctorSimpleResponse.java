package br.com.hospidata.appointment_mcp_service.dto.doctor_schedule;

import java.util.List;
import java.util.UUID;

public record DoctorSimpleResponse(
        UUID id,
        String name,
        List<CategorySimpleResponse> categories
){
}
