package br.com.hospidata.stock_service.service;

import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import br.com.hospidata.stock_service.controller.dto.CategoryRequest;
import br.com.hospidata.stock_service.controller.dto.CategoryResponse;
import br.com.hospidata.stock_service.entity.Category;
import br.com.hospidata.stock_service.mapper.CategoryMapper;
import br.com.hospidata.stock_service.repository.CategoryRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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

    @Transactional
    public List<CategoryResponse> createCategories(List<CategoryRequest> categories) {

        List<Category> entities = mapper.toEntities(categories);
        var result = repository.saveAll(entities);
        return mapper.toResponses(result);
    }

    @Transactional
    public List<CategoryResponse> uploadCategories(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo CSV vazio");
        }

        try (
                Reader reader = new BufferedReader(
                        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
                )
        ) {

            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);

            List<Category> categories = mapper.fromCsv(parser);

            List<Category> saved = repository.saveAll(categories);

            return mapper.toResponses(saved);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o CSV", e);
        }
    }

    public List<CategoryResponse> findAllCategories() {
        return mapper.toResponses(repository.findAll());
    }

    public CategoryResponse findCategoryById(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString())));
    }
}
