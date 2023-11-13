package com.apapedia.user.service;

import com.apapedia.user.DTO.response.ValidToken;
import com.apapedia.user.jwt.JwtUtil;
import com.apapedia.user.model.User;
import com.apapedia.user.repository.UserDB;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDB userDB;

    public String generateToken(String username, String role) {
        return jwtUtil.generateToken(username, role);
    }

    public ValidToken validateToken(String token) {
        Claims claims = jwtUtil.validateToken(token);
        User user = null;
        if(claims != null){
            String username = claims.getSubject();
            user = userDB.findByUsername(username);
        }
        if(user != null){
            var validToken = new ValidToken(claims.getSubject(), (String) claims.get("role"));
            return validToken;
        }
        return null;
//        return jwtUtil.validateToken(token);
    }
}
