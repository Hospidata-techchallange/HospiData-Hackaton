package br.com.hospidata.appointment_service.service;

import br.com.hospidata.appointment_service.controller.dto.CategoryRequest;
import br.com.hospidata.appointment_service.controller.dto.CategoryResponse;
import br.com.hospidata.appointment_service.entity.Category;
import br.com.hospidata.appointment_service.mapper.CategoryMapper;
import br.com.hospidata.appointment_service.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.hospidata.common.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllCategories(Boolean active) {
        if (active == null) {
            return mapper.toResponses(repository.findAll());
        }
        return mapper.toResponses(repository.findByActive(active));
    }

    @Transactional(readOnly = true)
    public CategoryResponse findCategoryById(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id.toString())));
    }


    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category entity = mapper.toEntity(request);
        var result = repository.save(entity);
        return mapper.toResponse(result);
    }

    @Transactional
    public void deleteCategory(UUID id) {
        Category find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id.toString()));
        repository.delete(find);
    }

    @Transactional
    public void enableCategory(UUID id) {
        Category find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id.toString()));
        find.setActive(true);
        repository.save(find);
    }

    @Transactional
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id.toString()));

        category.setName(request.name());
        category.setDescription(request.description());

        return mapper.toResponse(repository.save(category));

    }

}
