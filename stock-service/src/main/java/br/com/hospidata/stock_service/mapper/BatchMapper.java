package br.com.hospidata.stock_service.mapper;

import br.com.hospidata.stock_service.controller.dto.BatchRequest;
import br.com.hospidata.stock_service.controller.dto.BatchResponse;
import br.com.hospidata.stock_service.entity.Batch;
import br.com.hospidata.stock_service.entity.Location;
import br.com.hospidata.stock_service.entity.Product;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class BatchMapper {

    public Batch toEntity(BatchRequest dto , Product product , Location location , String user) {
        return Batch.builder()
                .batchNumber(dto.batchNumber())
                .product(product)
                .location(location)
                .expirationDate(dto.expirationDate())
                .manufacturingDate(dto.manufacturingDate())
                .unitPrice(dto.unitPrice())
                .initialQuantity(dto.initialQuantity())
                .quantityAvailable(dto.initialQuantity())
                .createdBy(user)
                .build();
    }

    public BatchResponse toResponse(Batch entity) {
        return new BatchResponse(
                entity.getId(),
                entity.getBatchNumber(),

                entity.getProduct().getId(),
                entity.getProduct().getName(),

                entity.getLocation().getId(),
                entity.getLocation().getDescription(),

                entity.getExpirationDate(),
                entity.getManufacturingDate(),

                entity.getUnitPrice(),
                entity.getQuantityAvailable(),
                entity.getInitialQuantity(),

                entity.getCreatedAt(),
                entity.getLastUpdatedAt(),

                entity.getActive()
        );
    }

    public List<BatchResponse> toResponses(List<Batch> entities) {
        return entities.stream()
                .map(entity -> toResponse(entity))
                .toList();
    }


    public List<BatchRequest> fromCsv(CSVParser parser) {


        List<BatchRequest> batches = new ArrayList<>();

        for (CSVRecord record : parser) {

            BatchRequest batch = new BatchRequest(
                    record.get("batch_number"),
                    UUID.fromString(record.get("product_id")),
                    UUID.fromString(record.get("location_id")),
                    LocalDate.parse(record.get("expiration_date")),
                    LocalDate.parse(record.get("manufacturing_date")),
                    new BigDecimal(record.get("unit_price")),
                    Integer.valueOf(record.get("initial_quantity"))
            );

            batches.add(batch);
        }

        return batches;

    }
}
