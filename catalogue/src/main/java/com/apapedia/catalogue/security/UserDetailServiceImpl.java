package com.apapedia.catalogue.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class UserDetailServiceImpl {
    public UserDetails loadUserByUsername(String username) throws IOException, InterruptedException {
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("username", username);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/auth/loadUserByUsername"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        ObjectMapper objectMapper = new ObjectMapper();
        UserDetails user = objectMapper.readValue(response.body(), UserDetails.class);
        return user;
    }
}
