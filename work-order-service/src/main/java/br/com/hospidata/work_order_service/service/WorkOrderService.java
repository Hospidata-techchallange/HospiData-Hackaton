package br.com.hospidata.work_order_service.service;

import br.com.hospidata.work_order_service.controller.dto.WorkOrderRequest;
import br.com.hospidata.work_order_service.controller.dto.WorkOrderResponse;

import java.util.List;
import java.util.UUID;

public interface WorkOrderService {
    WorkOrderResponse createOrder(WorkOrderRequest request);
    WorkOrderResponse getOrderById(UUID id);
    List<WorkOrderResponse> getAllOrders();
    WorkOrderResponse updateOrder(UUID id, WorkOrderRequest request);
    void deleteOrder(UUID id);
}