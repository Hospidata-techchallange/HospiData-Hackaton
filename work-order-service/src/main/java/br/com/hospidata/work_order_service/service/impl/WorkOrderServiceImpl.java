package br.com.hospidata.work_order_service.service.impl;

import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import br.com.hospidata.work_order_service.controller.dto.*;
import br.com.hospidata.work_order_service.entity.WorkOrder;
import br.com.hospidata.work_order_service.entity.WorkOrderItem;
import br.com.hospidata.work_order_service.entity.enums.WorkOrderStatus;
import br.com.hospidata.work_order_service.integration.stock.StockClient;
import br.com.hospidata.work_order_service.integration.stock.dto.BatchDTO;
import br.com.hospidata.work_order_service.integration.stock.dto.StockReductionDTO;
import br.com.hospidata.work_order_service.repository.WorkOrderRepository;
import br.com.hospidata.work_order_service.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository repository;
    private final StockClient stockClient;

    @Override
    @Transactional
    public WorkOrderResponse createOrder(WorkOrderRequest request) {

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

        List<StockReductionDTO> reductionRequests = instructions.stream()
                .map(instr -> StockReductionDTO.builder()
                        .batchId(instr.getBatchId())
                        .quantity(instr.getQuantityToPick())
                        .build())
                .collect(Collectors.toList());

        try {
            stockClient.reduceStock(reductionRequests);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao reservar estoque: " + e.getMessage());
        }

        WorkOrder savedOrder = repository.save(workOrder);

        return WorkOrderResponse.builder()
                .id(savedOrder.getId())
                .status(savedOrder.getStatus())
                .createdAt(savedOrder.getCreatedAt())
                .pickingInstructions(instructions)
                .build();
    }

    @Override
    public WorkOrderResponse getOrderById(Long id) {
        WorkOrder order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order not found with id: " + id));

        return WorkOrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .pickingInstructions(generatePickingInstructions(order.getItems()))
                .build();
    }

    private List<PickingInstructionDTO> generatePickingInstructions(List<WorkOrderItem> items) {
        List<PickingInstructionDTO> instructions = new ArrayList<>();

        for (WorkOrderItem item : items) {
            List<BatchDTO> batches = stockClient.getAvailableBatches(item.getProductId());

            batches.sort(Comparator.comparing(BatchDTO::getExpirationDate));

            int remainingQty = item.getQuantityRequested();

            for (BatchDTO batch : batches) {
                if (remainingQty <= 0) break;

                int quantityToTake = Math.min(remainingQty, batch.getQuantityAvailable());

                instructions.add(PickingInstructionDTO.builder()
                        .productId(item.getProductId())
                        .batchId(batch.getId())
                        .quantityToPick(quantityToTake)
                        .expirationDate(batch.getExpirationDate())
                        .location(String.format("Corredor: %s, Prateleira: %s, Seção: %s",
                                batch.getAisle(), batch.getShelf(), batch.getSection()))
                        .build());

                remainingQty -= quantityToTake;
            }
        }
        return instructions;
    }
}