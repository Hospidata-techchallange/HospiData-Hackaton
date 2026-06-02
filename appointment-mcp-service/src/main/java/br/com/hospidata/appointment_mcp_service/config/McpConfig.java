package br.com.hospidata.appointment_mcp_service.config;

import br.com.hospidata.appointment_mcp_service.tool.*;
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
            DoctorScheduleTool doctorScheduleTool,
            AppointmentTool appointmentTool,
            DateUtilsTool dateUtilsTool
    ) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(testTool ,
                        categoryTool ,
                        doctorTool ,
                        doctorScheduleTool ,
                        appointmentTool,
                        dateUtilsTool
                        )
                .build();
    }

}
