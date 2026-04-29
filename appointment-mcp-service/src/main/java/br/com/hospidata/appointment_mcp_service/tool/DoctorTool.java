package br.com.hospidata.appointment_mcp_service.tool;


import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.doctor.DoctorClientResponse;
import br.com.hospidata.appointment_mcp_service.service.DoctorService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DoctorTool {

    private final DoctorService service;

    public DoctorTool(DoctorService service) {
        this.service = service;
    }

    @Tool(
            name = "getAllDoctors",
            description = """
    Retorna a lista de médicos disponíveis.

    Esta ferramenta deve ser utilizada quando o usuário:
    - Perguntar quais médicos estão disponíveis
    - Quiser ver a lista de médicos
    - Já tiver escolhido uma especialidade e quiser ver os médicos daquela área
    - Estiver no fluxo de agendamento e precisar selecionar um médico

    É útil para:
    - Ajudar o usuário a escolher um médico para consulta
    - Exibir opções de profissionais disponíveis para agendamento

    O retorno contém:
    - doctorId: Identificador único do médico
    - name: Nome do médico
    - categoryId: Identificador da especialidade
    - categoryName: Nome da especialidade médica
    """
    )
    public List<DoctorClientResponse> getAllDoctors() {
        return service.getAllDoctors();
    }

    @Tool(
            name = "getDoctorsByCategory",
            description = """
    Retorna a lista de médicos filtrados por especialidade (categoria).
    
    Esta ferramenta deve ser utilizada quando o usuário:
    - Informar uma especialidade médica específica (ex: cardiologia, ortopedia, pediatria)
    - Quiser ver médicos de uma determinada área
    - Já tiver escolhido a especialidade no fluxo de agendamento
    - Pedir algo como "quais médicos são de [especialidade]?"
    
    É útil para:
    - Filtrar médicos de acordo com a especialidade desejada pelo usuário
    - Refinar a busca antes da seleção de um médico
    - Melhorar a precisão no fluxo de agendamento
    
    Parâmetros esperados:
    - categoryId: Identificador único da especialidade médica
    
    O retorno contém:
    - doctorId: Identificador único do médico
    - name: Nome do médico
    - categoryId: Identificador da especialidade
    - categoryName: Nome da especialidade médica
    """
    )
    public PageResponse<DoctorClientResponse> getDoctorsByCategory(String categoryId) {
        return service.getAllDoctorsByCategoryId(categoryId);
    }




}
