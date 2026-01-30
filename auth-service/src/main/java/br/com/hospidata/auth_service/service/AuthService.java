package br.com.hospidata.auth_service.service;

import br.com.hospidata.auth_service.controller.dto.error.AuthResponse;
import br.com.hospidata.auth_service.controller.dto.LoginRequest;
import br.com.hospidata.auth_service.entity.User;
import br.com.hospidata.auth_service.entity.enums.Role;
import br.com.hospidata.auth_service.repository.UserRepository;
import br.com.hospidata.auth_service.service.exceptions.AccessDeniedException;
import br.com.hospidata.auth_service.service.exceptions.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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


        String accessToken = tokenService.generateAccessToken(user.getEmail());
        String refreshToken = tokenService.generateRefreshToken(user.getEmail());


        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(String refreshToken) {
        if (!tokenService.validateToken(refreshToken)) {
            throw new UnauthorizedException("Refresh token invalid");
        }
        String username = tokenService.getUsernameFromToken(refreshToken);
        String newAccessToken = tokenService.generateAccessToken(username);
        return new AuthResponse(newAccessToken, refreshToken);
    }

    public String getEmailFromAccessToken(String accessToken) {
        if (!tokenService.validateToken(accessToken)) {
            throw new UnauthorizedException("Access token invalid or expired");
        }
        return tokenService.getUsernameFromToken(accessToken);
    }

    public void validRoles (String accessToken , List<Role> allowedRoles) {
        var email = getEmailFromAccessToken(accessToken);

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        Role userRole = user.getRole();

        if (!allowedRoles.contains(userRole)) {
            throw new AccessDeniedException("You do not have permission to access this resource.");
        }
    }

    public String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new UnauthorizedException("Access token not found in cookies");
    }
}
