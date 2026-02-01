package br.com.hospidata.stock_service.mapper;

import br.com.hospidata.stock_service.controller.dto.CategoryRequest;
import br.com.hospidata.stock_service.controller.dto.CategoryResponse;
import br.com.hospidata.stock_service.entity.Category;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest dto ) {
        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());
        return category;
    }

    public List<Category> toEntities(List<CategoryRequest> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public CategoryResponse toResponse(Category entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getLastUpdatedAt(),
                entity.getActive()
                );
    }

    public List<CategoryResponse> toResponses(List<Category> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Category fromCsv(CSVRecord record) {
        Category category = new Category();
        category.setName(record.get("name"));
        category.setDescription(record.get("description"));
        return category;
    }

    public List<Category> fromCsv(CSVParser parser) {
        return StreamSupport.stream(parser.spliterator(), false)
                .map(this::fromCsv)
                .toList();
    }

}
