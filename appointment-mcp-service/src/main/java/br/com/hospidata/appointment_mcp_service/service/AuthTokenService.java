package br.com.hospidata.appointment_mcp_service.service;

import br.com.hospidata.appointment_mcp_service.client.AuthLoginClient;
import br.com.hospidata.appointment_mcp_service.config.AuthClientProperties;
import br.com.hospidata.appointment_mcp_service.dto.login.LoginRequest;
import feign.Response;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

@Service
public class AuthTokenService {

    private static final String ACCESS_TOKEN_COOKIE = "accessToken";
    private static final long EXPIRATION_SAFETY_WINDOW_SECONDS = 5;

    private final AuthLoginClient authLoginClient;
    private final AuthClientProperties properties;

    private String token;
    private Instant expiresAt;

    public AuthTokenService(AuthLoginClient authLoginClient, AuthClientProperties properties) {
        this.authLoginClient = authLoginClient;
        this.properties = properties;
    }

    public synchronized String getToken() {
        if (hasValidToken()) {
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
        this.expiresAt = null;
    }

    private String extractToken(Response response) {
        Collection<String> cookies = getHeaderValuesIgnoreCase(response.headers(), "Set-Cookie");

        if (cookies == null) {
            throw new RuntimeException("No Set-Cookie header found");
        }

        return cookies.stream()
                .map(this::parseCookieAttributes)
                .filter(attributes -> ACCESS_TOKEN_COOKIE.equals(attributes.get("name")))
                .findFirst()
                .map(attributes -> {
                    this.expiresAt = resolveExpiration(attributes);
                    return attributes.get("value");
                })
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }

    private boolean hasValidToken() {
        if (token == null || token.isBlank()) {
            return false;
        }

        return expiresAt == null || Instant.now().isBefore(expiresAt.minusSeconds(EXPIRATION_SAFETY_WINDOW_SECONDS));
    }

    private Collection<String> getHeaderValuesIgnoreCase(Map<String, Collection<String>> headers, String headerName) {
        return headers.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getKey().equalsIgnoreCase(headerName))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private Map<String, String> parseCookieAttributes(String rawCookie) {
        String[] parts = rawCookie.split(";");
        String[] nameAndValue = parts[0].split("=", 2);

        if (nameAndValue.length != 2) {
            throw new RuntimeException("Invalid cookie format");
        }

        Map<String, String> attributes = new java.util.HashMap<>();
        attributes.put("name", nameAndValue[0].trim());
        attributes.put("value", nameAndValue[1].trim());

        for (int i = 1; i < parts.length; i++) {
            String[] attribute = parts[i].trim().split("=", 2);
            String key = attribute[0].trim().toLowerCase(Locale.ROOT);
            String value = attribute.length > 1 ? attribute[1].trim() : "";
            attributes.put(key, value);
        }

        return attributes;
    }

    private Instant resolveExpiration(Map<String, String> attributes) {
        String maxAgeValue = attributes.get("max-age");
        if (maxAgeValue != null && !maxAgeValue.isBlank()) {
            try {
                long maxAgeSeconds = Long.parseLong(maxAgeValue);
                return Instant.now().plus(maxAgeSeconds, ChronoUnit.SECONDS);
            } catch (NumberFormatException ignored) {
                // Falls back to Expires or response-driven invalidation.
            }
        }

        String expiresValue = attributes.get("expires");
        if (expiresValue != null && !expiresValue.isBlank()) {
            try {
                return DateTimeFormatter.RFC_1123_DATE_TIME.parse(expiresValue, Instant::from);
            } catch (Exception ignored) {
                // Falls back to response-driven invalidation.
            }
        }

        return null;
    }
}
