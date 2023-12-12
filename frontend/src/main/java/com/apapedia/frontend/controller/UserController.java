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
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
//import org.springframework.boot.configurationprocessor.json;

@Controller
@RequestMapping("/user")
public class UserController {

    @PostMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        // Invalidate the session (if any)
        System.out.println("MASUK LOGOUT");
        request.getSession().invalidate();

        // Remove the JWT cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // Set the cookie's maximum age to 0, effectively deleting it
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        return "redirect:/user/login"; // Redirect to the login page or any other desired destination
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
    public String profilePage(HttpServletRequest httpServletRequest, Model model)
            throws IOException, InterruptedException {

        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
                                                                    // tidak ada

        if (session == null) {
            return "Register";
        }

        String jwtToken = null;
        jwtToken = (String) session.getAttribute("token");

        // RegisterReqDTO registerDTO = new RegisterReqDTO();
        // model.addAttribute("registerDTO", registerDTO);
        System.out.println("MASUK PROFILE USERCONTROLLER");
        System.out.println(jwtToken);
        // Cookie[] cookies = httpServletRequest.getCookies();

        // System.out.println(cookies);
        // // Search for the "jwtToken" cookie
        // String jwtToken = null;
        // if (cookies != null) {
        // for (Cookie cookie : cookies) {
        // System.out.println(cookie.getName());
        // if ("jwtToken".equals(cookie.getName())) {
        // jwtToken = cookie.getValue();
        System.out.println("Sending HTTP request to backend...");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/user/profile"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        System.out.println("HTTP request created: " + request);

        HttpResponse<String> output = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Received HTTP response from backend: " + output.body());

        ObjectMapper objectMapper = new ObjectMapper();
        ProfileResDTO user = objectMapper.readValue(output.body(),
                ProfileResDTO.class);

        model.addAttribute("user", user);
        return "user-profile";

        // }
        // }
        // }
        // return "Login";
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
        System.out.println("10 Desember 2023");

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
    public String withdrawPage(HttpServletRequest httpServletRequest, Model model)
            throws IOException, InterruptedException {
        // RegisterReqDTO registerDTO = new RegisterReqDTO();
        // model.addAttribute("registerDTO", registerDTO);
        System.out.println("MASUK GETMAPPING WITHDRAW");
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
                                                                    // tidak ada

        if (session == null) {
            return "Register";
        }
        String jwtToken = null;
        jwtToken = (String) session.getAttribute("token");

        // Cookie[] cookies = httpServletRequest.getCookies();

        // System.out.println(cookies);
        // // Search for the "jwtToken" cookie
        // String jwtToken = null;
        // if (cookies != null) {
        // for (Cookie cookie : cookies) {
        // System.out.println(cookie.getName());
        // if ("jwtToken".equals(cookie.getName())) {
        // jwtToken = cookie.getValue();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/user/profile"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(output);
        System.out.println(output.body());

        ObjectMapper objectMapper = new ObjectMapper();
        ProfileResDTO user = objectMapper.readValue(output.body(),
                ProfileResDTO.class);
        System.out.println(user);
        System.out.println("AMAN123");

        model.addAttribute("user", user);
        return "user-withdraw";

        // }
        // }
        // }
        // return "Login";
    }

    @PostMapping("withdraw")
    public String submitWithdrawPage(@ModelAttribute ProfileResDTO user, Model model)
            throws IOException, InterruptedException {
        System.out.println(user.getBalance() + " INI BALANCENYA MAN");
        System.out.println(user.getId() + " INI ID MAN");
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("balance", user.getBalance());
        jsonBody.addProperty("id", user.getId().toString());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/user/withdraw"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        if (response.body() == null) {

            return "user-withdraw";
        }
        return "redirect:/user/profile";
    }

    @PostMapping("delete-account")
    public String deleteAccount(@ModelAttribute("user") ProfileResDTO user, Model model)
            throws IOException, InterruptedException {
        System.out.println("TANGGAL 9 DESEMBER");
        System.out.println(user.getBalance() + " INI BALANCENYA MAN");
        System.out.println(user.getUsername() + " INI UNAMENYA MAN");
        System.out.println(user.getName() + " INI NAMENYA MAN");
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("balance", Long.parseLong("5"));
        jsonBody.addProperty("username", user.getUsername());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/user/delete-account"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        System.out.println("Tanggal 9 Desember");
        if (response.body() == null) {

            return "user-profile";
        }
        return "redirect:/user/login";
    }
}
