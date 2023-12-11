package com.apapedia.frontend.services;

import com.apapedia.frontend.DTO.LoginSsoReqDTO;
import com.apapedia.frontend.DTO.TokenDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserServiceImpl implements UserService{
    private final WebClient webClient;

    public UserServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    @Override
    public String getToken(String username, String name) {
        var body = new LoginSsoReqDTO(username, name);

        var response = this.webClient
                .post()
                .uri("/api/auth/login-sso")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(TokenDTO.class)
                .block();

        var token = response.getToken();

        return token;
    }
}
