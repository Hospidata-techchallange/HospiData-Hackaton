package br.com.hospidata.auth_service.security.aspect;

import br.com.hospidata.auth_service.service.AuthService;
import br.com.hospidata.auth_service.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Aspect
@Component
public class RoleAspect {

    private final AuthService authService;
    private final TokenService tokenService;

    public RoleAspect(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @Before("@annotation(checkRole)")
    public void checkRole(JoinPoint jp, CheckRole checkRole) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        tokenService.validRoles(request , List.of(checkRole.value()));
    }
}
