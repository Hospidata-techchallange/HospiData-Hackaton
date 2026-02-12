package br.com.hospidata.auth_service.controller;

import br.com.hospidata.auth_service.controller.dto.MeResponse;
import br.com.hospidata.auth_service.controller.dto.error.AuthResponse;
import br.com.hospidata.auth_service.controller.dto.LoginRequest;
import br.com.hospidata.auth_service.controller.dto.UserResponse;
import br.com.hospidata.auth_service.entity.User;
import br.com.hospidata.auth_service.mapper.UserMappper;
import br.com.hospidata.auth_service.service.AuthService;
import br.com.hospidata.auth_service.service.TokenService;
import br.com.hospidata.auth_service.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMappper userMappper;
    private final TokenService tokenService;

    public AuthController(AuthService authService , UserService userService , UserMappper userMappper, TokenService tokenService) {
        this.authService = authService;
        this.userService = userService;
        this.userMappper = userMappper;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest,
                                      HttpServletResponse response) {

        AuthResponse authResponse = authService.login(loginRequest);

        Cookie refreshCookie = new Cookie("refreshToken", authResponse.refreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/auth/refresh");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 dias
        response.addCookie(refreshCookie);

        Cookie accessCookie = new Cookie("accessToken", authResponse.accessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60); // 15 minutos
        response.addCookie(accessCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                        HttpServletResponse response) {
        if (refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }

        AuthResponse newTokens = authService.refresh(refreshToken);

        Cookie refreshCookie = new Cookie("refreshToken", newTokens.refreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/auth/refresh");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshCookie);

        Cookie accessCookie = new Cookie("accessToken", newTokens.accessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60);
        response.addCookie(accessCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/auth/refresh");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        Cookie accessCookie = new Cookie("accessToken", null);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse>  me(@CookieValue(value = "accessToken", required = false) String accessToken) {
        return ResponseEntity.ok().body(tokenService.getUserInformation(accessToken));
    }



}
