package br.com.hospidata.appointment_mcp_service.tool;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class TestTool {


    @Tool(
            name = "testTool",
            description = "Tool de Teste"
    )
    public String testTool() {
        return "Testando............";
    }



}
