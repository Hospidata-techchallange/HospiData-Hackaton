package br.com.hospidata.work_order_service.service;

import br.com.hospidata.work_order_service.controller.dto.WorkOrderRequest;
import br.com.hospidata.work_order_service.controller.dto.WorkOrderResponse;

public interface WorkOrderService {

    WorkOrderResponse createOrder(WorkOrderRequest request);

    WorkOrderResponse getOrderById(Long id);
}