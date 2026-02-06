package br.com.hospidata.common_security.service;

import br.com.hospidata.common.exceptions.AccessDeniedException;
import br.com.hospidata.common.exceptions.UnauthorizedException;
import br.com.hospidata.common_security.dto.MeResponse;
import br.com.hospidata.common_security.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

//@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(
            String userId,
            String username,
            String role
    ) {
        return Jwts.builder()
                .setSubject(username) // sub
                .claim("user_id", userId)
                .claim("email", username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(
            String userId,
            String username,
            String role
    ) {
        return Jwts.builder()
                .setSubject(username) // sub
                .claim("user_id", userId)
                .claim("email", username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public MeResponse getUserInformation(String accessToken) {
        if (!this.validateToken(accessToken)) {
            throw new UnauthorizedException("Access token invalid or expired");
        }

        Claims claims = this.getAllClaims(accessToken);

        return new MeResponse(
                claims.get("user_id", String.class),
                claims.get("email", String.class),
                claims.get("role", String.class)
        );
    }

    public void validRoles(HttpServletRequest request, List<Role> allowedRoles) {

        String accessToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        if (accessToken == null) {
            throw new UnauthorizedException("Access token not found");
        }

        MeResponse meResponse = getUserInformation(accessToken);


        Role userRole;
        try {
            userRole = Role.valueOf(meResponse.role());
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid role in token");
        }

        if (!allowedRoles.contains(userRole)) {
            throw new AccessDeniedException("You do not have permission to access this resource.");
        }
    }
}
