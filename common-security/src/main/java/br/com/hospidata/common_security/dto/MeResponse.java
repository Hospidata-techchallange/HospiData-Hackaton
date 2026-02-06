package br.com.hospidata.common_security.dto;

public record MeResponse(
        String userId ,
        String email ,
        String role
) {
}
