package br.com.hospidata.stock_service.service;

import br.com.hospidata.common.exceptions.ListCannotEmpty;
import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import br.com.hospidata.stock_service.common.CustomRsqlVisitor;
import br.com.hospidata.stock_service.controller.dto.*;
import br.com.hospidata.stock_service.entity.Batch;
import br.com.hospidata.stock_service.entity.Location;
import br.com.hospidata.stock_service.entity.Product;
import br.com.hospidata.stock_service.entity.StockMovement;
import br.com.hospidata.stock_service.mapper.BatchMapper;
import br.com.hospidata.stock_service.repository.BatchRepository;
import br.com.hospidata.stock_service.repository.LocationRepository;
import br.com.hospidata.stock_service.repository.ProductRepository;
import br.com.hospidata.stock_service.repository.StockMovementRepository;
import cz.jirutka.rsql.parser.RSQLParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BatchService {

    private static final Logger logger = LoggerFactory.getLogger(BatchService.class);

    private final BatchRepository repository;
    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final StockMovementRepository movementRepository;
    private final BatchMapper mapper;

    public BatchService(BatchRepository repository, ProductRepository productRepository, LocationRepository locationRepository, BatchMapper mapper,StockMovementRepository movementRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
        this.movementRepository = movementRepository;
        this.mapper = mapper;
    }


    @Transactional
    public List<BatchResponse> createBatches(String user , List<BatchRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            throw new ListCannotEmpty();
        }

        Set<UUID> productIds = requests.stream()
                .map(BatchRequest::productId)
                .collect(Collectors.toSet());

        Map<UUID, Product> products =
                productRepository.findAllById(productIds)
                        .stream()
                        .collect(Collectors.toMap(Product::getId, Function.identity()));

        Set<UUID> notFoundProductIds = productIds.stream()
                .filter(id -> !products.containsKey(id))
                .collect(Collectors.toSet());

        if (!notFoundProductIds.isEmpty()) {
            throw new ResourceNotFoundException("Product", "id", notFoundProductIds.toString());
        }

        Set<UUID> locationIds = requests.stream()
                .map(BatchRequest::locationId)
                .collect(Collectors.toSet());

        Map<UUID, Location> locations =
                locationRepository.findAllById(locationIds)
                        .stream()
                        .collect(Collectors.toMap(Location::getId, Function.identity()));

        Set<UUID> notFoundLocationIds = locationIds.stream()
                .filter(id -> !locations.containsKey(id))
                .collect(Collectors.toSet());

        if (!notFoundLocationIds.isEmpty()) {
            throw new ResourceNotFoundException( "Location", "id", notFoundLocationIds.toString()
            );
        }

        List<Batch> batches = requests.stream()
                .map(dto -> mapper.toEntity(
                        dto ,
                        products.get(dto.productId()) ,
                        locations.get(dto.locationId()) ,
                        user
                )).toList();

        List<Batch> savedBatches = repository.saveAll(batches);

        return mapper.toResponses(savedBatches);

    }

    @Transactional(readOnly = true)
    public List<BatchResponse> findAllBatches(Boolean active) {
        if (active == null) {
            return mapper.toResponses(repository.findAll());
        }
        return mapper.toResponses(repository.findByActive(active));
    }

    @Transactional(readOnly = true)
    public BatchResponse findBatchById(UUID id) {
        return mapper.toResponse(
                repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id.toString()))
        );
    }

    @Transactional
    public void deleteBatchById(UUID id , String user) {
        Batch find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id.toString()));

        find.setLastModifiedBy(user);
        find.setActive(false);

        repository.save(find);
    }

    @Transactional
    public void enableBatchById(UUID id, String user) {
        Batch find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id.toString()));
        find.setLastModifiedBy(user);
        find.setActive(true);
        repository.save(find);
    }

    @Transactional
    public BatchResponse updateBatchById(UUID id, BatchRequestUpdate dto , String user) {
        Batch find =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id.toString()));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id" , dto.productId().toString()));

        Location location = locationRepository.findById(dto.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id" , dto.locationId().toString()));

        find.setLastModifiedBy(user);

        find.setBatchNumber( dto.batchNumber());
        find.setProduct(product);
        find.setLocation(location);
        find.setExpirationDate(dto.expirationDate());
        find.setManufacturingDate(dto.manufacturingDate());
        find.setUnitPrice(dto.unitPrice());

        Batch saved = repository.save(find);
        return mapper.toResponse(saved);
    }

    @Transactional
    public List<BatchResponse> uploadBatches(MultipartFile file , String user) {

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

            List<BatchRequest> responses = mapper.fromCsv(parser);

            List<BatchResponse> saved = this.createBatches( user ,responses);

            return saved;

        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o CSV", e);
        }

    }

    public Page<BatchResponse> filterBatches(String search, Pageable pageable) {
        Specification<Batch> spec = null;

        if (search != null && !search.isBlank()) {
            spec = new RSQLParser()
                    .parse(search)
                    .accept(new CustomRsqlVisitor<>());
        }

        return repository
                .findAll(spec, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public void reduceStock(List<StockReductionRequest> items, String user) {
        for (StockReductionRequest item : items) {
            Batch batch = repository.findById(item.batchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", item.batchId().toString()));

            if (batch.getQuantityAvailable() < item.quantity()) {
                throw new IllegalArgumentException("Estoque insuficiente para o lote: " + batch.getBatchNumber());
            }

            batch.setQuantityAvailable(batch.getQuantityAvailable() - item.quantity());
            batch.setLastModifiedBy(user);

            if (batch.getQuantityAvailable() == 0) {
                batch.setActive(false);
            }
            repository.save(batch);

            StockMovement movement = StockMovement.builder()
                    .batch(batch)
                    .quantity(item.quantity())
                    .movementType("OUT")
                    .reason("Work Order Fulfillment")
                    .createdBy(user)
                    .build();
            movementRepository.save(movement);

            checkMinStockAlert(batch.getProduct());
        }
    }

    @Transactional
    public void restoreStock(List<StockRestoreRequest> items, String user) {
        for (StockRestoreRequest item : items) {
            List<Batch> activeBatches = repository.findByActive(true).stream()
                    .filter(b -> b.getProduct().getId().equals(item.getProductId()))
                    .sorted(Comparator.comparing(Batch::getExpirationDate)) // Pega o que vence primeiro ou último, aqui optei por manter ordem de data
                    .collect(Collectors.toList());

            if (activeBatches.isEmpty()) {
                throw new ResourceNotFoundException("Não há lotes ativos para devolver o produto ID: " + item.getProductId() + ". Ative um lote manualmente.");
            }

            Batch targetBatch = activeBatches.get(0);

            targetBatch.setQuantityAvailable(targetBatch.getQuantityAvailable() + item.getQuantity());
            targetBatch.setLastModifiedBy(user);
            repository.save(targetBatch);

            StockMovement movement = StockMovement.builder()
                    .batch(targetBatch)
                    .quantity(item.getQuantity())
                    .movementType("IN")
                    .reason("Work Order Cancelled / Restored")
                    .createdBy(user)
                    .build();
            movementRepository.save(movement);

            logger.info("Estoque restaurado: +{} itens do produto {} no lote {}", item.getQuantity(), item.getProductId(), targetBatch.getBatchNumber());
        }
    }

    private void checkMinStockAlert(Product product) {
        Integer totalStock = repository.getTotalQuantityByProductId(product.getId());
        if (totalStock == null) totalStock = 0;

        if (totalStock <= product.getMinStockAlert()) {
            logger.warn("ALERTA DE ESTOQUE BAIXO: Produto '{}' (SKU: {}) tem apenas {} unidades. Mínimo é {}.",
                    product.getName(), product.getSkuCode(), totalStock, product.getMinStockAlert());
        }
    }

    public List<BatchResponse> findBatchesByProductId(UUID productId) {
        List<Batch> batches = repository.findByProductIdAndActiveTrue(productId);
        return batches.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

}
