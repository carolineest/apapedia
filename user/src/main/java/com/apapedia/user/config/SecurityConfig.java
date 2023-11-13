//package com.apapedia.user.config;
//
//
//import io.jsonwebtoken.JwtParser;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.Authentication;
//
//import java.util.Collections;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    private final String secretKey = "yourSecretKey"; // Ganti dengan kunci rahasia yang kuat
//
//    @Bean
//    public JwtParser jwtParser() {
//        return Jwts.parser().setSigningKey(secretKey);
//    }
//
//    public Authentication getAuthentication(HttpServletRequest request) {
//        String token = request.getHeader("Authorization").substring(7);
//        if (token != null) {
//            // Parsing token dan melakukan validasi (sesuai kebutuhan)
//            String username = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
//            if (username != null) {
//                return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
//            }
//        }
//        return null;
//    }
//}
