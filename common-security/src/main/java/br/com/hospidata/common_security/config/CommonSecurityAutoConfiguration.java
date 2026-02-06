package br.com.hospidata.common_security.config;

import br.com.hospidata.common_security.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonSecurityAutoConfiguration {

    @Bean
    public TokenService tokenService() {
        return new TokenService();
    }
}
