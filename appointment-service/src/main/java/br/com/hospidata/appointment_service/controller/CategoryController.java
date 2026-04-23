package br.com.hospidata.appointment_service.controller;

import br.com.hospidata.appointment_service.controller.dto.CategoryRequest;
import br.com.hospidata.appointment_service.controller.dto.CategoryResponse;
import br.com.hospidata.appointment_service.service.CategoryService;
import br.com.hospidata.common_security.aspect.CheckRole;
import br.com.hospidata.common_security.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointment/category")
public class CategoryController {

    private CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    @CheckRole({Role.ADMIN , Role.AGENT , Role.DOCTOR})
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok().body(service.findAllCategories(active));
    }

    @GetMapping("/{id}")
    @CheckRole({Role.ADMIN , Role.AGENT , Role.DOCTOR})
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
        return  ResponseEntity.ok().body(service.findCategoryById(id));
    }

    @PostMapping
    @CheckRole({Role.ADMIN})
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody CategoryRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(request));
    }

    @DeleteMapping("/{id}")
    @CheckRole({Role.ADMIN})
    public ResponseEntity<Void> deleteCategory(
            @PathVariable UUID id
    ) {
        service.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/enable/{id}")
    @CheckRole({Role.ADMIN})
    public ResponseEntity<Void> enableCategory(
            @PathVariable UUID id
    ) {
        service.enableCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @CheckRole({Role.ADMIN})
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id ,
            @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateCategory(id, request));
    }

}
