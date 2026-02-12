package br.com.hospidata.stock_service.repository;

import br.com.hospidata.stock_service.controller.dto.CategoryResponse;
import br.com.hospidata.stock_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByActive(Boolean active);
}
