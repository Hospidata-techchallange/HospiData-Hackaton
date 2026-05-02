package br.com.hospidata.appointment_mcp_service.service;

import br.com.hospidata.appointment_mcp_service.dto.util.AvailableDateResponse;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DateUtilsService {

    public List<AvailableDateResponse> getDatesByDayOfWeek(String dayOfWeek) {
        DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase());

        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(30);

        List<AvailableDateResponse> dates = new ArrayList<>();

        for (LocalDate date = today; !date.isAfter(limit); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == targetDay) {
                dates.add(new AvailableDateResponse(
                        date,
                        date.getDayOfWeek().name()
                ));
            }
        }

        return dates;
    }
}
