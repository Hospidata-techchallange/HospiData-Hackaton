package br.com.hospidata.appointment_mcp_service.tool;

import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.appointment.AppointmentClientResponse;
import br.com.hospidata.appointment_mcp_service.dto.appointment.DoctorAvailableSlotsResponse;
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
- Quiser consultar appointments existentes
- Precisar listar ou auditar consultas já criadas
- Perguntar por consultas marcadas, status ou detalhes de appointments

É útil para:
- Visualizar consultas registradas
- Consultar dados operacionais de agendamentos

Regras de uso:
- Esta ferramenta retorna consultas já marcadas, nao horarios livres
- Nao use esta ferramenta como fonte principal para afirmar disponibilidade de horarios
- Para descobrir horarios realmente disponiveis de um medico em uma data, use getAvailableSlotsByDoctorAndDate

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
            name = "getAppointmentsByAppointmentDateAndDoctorId",
            description = """
Retorna consultas ja marcadas filtradas por medico e data.

Use apenas quando o objetivo for consultar appointments existentes de um medico em uma data especifica.

Regras de uso:
- Nao use esta ferramenta para afirmar quais horarios estao disponiveis
- Para disponibilidade real, use getAvailableSlotsByDoctorAndDate
- Esta ferramenta deve continuar existindo para consultas especificas de appointments

Parametros esperados:
- doctorId: UUID string do medico
- appointmentDate: data no formato yyyy-MM-dd
"""
    )
    public PageResponse<AppointmentClientResponse> getAppointmentsByAppointmentDateAndDoctorId(
            String doctorId , String appointmentDate
    ) {
         return service.getAppointmentsByAppointmentDateAndDoctorId(doctorId , appointmentDate);
    }

    @Tool(
            name = "getAvailableSlotsByDoctorAndDate",
            description = """
Retorna os horarios realmente disponiveis de um medico em uma data especifica. Esta ferramenta considera a agenda base do medico e remove horarios ja ocupados por consultas existentes. Use quando ja houver doctorId e appointmentDate definidos no contexto.

Esta ferramenta deve ser usada quando:
- O usuario ja tiver escolhido um medico
- Houver doctorId disponivel no contexto
- Houver appointmentDate resolvida no formato yyyy-MM-dd
- O usuario perguntar por horarios livres, horarios disponiveis ou disponibilidade real de um medico em uma data

Regras de uso:
- Nao use se o usuario ainda nao informou ou escolheu um medico
- Nao use se ainda nao houver doctorId no contexto
- Nao use se o usuario ainda nao informou data
- Nao use com datas relativas sem antes resolver a data real com getCurrentDate ou outra ferramenta de data
- Use esta ferramenta no lugar de calcular disponibilidade manualmente com agenda + appointments
- Nao use getAppointmentsByAppointmentDateAndDoctorId como principal ferramenta de disponibilidade

Parametros esperados:
- doctorId: UUID string do medico
- appointmentDate: data no formato yyyy-MM-dd

O retorno contem:
- doctorId: Identificador unico do medico
- doctorName: Nome do medico
- appointmentDate: Data consultada
- dayOfWeek: Dia da semana da data consultada
- availableSlots: Lista de horarios realmente disponiveis
"""
    )
    public DoctorAvailableSlotsResponse getAvailableSlotsByDoctorAndDate(
            String doctorId,
            String appointmentDate
    ) {
        return service.getAvailableSlotsByDoctorAndDate(doctorId, appointmentDate);
    }

    @Tool(
            name = "createAppointment",
            description = """
Cria uma nova consulta/agendamento para um medico em uma data e horario especificos.

Use esta ferramenta quando:
- O usuario confirmar que deseja agendar a consulta
- Houver doctorId definido no contexto
- Houver appointmentDate resolvida no formato yyyy-MM-dd
- Houver startTime escolhido no formato HH:mm
- O horario escolhido ja tiver sido validado como disponivel por getAvailableSlotsByDoctorAndDate

Regras de uso:
- Nao use se o usuario ainda nao escolheu um medico
- Nao use se ainda nao houver doctorId no contexto
- Nao use se a data ainda for relativa ou ambigua
- Nao use se ainda nao houver horario escolhido
- Antes de criar o agendamento, consulte getAvailableSlotsByDoctorAndDate para confirmar que startTime esta em availableSlots
- Nao crie agendamento em horario que nao aparece em availableSlots
- Use notes apenas para observacoes clinicas ou administrativas informadas pelo usuario; se nao houver observacao, envie texto vazio

Parametros esperados:
- doctorId: UUID string do medico
- appointmentDate: data no formato yyyy-MM-dd
- startTime: horario de inicio no formato HH:mm
- notes: observacoes opcionais sobre o agendamento

O retorno contem os dados da consulta criada, incluindo identificador, medico, data, horario, status e observacoes.
"""
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
