package br.com.hospidata.appointment_mcp_service.service;

import br.com.hospidata.appointment_mcp_service.client.AuthClient;
import br.com.hospidata.appointment_mcp_service.dto.login.MeResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthClient authClient;
    private final AuthTokenService authTokenService;

    public AuthService(AuthClient authClient, AuthTokenService authTokenService) {
        this.authClient = authClient;
        this.authTokenService = authTokenService;
    }

    public synchronized String getToken() {
        return authTokenService.getToken();
    }

    public synchronized void clearToken() {
        authTokenService.clearToken();
    }

    public MeResponse me() {
        return authClient.me();
    }


}
