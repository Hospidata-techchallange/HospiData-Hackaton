package br.com.hospidata.auth_service.security.aspect;


import br.com.hospidata.auth_service.entity.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {
    Role[] value();
}
