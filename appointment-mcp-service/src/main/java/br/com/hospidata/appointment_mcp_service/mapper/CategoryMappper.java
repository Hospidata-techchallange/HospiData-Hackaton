package br.com.hospidata.appointment_mcp_service.mapper;

import br.com.hospidata.appointment_mcp_service.dto.category.CategoryClientResponse;
import br.com.hospidata.appointment_mcp_service.dto.category.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMappper {

    public CategoryResponse toResponse(CategoryClientResponse category) {
        return new CategoryResponse(
                category.id(),
                category.name(),
                category.description(),
                category.active()
        );
    }

    public List<CategoryResponse> toResponses(List<CategoryClientResponse> categories) {
        return categories.stream()
                .map(this::toResponse)
                .toList();
    }

}
