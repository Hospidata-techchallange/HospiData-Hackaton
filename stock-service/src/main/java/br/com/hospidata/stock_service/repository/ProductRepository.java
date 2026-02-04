package br.com.hospidata.stock_service.repository;

import br.com.hospidata.stock_service.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByActive(Boolean active);

    Optional<Product> findBySkuCode(String s);
}
