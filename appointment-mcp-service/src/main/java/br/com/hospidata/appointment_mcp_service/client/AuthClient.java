package br.com.hospidata.appointment_mcp_service.client;

import br.com.hospidata.appointment_mcp_service.config.FeignAuthConfig;
import br.com.hospidata.appointment_mcp_service.dto.login.MeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service", contextId = "authProtectedClient", configuration = FeignAuthConfig.class)
public interface AuthClient {

    @GetMapping("/api/v1/auth/me")
    MeResponse me();

}
