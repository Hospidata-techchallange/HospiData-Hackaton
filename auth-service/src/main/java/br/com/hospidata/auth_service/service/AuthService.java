package br.com.hospidata.auth_service.service;

import br.com.hospidata.auth_service.controller.dto.error.AuthResponse;
import br.com.hospidata.auth_service.controller.dto.LoginRequest;
import br.com.hospidata.auth_service.entity.User;
import br.com.hospidata.auth_service.repository.UserRepository;
import br.com.hospidata.auth_service.service.exceptions.UnauthorizedException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public AuthService(UserRepository repository, PasswordEncoder encoder, TokenService tokenService) {
        this.repository = repository;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    public AuthResponse login(LoginRequest login) {
        User user = repository.findByEmail(login.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));


        if (!encoder.matches(login.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }


        String accessToken = tokenService.generateAccessToken(
                user.getId().toString(),
                user.getEmail(),
                user.getRole().name()
        );

        String refreshToken = tokenService.generateRefreshToken(
                user.getId().toString(),
                user.getEmail(),
                user.getRole().name()
        );


        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(String refreshToken) {
        if (!tokenService.validateToken(refreshToken)) {
            throw new UnauthorizedException("Refresh token invalid");
        }
        String username = tokenService.getUsernameFromToken(refreshToken);

        User user = repository.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        String newAccessToken = tokenService.generateAccessToken(
                user.getId().toString(),
                user.getEmail(),
                user.getRole().name()
        );
        return new AuthResponse(newAccessToken, refreshToken);
    }
}
