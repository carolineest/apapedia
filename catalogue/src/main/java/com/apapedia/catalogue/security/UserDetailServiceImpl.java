package com.apapedia.catalogue.security;

import com.apapedia.catalogue.DTO.response.LoadUsernameResDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

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
        LoadUsernameResDTO users = objectMapper.readValue(response.body(), LoadUsernameResDTO.class);

        Set<GrantedAuthority> grantedAuthoritySet = new HashSet();
        String authority = users.getAuthorities().get(0).getAuthority();
        if(authority.equals("Seller") || authority == "Seller"){
            grantedAuthoritySet.add(new SimpleGrantedAuthority("Seller"));
        }
        if(authority.equals("Customer") || authority == "Customer"){
            grantedAuthoritySet.add(new SimpleGrantedAuthority("Customer"));
        }
        return new User(users.getUsername(), users.getPassword(), grantedAuthoritySet);
    }
}
