package br.com.hospidata.appointment_service.repository;

import br.com.hospidata.appointment_service.entity.Appointment;
import br.com.hospidata.appointment_service.entity.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRespository extends JpaRepository<Appointment, UUID>  , JpaSpecificationExecutor<Appointment> {

    boolean existsByDoctorIdAndAppointmentDateAndStartTimeAndActiveTrue(
            UUID doctorId,
            LocalDate appointmentDate,
            LocalTime startTime
    );

    List<Appointment> findByDoctorId(UUID doctorId);

    List<Appointment> findByDoctorIdAndActive(UUID doctorId, Boolean active);

    List<Appointment> findByDoctorIdAndAppointmentDateAndActiveTrueAndStatusNot(
            UUID doctorId,
            LocalDate appointmentDate,
            AppointmentStatus status
    );
}
