package br.com.hospidata.appointment_mcp_service.config;

import br.com.hospidata.appointment_mcp_service.tool.CategoryTool;
import br.com.hospidata.appointment_mcp_service.tool.DoctorScheduleTool;
import br.com.hospidata.appointment_mcp_service.tool.DoctorTool;
import br.com.hospidata.appointment_mcp_service.tool.TestTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {
    @Bean
    public ToolCallbackProvider toolCallbackProvider(
            TestTool testTool ,
            CategoryTool categoryTool ,
            DoctorTool doctorTool,
            DoctorScheduleTool doctorScheduleTool
    ) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(testTool , categoryTool , doctorTool , doctorScheduleTool)
                .build();
    }

}
