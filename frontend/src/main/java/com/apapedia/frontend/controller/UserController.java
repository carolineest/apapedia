package com.apapedia.frontend.controller;

import com.apapedia.frontend.DTO.LoginReqDTO;
import com.apapedia.frontend.DTO.RegisterReqDTO;
import com.apapedia.frontend.DTO.ProfileResDTO;
import com.apapedia.frontend.DTO.RegisterReqDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
//import org.springframework.boot.configurationprocessor.json;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/register")
    public String registerPage(Model model) {
        RegisterReqDTO registerDTO = new RegisterReqDTO();
        model.addAttribute("registerDTO", registerDTO);
        return "Register";
    }

    @PostMapping("/register")
    public String submitFormRegister(@RequestParam(name = "name") String name,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "address") String address,
            Model model) throws IOException, InterruptedException {
        // Buat objek JSON
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("name", name);
        jsonBody.addProperty("username", username);
        jsonBody.addProperty("password", password);
        jsonBody.addProperty("email", email);
        jsonBody.addProperty("address", address);
        jsonBody.addProperty("role", "seller");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/user/register"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        if (response.body() == null) {
            return "Register";
        }
        return "Login";
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        var loginDTO = new LoginReqDTO();
        model.addAttribute("loginDTO", loginDTO);
        System.out.println("masuk GET LOGIN");
        return "Login";
    }

    @PostMapping("/login")
    public String submitFormLogin(@RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            HttpServletResponse response,
            Model model) throws IOException, InterruptedException {
        System.out.println("masuk POST LOGIN");
        // Buat objek JSON
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("username", username);
        jsonBody.addProperty("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/user/login"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(output);
        System.out.println(output.body());

        if (output.body() != null) {
            // Create a new cookie
            Cookie cookie = new Cookie("jwtToken", output.body());
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setMaxAge(24 * 60 * 60); // 24 hours in seconds

            // Add the cookie to the response
            response.addCookie(cookie);
            System.out.println("Login Success");
            return "LoginSuccess";
        }
        System.out.println("Login Failed");
        return "Login";
    }

    @GetMapping("/tryJwtToken")
    public String tryJwtToken(HttpServletRequest httpServletRequest, Model model)
            throws IOException, InterruptedException {
        System.out.println("masuk GET TRYJWT");
        // Retrieve cookies from the request
        Cookie[] cookies = httpServletRequest.getCookies();

        System.out.println(cookies);
        // Search for the "jwtToken" cookie
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8082/user/validate"))
                            .header("Authorization", "Bearer " + jwtToken)
                            .GET()
                            .build();
                    HttpResponse<String> output = HttpClient.newHttpClient().send(request,
                            HttpResponse.BodyHandlers.ofString());
                    System.out.println(output);
                    System.out.println(output.body());
                    if (output.body() != null) {
                        return "TryJwtSuccess";
                    }
                    return "Login";
                }
            }
        }
        return "Login";
    }

    @GetMapping("/profile")
    public String profilePage(HttpServletRequest httpServletRequest, Model model)throws IOException, InterruptedException {
//        RegisterReqDTO registerDTO = new RegisterReqDTO();
//        model.addAttribute("registerDTO", registerDTO);
        Cookie[] cookies = httpServletRequest.getCookies();

        System.out.println(cookies);
        // Search for the "jwtToken" cookie
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8082/user/profile"))
                            .header("Authorization", "Bearer " + jwtToken)
                            .GET()
                            .build();
                    HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println(output);
                    System.out.println(output.body());

                    ObjectMapper objectMapper = new ObjectMapper();
                    ProfileResDTO user = objectMapper.readValue(output.body(), ProfileResDTO.class);


                    model.addAttribute("user", user);
                    return "user-profile";


                }
            }
        }
        return "Login";
    }

    @GetMapping("/edit-profile")
    public String editProfilePage(Model model) {
        RegisterReqDTO registerDTO = new RegisterReqDTO();
        model.addAttribute("registerDTO", registerDTO);
        return "user-edit-profile";
    }

    @PostMapping("/edit-profile")
    public String submitFormEditProfile(@RequestParam(name = "name") String name,
                                     @RequestParam(name = "username") String username,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "email") String email,
                                     @RequestParam(name = "address") String address,
                                     Model model) throws IOException, InterruptedException {
        // Buat objek JSON
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("name", name);
        jsonBody.addProperty("username", username);
        jsonBody.addProperty("password", password);
        jsonBody.addProperty("email", email);
        jsonBody.addProperty("address", address);
        jsonBody.addProperty("role", "seller");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/user/edit-profile"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        if (response.body() == null) {

            return "user-edit-profile";
        }
        return "redirect:/user/profile";
    }

    @GetMapping("/withdraw")
    public String withdrawPage(Model model) {
        RegisterReqDTO registerDTO = new RegisterReqDTO();
        model.addAttribute("registerDTO", registerDTO);
        return "user-withdraw";
    }
}
