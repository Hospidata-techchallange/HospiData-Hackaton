package br.com.hospidata.work_order_service.repository;

import br.com.hospidata.work_order_service.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID> {

}