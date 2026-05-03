package br.com.hospidata.appointment_mcp_service.tool;

import br.com.hospidata.appointment_mcp_service.dto.util.AvailableDateResponse;
import br.com.hospidata.appointment_mcp_service.service.DateUtilsService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    @Tool(
            name = "getCurrentDate",
            description = """
            Retorna a data atual oficial do sistema.

            Use esta ferramenta sempre que o usuário mencionar datas relativas, como:
            - hoje
            - amanhã
            - depois de amanhã
            - semana que vem
            - mês que vem
            - próxima segunda-feira
            - próxima terça-feira
            - qualquer dia da semana sem informar uma data exata

            Regras:
            - Nunca use conhecimento interno do modelo para descobrir a data atual.
            - Sempre chame esta ferramenta antes de calcular datas relativas.
            - A data retornada deve ser usada como referência oficial para agendamentos.
            - Nunca crie agendamentos em datas anteriores à data retornada.

            O retorno contém:
            - currentDate: data atual no formato yyyy-MM-dd
            - timezone: timezone usado no cálculo
            """
    )
    public LocalDate getCurrentDate() {
        return service.getCurrentDate();
    }



}
