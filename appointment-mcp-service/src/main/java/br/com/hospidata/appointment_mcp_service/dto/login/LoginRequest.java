package br.com.hospidata.appointment_mcp_service.dto.login;

public record LoginRequest (
        String email ,
        String password
) {
}
