package br.com.hospidata.appointment_service.repository;

import br.com.hospidata.appointment_service.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {


    List<Doctor> findByActive(Boolean active);
}
