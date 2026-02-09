package br.com.hospidata.stock_service.repository;

import br.com.hospidata.stock_service.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {


    List<Location> findByActive(Boolean active);
}
