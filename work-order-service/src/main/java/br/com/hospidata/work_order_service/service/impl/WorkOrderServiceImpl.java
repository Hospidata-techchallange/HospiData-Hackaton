package br.com.hospidata.work_order_service.service.impl;

import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import br.com.hospidata.work_order_service.controller.dto.*;
import br.com.hospidata.work_order_service.entity.WorkOrder;
import br.com.hospidata.work_order_service.entity.WorkOrderItem;
import br.com.hospidata.work_order_service.entity.enums.WorkOrderStatus;
import br.com.hospidata.work_order_service.integration.stock.StockClient;
import br.com.hospidata.work_order_service.integration.stock.dto.BatchDTO;
import br.com.hospidata.work_order_service.integration.stock.dto.StockReductionDTO;
import br.com.hospidata.work_order_service.integration.stock.dto.StockRestoreDTO;
import br.com.hospidata.work_order_service.repository.WorkOrderRepository;
import br.com.hospidata.work_order_service.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository repository;
    private final StockClient stockClient;

    @Override
    @Transactional
    public WorkOrderResponse createOrder(WorkOrderRequest request) {
        log.info("Iniciando criação de Work Order para UserID: {}", request.getUserId());

        WorkOrder workOrder = WorkOrder.builder()
                .userId(request.getUserId())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(WorkOrderStatus.OPEN)
                .build();

        List<WorkOrderItem> items = request.getItems().stream()
                .map(item -> WorkOrderItem.builder()
                        .workOrder(workOrder)
                        .productId(item.getProductId())
                        .quantityRequested(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        workOrder.setItems(items);

        List<PickingInstructionDTO> instructions = generatePickingInstructions(items);

        BigDecimal totalOrderCost = instructions.stream()
                .map(PickingInstructionDTO::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<StockReductionDTO> reductionRequests = instructions.stream()
                .map(instr -> StockReductionDTO.builder()
                        .batchId(instr.getBatchId())
                        .quantity(instr.getQuantityToPick())
                        .build())
                .collect(Collectors.toList());

        try {
            stockClient.reduceStock(reductionRequests);
        } catch (Exception e) {
            log.error("Erro ao comunicar com Stock Service para baixa: {}", e.getMessage());
            throw new RuntimeException("Erro ao reservar estoque: " + e.getMessage());
        }

        WorkOrder savedOrder = repository.save(workOrder);
        log.info("Work Order criada com sucesso ID: {}", savedOrder.getId());

        return toResponseWithCalculatedCost(savedOrder, instructions, totalOrderCost);
    }

    @Override
    public WorkOrderResponse getOrderById(UUID id) {
        WorkOrder order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id.toString()));

        return toResponse(order);
    }

    @Override
    public List<WorkOrderResponse> getAllOrders() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkOrderResponse updateOrder(UUID id, WorkOrderRequest request) {
        WorkOrder order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id.toString()));

        order.setDescription(request.getDescription());
        if (request.getPriority() != null) {
            order.setPriority(request.getPriority());
        }

        WorkOrder saved = repository.save(order);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        WorkOrder order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id.toString()));

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            log.info("Iniciando devolução de itens ao estoque para Work Order ID: {}", id);

            List<StockRestoreDTO> restoreList = order.getItems().stream()
                    .map(item -> StockRestoreDTO.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantityRequested())
                            .build())
                    .collect(Collectors.toList());

            try {
                stockClient.restoreStock(restoreList);
            } catch (Exception e) {
                log.error("Falha ao devolver itens ao estoque: {}", e.getMessage());
                throw new RuntimeException("Erro ao devolver itens ao estoque. Work Order não excluída. " + e.getMessage());
            }
        }

        repository.delete(order);
        log.info("Work Order ID: {} excluída com sucesso.", id);
    }


    private WorkOrderResponse toResponse(WorkOrder order) {
        List<PickingInstructionDTO> instructions = generatePickingInstructions(order.getItems());

        BigDecimal totalOrderCost = instructions.stream()
                .map(PickingInstructionDTO::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return toResponseWithCalculatedCost(order, instructions, totalOrderCost);
    }

    private WorkOrderResponse toResponseWithCalculatedCost(WorkOrder order, List<PickingInstructionDTO> instructions, BigDecimal totalCost) {
        return WorkOrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus())
                .priority(order.getPriority())
                .description(order.getDescription())
                .createdAt(order.getCreatedAt())
                .totalOrderCost(totalCost)
                .pickingInstructions(instructions)
                .build();
    }

    private List<PickingInstructionDTO> generatePickingInstructions(List<WorkOrderItem> items) {
        List<PickingInstructionDTO> instructions = new ArrayList<>();

        if (items == null) return instructions;

        for (WorkOrderItem item : items) {
            try {
                List<BatchDTO> batches = stockClient.getAvailableBatches(item.getProductId());

                batches.sort(Comparator.comparing(BatchDTO::getExpirationDate));

                int remainingQty = item.getQuantityRequested();

                for (BatchDTO batch : batches) {
                    if (remainingQty <= 0) break;

                    int quantityToTake = Math.min(remainingQty, batch.getQuantityAvailable());

                    BigDecimal unitPrice = batch.getUnitPrice() != null ? batch.getUnitPrice() : BigDecimal.ZERO;
                    BigDecimal cost = unitPrice.multiply(BigDecimal.valueOf(quantityToTake));

                    instructions.add(PickingInstructionDTO.builder()
                            .productId(item.getProductId())
                            .batchId(batch.getId())
                            .quantityToPick(quantityToTake)
                            .expirationDate(batch.getExpirationDate())
                            .location(String.format("Corredor: %s, Prateleira: %s, Seção: %s",
                                    batch.getAisle(), batch.getShelf(), batch.getSection()))
                            .unitPrice(unitPrice)
                            .totalCost(cost)
                            .build());

                    remainingQty -= quantityToTake;
                }
            } catch (Exception e) {
                log.warn("Não foi possível gerar instruções para o produto {}: {}", item.getProductId(), e.getMessage());
            }
        }
        return instructions;
    }
}