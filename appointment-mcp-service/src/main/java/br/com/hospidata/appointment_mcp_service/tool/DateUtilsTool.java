package br.com.hospidata.appointment_mcp_service.tool;

import br.com.hospidata.appointment_mcp_service.dto.util.AvailableDateResponse;
import br.com.hospidata.appointment_mcp_service.service.DateUtilsService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DateUtilsTool {

    private final DateUtilsService service;

    public DateUtilsTool(DateUtilsService service) {
        this.service = service;
    }

    @Tool(
            name = "getDatesByDayOfWeek",
            description = """
Retorna todas as datas correspondentes a um dia da semana dentro dos próximos 30 dias.

IMPORTANTE:
O parâmetro dayOfWeek DEVE ser informado no padrão ENUM em inglês:
MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY ou SUNDAY.

Exemplo:
- segunda-feira deve ser enviada como MONDAY
- terça-feira deve ser enviada como TUESDAY

Parâmetros esperados:
- dayOfWeek: Dia da semana no formato ENUM em inglês

O retorno contém:
- date: Data no formato ISO yyyy-MM-dd
- dayOfWeek: Dia da semana correspondente
"""
    )
    public List<AvailableDateResponse> getDatesByDayOfWeek(String dayOfWeek) {
        return service.getDatesByDayOfWeek(dayOfWeek);
    }
}
