package br.com.hospidata.appointment_mcp_service.dto.login;

public record MeResponse(
        String userId ,
        String email ,
        String role
) {
}
