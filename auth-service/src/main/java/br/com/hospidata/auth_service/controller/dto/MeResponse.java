package br.com.hospidata.auth_service.controller.dto;

public record MeResponse (
        String userId ,
        String email ,
        String role
) {
}
