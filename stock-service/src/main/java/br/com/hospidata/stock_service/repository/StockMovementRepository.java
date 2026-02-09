package br.com.hospidata.stock_service.repository;

import br.com.hospidata.stock_service.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
}