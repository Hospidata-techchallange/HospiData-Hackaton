package br.com.hospidata.stock_service.repository;

import br.com.hospidata.stock_service.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> , JpaSpecificationExecutor<Batch> {

    List<Batch> findByActive(Boolean active);

    @Query("SELECT SUM(b.quantityAvailable) FROM Batch b WHERE b.product.id = :productId AND b.active = true")
    Integer getTotalQuantityByProductId(@Param("productId") UUID productId);
}
