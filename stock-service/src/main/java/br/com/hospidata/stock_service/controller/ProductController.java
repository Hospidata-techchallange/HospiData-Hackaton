package br.com.hospidata.stock_service.controller;

import br.com.hospidata.stock_service.controller.dto.ProductRequest;
import br.com.hospidata.stock_service.controller.dto.ProductResponse;
import br.com.hospidata.stock_service.entity.Product;
import br.com.hospidata.stock_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock/product")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<List<ProductResponse>> addProduct(@RequestBody List<ProductRequest> requests) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProducts(requests));
    }

    @PostMapping("/upload")
    public ResponseEntity<List<ProductResponse>> uploadProduct(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.uploadProducts(file));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllCategoris(active));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findProductById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(
            @PathVariable UUID id
    ) {
        service.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<Void> enableProductById(
            @PathVariable UUID id
    ) {
        service.enableProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(
            @RequestBody ProductRequest request,
            @PathVariable UUID id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateProduct(request , id));
    }

}
