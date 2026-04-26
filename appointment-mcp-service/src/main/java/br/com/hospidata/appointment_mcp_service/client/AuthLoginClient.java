package br.com.hospidata.appointment_mcp_service.client;

import br.com.hospidata.appointment_mcp_service.dto.login.LoginRequest;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", contextId = "authLoginClient")
public interface AuthLoginClient {

    @PostMapping("/api/v1/auth/login")
    Response login(@RequestBody LoginRequest request);
}
