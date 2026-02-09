package br.com.hospidata.stock_service.service;

import br.com.hospidata.common.exceptions.DuplicateKeyException;
import br.com.hospidata.common.exceptions.ListCannotEmpty;
import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import br.com.hospidata.stock_service.controller.dto.ProductRequest;
import br.com.hospidata.stock_service.controller.dto.ProductResponse;
import br.com.hospidata.stock_service.entity.Category;
import br.com.hospidata.stock_service.entity.Product;
import br.com.hospidata.stock_service.mapper.ProductMapper;
import br.com.hospidata.stock_service.repository.CategoryRepository;
import br.com.hospidata.stock_service.repository.ProductRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper , CategoryRepository categoryRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }


    @Transactional
    public List<ProductResponse> createProducts(List<ProductRequest> requests) throws Exception {

        if (requests == null || requests.isEmpty()) {
            throw new ListCannotEmpty();
        }

        Set<UUID> categoryIds = requests.stream()
                .map(ProductRequest::categoryId)
                .collect(Collectors.toSet());

        Map<UUID, Category> categories =
                categoryRepository.findAllById(categoryIds)
                        .stream()
                        .collect(Collectors.toMap(Category::getId, Function.identity()));

        Set<UUID> notFoundCategories = categoryIds.stream()
                .filter(id -> !categories.containsKey(id))
                .collect(Collectors.toSet());

        if (!notFoundCategories.isEmpty()) {
            throw new ResourceNotFoundException("Category", "id", notFoundCategories.toString());
        }

        Set<String> skus = requests.stream()
                .map(ProductRequest::skuCode)
                .collect(Collectors.toSet());

        if (skus.size() != requests.size()) {
            throw new DuplicateKeyException("Category" , "skuCode", skus.toString());
        }

        List<Product> products = requests.stream()
                .map(dto -> mapper.toEntity(dto, categories.get(dto.categoryId())))
                .toList();

        List<Product> savedProducts = repository.saveAll(products);

        return mapper.toResponses(savedProducts);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProducts(Boolean active) {
        if (active == null) {
            return mapper.toResponses(repository.findAll());
        }
        return mapper.toResponses(repository.findByActive(active));
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString())));
    }

    @Transactional
    public void deleteProduct(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));
         repository.deleteById(id);
    }


    @Transactional
    public void enableProductById(UUID id) {
        Product find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));
        find.setActive(true);
        repository.save(find);
    }

    @Transactional
    public ProductResponse updateProduct(ProductRequest request, UUID id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "categoryId", request.categoryId().toString()));

        repository.findBySkuCode(request.skuCode()).ifPresent(existingProduct -> {
            if (!existingProduct.getId().equals(id)) {
                throw new DuplicateKeyException("Category", "skuCode", request.skuCode().toString());
            }
        });

        product.setName(request.name());
        product.setDescription(request.description());
        product.setSkuCode(request.skuCode());
        product.setCategory(category);
        product.setMinStockAlert(request.minStockAlert());

        return mapper.toResponse(repository.save(product));
    }

    @Transactional
    public List<ProductResponse> uploadProducts(MultipartFile file) {

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

            List<ProductRequest> responses = mapper.fromCsv(parser);

            List<ProductResponse> saved = this.createProducts(responses);

            return saved;

        } catch (ResourceNotFoundException | IllegalArgumentException | DuplicateKeyException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Erro ao processar o CSV", e);
        }

    }
}
