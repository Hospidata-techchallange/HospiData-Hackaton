package br.com.hospidata.auth_service.security.aspect;

import br.com.hospidata.auth_service.service.AuthService;
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

    public RoleAspect(AuthService authService) {
        this.authService = authService;
    }

    @Before("@annotation(checkRole)")
    public void checkRole(JoinPoint jp, CheckRole checkRole) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = authService.getTokenFromCookie(request);
        authService.validRoles(token, List.of(checkRole.value()));
    }
}
