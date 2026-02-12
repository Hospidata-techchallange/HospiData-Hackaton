package br.com.hospidata.auth_service.controller.dto.error;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
