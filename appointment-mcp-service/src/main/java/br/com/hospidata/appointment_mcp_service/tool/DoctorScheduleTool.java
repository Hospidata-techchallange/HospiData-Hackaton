package br.com.hospidata.appointment_mcp_service.tool;

import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.doctor_schedule.DoctorScheduleResponse;
import br.com.hospidata.appointment_mcp_service.service.DoctorScheduleService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoctorScheduleTool {

    private final DoctorScheduleService service;

    public DoctorScheduleTool(DoctorScheduleService service) {
        this.service = service;
    }


    @Tool(
            name = "getAllDoctorSchedules",
            description = """
Retorna a agenda de horários disponíveis de médicos.

IMPORTANTE:
Esta ferramenta deve ser utilizada APENAS quando o usuário NÃO informou um médico específico (doctorId).
Se o usuário já tiver escolhido um médico, outra ferramenta mais específica deve ser utilizada.

Esta ferramenta deve ser utilizada quando o usuário:
- Perguntar disponibilidade de horários de forma geral (sem indicar médico)
- Quiser ver quais dias e horários existem no sistema
- Ainda não escolheu um médico no fluxo de agendamento
- Perguntar algo como "quais horários estão disponíveis?"

É útil para:
- Dar uma visão geral da disponibilidade de atendimento
- Ajudar o usuário a entender os dias e períodos disponíveis
- Servir como apoio antes da escolha do médico

Regras de uso:
- NÃO utilizar se doctorId estiver disponível no contexto
- Utilizar apenas para consultas genéricas de agenda

O retorno contém:
- scheduleId: Identificador da agenda
- doctorId: Identificador do médico
- doctorName: Nome do médico
- dayOfWeek: Dia da semana (ex: MONDAY, TUESDAY)
- startTime: Horário de início do atendimento
- endTime: Horário final do atendimento
- slotDurationMinutes: Duração de cada consulta em minutos
- active: Indica se a agenda está ativa
"""
    )
    public List<DoctorScheduleResponse> getAllDoctorSchedules() {
        return service.getAllDoctorSchedules();
    }


    @Tool(
            name = "getDoctorSchedulesByDoctorId",
            description = """
Retorna a agenda de horários de um médico específico.

Esta ferramenta deve ser utilizada quando o usuário:
- Já escolheu um médico específico
- Informar ou o contexto já possuir o doctorId
- Perguntar quais dias ou horários um médico específico atende
- Estiver no fluxo de agendamento após selecionar o médico

É útil para:
- Mostrar a disponibilidade de atendimento de um médico específico
- Exibir os dias da semana em que o médico atende
- Exibir horário inicial, horário final e duração dos slots de consulta
- Ajudar o usuário a escolher um dia e horário para agendamento

Parâmetros esperados:
- doctorId: Identificador único do médico em formato UUID string

Regras de uso:
- Utilizar somente quando houver doctorId disponível
- Se o usuário ainda não escolheu um médico, utilize a ferramenta genérica getAllDoctorSchedules

O retorno contém:
- scheduleId: Identificador da agenda
- doctorId: Identificador do médico
- doctorName: Nome do médico
- dayOfWeek: Dia da semana (ex: MONDAY, TUESDAY)
- startTime: Horário de início do atendimento
- endTime: Horário final do atendimento
- slotDurationMinutes: Duração de cada consulta em minutos
- active: Indica se a agenda está ativa
"""
    )
    public PageResponse<DoctorScheduleResponse> getDoctorSchedulesByDoctorId(String doctorId) {
        return service.getDoctorSchedulesByDoctorId(doctorId);
    }

    @Tool(
            name = "getDoctorSchedulesByCategoryId",
            description = """
Retorna a agenda de horários dos médicos filtrados por especialidade (categoria).

IMPORTANTE:
Esta ferramenta deve ser PRIORIZADA quando o usuário já informou ou escolheu uma especialidade médica,
mas ainda não escolheu um médico específico.

Esta ferramenta deve ser utilizada quando o usuário:
- Já escolheu uma especialidade médica
- Informar ou o contexto já possuir o categoryId
- Quiser ver horários disponíveis para uma especialidade específica
- Perguntar algo como "quais horários tem para cardiologia?"
- Estiver no fluxo de agendamento após escolher a especialidade, mas antes de escolher o médico

É útil para:
- Verificar a agenda dos médicos daquela especialidade
- Mostrar quais médicos atendem em determinados dias e horários
- Ajudar o usuário a escolher um médico com base na disponibilidade
- Refinar o fluxo antes da seleção do médico

Parâmetros esperados:
- categoryId: Identificador único da especialidade médica em formato UUID string

Regras de uso:
- Utilizar quando houver categoryId disponível
- Priorizar esta ferramenta para consultar agendas por especialidade
- Se o usuário já tiver escolhido um médico específico, utilize getDoctorSchedulesByDoctorId
- Se não houver categoryId nem doctorId, utilize getAllDoctorSchedules

O retorno contém:
- scheduleId: Identificador da agenda
- doctorId: Identificador do médico
- doctorName: Nome do médico
- categoryId: Identificador da especialidade médica
- categoryName: Nome da especialidade médica
- dayOfWeek: Dia da semana (ex: MONDAY, TUESDAY)
- startTime: Horário de início do atendimento
- endTime: Horário final do atendimento
- slotDurationMinutes: Duração de cada consulta em minutos
- active: Indica se a agenda está ativa
"""
    )
    public PageResponse<DoctorScheduleResponse> getDoctorSchedulesByCategoryId(String categoryId) {
        return service.getDoctorSchedulesByCategoryId(categoryId);
    }

}
