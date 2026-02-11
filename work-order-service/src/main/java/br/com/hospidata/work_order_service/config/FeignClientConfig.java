package br.com.hospidata.work_order_service.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = null;

                String headerAuth = request.getHeader("Authorization");
                if (headerAuth != null && !headerAuth.isBlank()) {
                    token = headerAuth;
                }

                if (token == null && request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if ("accessToken".equals(cookie.getName())) {
                            token = cookie.getValue();
                            break;
                        }
                    }
                }

                if (token != null) {
                    if (!token.startsWith("Bearer ")) {
                        token = "Bearer " + token;
                    }
                    log.info("Injetando token no Feign Client para: {}", requestTemplate.url());
                    requestTemplate.header("Authorization", token);
                } else {
                    log.warn("Nenhum token encontrado para injetar na requisição Feign: {}", requestTemplate.url());
                }
            }
        };
    }
}