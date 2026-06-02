package br.com.hospidata.stock_mcp_service.config;

import br.com.hospidata.stock_mcp_service.tool.BatchTool;
import br.com.hospidata.stock_mcp_service.tool.CategoryTool;
import br.com.hospidata.stock_mcp_service.tool.ProductTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(CategoryTool categoryTool , ProductTool productTool , BatchTool batchTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(categoryTool , productTool, batchTool)
                .build();
    }
}