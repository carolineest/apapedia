package com.apapedia.order.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${apapedia.web.jwtSecret}")
    private String jwtSecret;

    @Value("${apapedia.web.jwtExpirationMs}")
    private long jwtExpirationMs;


    public String generateJwtToken(String username, String role, UUID userId) {
        Jwts.parser().setSigningKey(jwtSecret);
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put("userId", userId);

        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(new Date().getTime() + TimeUnit.MILLISECONDS.toMillis(jwtExpirationMs)));
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("role");
    }

    public UUID getUserIdFromToken(String token) {
        String userIdString = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("userId", String.class);

        // Convert String to UUID
        return UUID.fromString(userIdString);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
