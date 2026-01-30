package br.com.hospidata.auth_service.controller.dto;

import br.com.hospidata.auth_service.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "UserResponse", description = "DTO de resposta para usuário")
public record UserResponse(

        @Schema(description = "ID do usuário", example = "84980e39-bdfa-4b9b-b50a-bed439bcbcc4", required = true) UUID id,

        @Schema(description = "Nome do usuário", example = "João da Silva", required = true)
        String name,

        @Schema(description = "Email do usuário", example = "joao@email.com", required = true)
        String email,

        @Schema(description = "Função/Perfil do usuário", example = "ADMIN", required = true) Role role,

        @Schema(description = "Data de criação do usuário", example = "2025-09-06T22:15:30", required = true) LocalDateTime createdAt,

        @Schema(description = "Data da última atualização do usuário", example = "2025-09-07T01:45:00", required = true)
        LocalDateTime lastUpdatedAt,

        @Schema(description = "Indica se o usuário está ativo", example = "true", required = true)
        Boolean active
) {}
