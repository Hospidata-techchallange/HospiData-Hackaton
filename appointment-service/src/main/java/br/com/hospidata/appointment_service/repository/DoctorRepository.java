package br.com.hospidata.appointment_service.repository;

import br.com.hospidata.appointment_service.entity.Appointment;
import br.com.hospidata.appointment_service.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> , JpaSpecificationExecutor<Doctor> {


    List<Doctor> findByActive(Boolean active);
}
