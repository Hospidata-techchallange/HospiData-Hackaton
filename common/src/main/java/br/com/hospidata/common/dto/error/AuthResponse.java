package br.com.hospidata.common.dto.error;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
