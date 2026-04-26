package br.com.hospidata.appointment_mcp_service.tool;

import br.com.hospidata.appointment_mcp_service.dto.category.CategoryClientResponse;
import br.com.hospidata.appointment_mcp_service.dto.category.CategoryResponse;
import br.com.hospidata.appointment_mcp_service.service.CategoryService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryTool {

    private final CategoryService service;

    CategoryTool(CategoryService service) {
        this.service = service;
    }



    @Tool(
            name = "getAllCategories",
            description = """
        Retorna a lista de especialidades médicas disponíveis.

        Esta ferramenta deve ser utilizada quando o usuário:
        - Perguntar quais especialidades médicas existem
        - Quiser ver as especialidades disponíveis
        - Estiver iniciando o fluxo de agendamento e ainda não escolheu a especialidade

        É útil para:
        - Ajudar o usuário a escolher a especialidade correta antes de agendar uma consulta
        - Servir como passo inicial para buscar médicos por especialidade

        O retorno contém:
        - categoryId: Identificador único da especialidade
        - name: Nome da especialidade médica
        """
    )
    public List<CategoryResponse> getAllCategories() {
        return service.getAllCategories();
    }




}
