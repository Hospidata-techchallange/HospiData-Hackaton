package br.com.hospidata.appointment_service.mapper;

import br.com.hospidata.appointment_service.controller.dto.CategoryRequest;
import br.com.hospidata.appointment_service.controller.dto.CategoryResponse;
import br.com.hospidata.appointment_service.controller.dto.CategorySimpleResponse;
import br.com.hospidata.appointment_service.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getLastUpdatedAt()
        );
    }


    public List<CategoryResponse> toResponses(List<Category> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        return category;
    }

    public List<CategorySimpleResponse> toSimpleResponses(Set<Category> categories) {

        if (categories == null || categories.isEmpty()) {
            return List.of();
        }

        return categories.stream()
                .map(category -> new CategorySimpleResponse(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }
}
