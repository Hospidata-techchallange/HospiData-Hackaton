package br.com.hospidata.stock_service.controller;

import br.com.hospidata.stock_service.controller.dto.CategoryRequest;
import br.com.hospidata.stock_service.controller.dto.CategoryResponse;
import br.com.hospidata.stock_service.mapper.CategoryMapper;
import br.com.hospidata.stock_service.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok().body(service.findAllCategories(active));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<List<CategoryResponse>> createCategory(@RequestBody List<CategoryRequest> request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategories(request));
    }

    @PostMapping("/upload")
    public ResponseEntity<List<CategoryResponse>> uploadCategories(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.uploadCategories(file));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @RequestBody CategoryRequest categoryRequest,
            @PathVariable UUID id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateCategory(categoryRequest , id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable UUID id
    ) {
        service.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<Void> enableCategory(
            @PathVariable UUID id
    ) {
        service.enableCategoryById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
