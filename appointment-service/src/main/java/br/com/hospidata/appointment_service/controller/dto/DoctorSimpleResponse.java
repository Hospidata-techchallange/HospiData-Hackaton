package br.com.hospidata.appointment_service.controller.dto;

import java.util.List;
import java.util.UUID;

public record DoctorSimpleResponse (
        UUID id,
        String name,
        List<CategorySimpleResponse> categories
){
}
