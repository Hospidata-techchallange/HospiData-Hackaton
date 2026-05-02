package br.com.hospidata.appointment_mcp_service.tool;

import br.com.hospidata.appointment_mcp_service.client.AppointmentClient;
import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.appointment.AppointmentClientResponse;
import br.com.hospidata.appointment_mcp_service.service.AppointmentsService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class AppointmentTool {

    private final AppointmentsService service;

    public AppointmentTool(AppointmentsService service) {
        this.service = service;
    }

    @Tool(
            name = "getAllAppointments",
            description = """
Retorna a lista de consultas/agendamentos já marcados.

Esta ferramenta deve ser utilizada quando o usuário:
- Precisar verificar a disponibilidade real de uma agenda em um dia específico
- Quiser saber quais horários já estão ocupados
- Estiver no fluxo de agendamento após escolher médico, especialidade, dia ou horário
- Perguntar se existe horário livre em determinada data
- Precisar comparar a agenda do médico com consultas já marcadas

É útil para:
- Identificar horários indisponíveis
- Evitar conflito de agendamento
- Verificar quais slots já foram reservados
- Apoiar a escolha de um horário livre para consulta

Regras de uso:
- Esta ferramenta retorna consultas já marcadas, não a agenda base do médico
- Deve ser usada em conjunto com as ferramentas de agenda médica
- Para saber os horários possíveis de atendimento, consulte primeiro a agenda do médico
- Para saber os horários ocupados, consulte os appointments
- A disponibilidade real deve ser calculada comparando:
  agenda do médico + consultas já marcadas no dia

O retorno contém:
- appointmentId: Identificador único do agendamento
- doctor: Dados resumidos do médico
- appointmentDate: Data da consulta
- startTime: Horário de início da consulta
- endTime: Horário final da consulta
- status: Status do agendamento
- notes: Observações do agendamento
- active: Indica se o agendamento está ativo
- createdAt: Data de criação
- lastUpdatedAt: Data da última atualização
"""
    )
    public PageResponse<AppointmentClientResponse> getAllAppointments() {
        return service.getAllAppointments();
    }

    @Tool(
            name = "getAppointmentsByAppointmentDateAndDoctorId"
    )
    public PageResponse<AppointmentClientResponse> getAppointmentsByAppointmentDateAndDoctorId(
            String doctorId , String appointmentDate
    ) {
         return service.getAppointmentsByAppointmentDateAndDoctorId(doctorId , appointmentDate);
    }

    @Tool(
            name = "createAppointment"
    )
    public AppointmentClientResponse createAppointment(
            String doctorId ,
            String appointmentDate ,
            String startTime,
            String notes
    ) {
        return service.createAppointment(doctorId , appointmentDate , startTime, notes);
    }

}
