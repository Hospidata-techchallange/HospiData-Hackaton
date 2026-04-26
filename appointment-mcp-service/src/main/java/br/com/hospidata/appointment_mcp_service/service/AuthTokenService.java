package br.com.hospidata.appointment_mcp_service.service;

import br.com.hospidata.appointment_mcp_service.client.AuthLoginClient;
import br.com.hospidata.appointment_mcp_service.config.AuthClientProperties;
import br.com.hospidata.appointment_mcp_service.dto.login.LoginRequest;
import feign.Response;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuthTokenService {

    private final AuthLoginClient authLoginClient;
    private final AuthClientProperties properties;

    private String token;

    public AuthTokenService(AuthLoginClient authLoginClient, AuthClientProperties properties) {
        this.authLoginClient = authLoginClient;
        this.properties = properties;
    }

    public synchronized String getToken() {
        if (token != null && !token.isBlank()) {
            return token;
        }

        Response response = authLoginClient.login(
                new LoginRequest(properties.getEmail(), properties.getPassword())
        );

        if (response.status() < 200 || response.status() >= 300) {
            throw new RuntimeException("Login failed with status " + response.status());
        }

        this.token = extractToken(response);
        return this.token;
    }

    public synchronized void clearToken() {
        this.token = null;
    }

    private String extractToken(Response response) {
        Collection<String> cookies = response.headers().get("Set-Cookie");

        if (cookies == null) {
            throw new RuntimeException("No Set-Cookie header found");
        }

        return cookies.stream()
                .filter(cookie -> cookie.startsWith("accessToken"))
                .findFirst()
                .map(cookie -> cookie.split(";")[0].split("=")[1])
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }
}
