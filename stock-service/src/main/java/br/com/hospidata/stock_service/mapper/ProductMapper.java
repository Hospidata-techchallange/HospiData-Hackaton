package br.com.hospidata.stock_service.mapper;

import br.com.hospidata.stock_service.controller.dto.ProductRequest;
import br.com.hospidata.stock_service.controller.dto.ProductResponse;
import br.com.hospidata.stock_service.entity.Category;
import br.com.hospidata.stock_service.entity.Product;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest dto, Category category) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .skuCode(dto.skuCode())
                .category(category)
                .minStockAlert(dto.minStockAlert())
                .build();
    }

    public List<Product> toEntities(
            List<ProductRequest> dtos,
            Category category
    ) {
        return dtos.stream()
                .map(dto -> toEntity(dto, category))
                .toList();
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSkuCode(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getMinStockAlert(),
                product.getActive(),
                product.getCreatedAt(),
                product.getLastUpdatedAt()
        );
    }

    public List<ProductResponse> toResponses(List<Product> products) {
        return products.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductRequest> fromCsv(CSVParser parser) {

        List<ProductRequest> products = new ArrayList<>();

        for (CSVRecord record : parser) {

            ProductRequest product = new ProductRequest(
                    record.get("name").trim(),
                    record.get("description").trim(),
                    record.get("skuCode").trim(),
                    UUID.fromString(record.get("categoryId").trim()),
                    Integer.parseInt(record.get("minStockAlert").trim())
            );

            products.add(product);
        }

        return products;
    }
}
