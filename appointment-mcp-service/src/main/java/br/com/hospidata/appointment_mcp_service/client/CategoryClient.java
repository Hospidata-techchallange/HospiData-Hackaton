package br.com.hospidata.appointment_mcp_service.client;


import br.com.hospidata.appointment_mcp_service.config.FeignAuthConfig;
import br.com.hospidata.appointment_mcp_service.dto.category.CategoryClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "appointment-service",
        contextId = "categoryClient",
        configuration = FeignAuthConfig.class
)
public interface CategoryClient {

    @GetMapping("/api/v1/appointment/category")
    List<CategoryClientResponse> getAllCategories();
}
