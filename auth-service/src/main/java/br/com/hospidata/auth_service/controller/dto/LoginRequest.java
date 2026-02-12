package br.com.hospidata.auth_service.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest", description = "DTO para login do usuário")
public record LoginRequest(

        @Schema(description = "Email do usuário", example = "joao@email.com", required = true)
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email must not be blank")
        String email,

        @Schema(description = "Senha do usuário", example = "123456", required = true)
        @NotBlank(message = "Password must not be blank")
        String password

) {}