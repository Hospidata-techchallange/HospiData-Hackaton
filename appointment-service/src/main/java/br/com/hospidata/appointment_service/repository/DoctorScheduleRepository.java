package br.com.hospidata.appointment_service.repository;


import br.com.hospidata.appointment_service.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {

    List<DoctorSchedule> findByActive(Boolean active);

    List<DoctorSchedule> findByDoctorIdAndDayOfWeekAndActiveTrue(UUID doctorId, DayOfWeek dayOfWeek);

}
