package br.com.hospidata.appointment_mcp_service.tool;


import br.com.hospidata.appointment_mcp_service.dto.login.MeResponse;
import br.com.hospidata.appointment_mcp_service.service.AuthService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class TestTool {

    private final AuthService authService;

    public TestTool(AuthService authService) {
        this.authService = authService;
    }

    @Tool(
            name = "testTool",
            description = "Tool de Teste"
    )
    public MeResponse testTool() {
        return authService.me();
    }

}
