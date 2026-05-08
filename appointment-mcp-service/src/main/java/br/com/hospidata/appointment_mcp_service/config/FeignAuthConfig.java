package br.com.hospidata.appointment_mcp_service.config;

import br.com.hospidata.appointment_mcp_service.service.AuthTokenService;
import feign.Request;
import feign.RequestInterceptor;
import feign.RetryableException;
import feign.Retryer;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class FeignAuthConfig {

    private static final Set<Integer> AUTH_FAILURE_STATUSES = Set.of(401, 403);

    @Bean
    public RequestInterceptor feignAuthRequestInterceptor(AuthTokenService authTokenService) {
        return template -> {
            String token = authTokenService.getToken();
            if (token != null && !token.isBlank()) {
                template.header("Cookie", "accessToken=" + token);
            }
        };
    }

    @Bean
    public ErrorDecoder feignAuthErrorDecoder(AuthTokenService authTokenService) {
        ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

        return (methodKey, response) -> {
            if (AUTH_FAILURE_STATUSES.contains(response.status())) {
                authTokenService.clearToken();

                Request request = response.request();
                Collection<String> retryAfterValues = response.headers().get("Retry-After");
                Date retryAfter = null;
                if (retryAfterValues != null && !retryAfterValues.isEmpty()) {
                    retryAfter = new Date();
                }

                return new RetryableException(
                        response.status(),
                        "Authentication failed, retrying with a new login",
                        request.httpMethod(),
                        null,
                        retryAfter,
                        request
                );
            }

            return defaultErrorDecoder.decode(methodKey, response);
        };
    }

    @Bean
    public Retryer feignAuthRetryer() {
        return new Retryer.Default(100, 1000, 2);
    }
}
