package com.apapedia.frontend.controller;

import com.apapedia.frontend.DTO.RegisterReqDTO;
import com.apapedia.frontend.DTO.ProfileResDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @PostMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        request.getSession().invalidate();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        return "redirect:/user/login";
    }

    @GetMapping("/tryJwtToken")
    public String tryJwtToken(HttpServletRequest httpServletRequest, Model model)
            throws IOException, InterruptedException {
        Cookie[] cookies = httpServletRequest.getCookies();

        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8082/user/validate"))
                            .header("Authorization", "Bearer " + jwtToken)
                            .GET()
                            .build();
                    HttpResponse<String> output = HttpClient.newHttpClient().send(request,
                            HttpResponse.BodyHandlers.ofString());
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

        HttpSession session = httpServletRequest.getSession(false);

        if (session == null) {
            return "Register";
        }

        String jwtToken = null;
        jwtToken = (String) session.getAttribute("token");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/user/profile"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> output = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());


        ObjectMapper objectMapper = new ObjectMapper();
        ProfileResDTO user = objectMapper.readValue(output.body(),
                ProfileResDTO.class);

        model.addAttribute("user", user);
        return "user-profile";
    }

    @GetMapping("/edit-profile")
    public String editProfilePage(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String address, Model model) {
        RegisterReqDTO registerDTO = new RegisterReqDTO();
        model.addAttribute("registerDTO", registerDTO);
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("address", address);
        return "user-edit-profile";
    }

    @PostMapping("/edit-profile")
    public String submitFormEditProfile(@RequestParam(name = "name") String name,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "address") String address,
            Model model, HttpServletRequest request) throws IOException, InterruptedException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        String jwtToken = (String) session.getAttribute("token");

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("name", name);
        jsonBody.addProperty("username", username);
        jsonBody.addProperty("password", password);
        jsonBody.addProperty("email", email);
        jsonBody.addProperty("address", address);
        jsonBody.addProperty("role", "seller");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/user/edit-profile"))
                .header("content-type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        if (response.body() == null) {
            return "user-edit-profile";
        }
        return "redirect:/user/profile";
    }

    @GetMapping("/withdraw")
    public String withdrawPage(HttpServletRequest httpServletRequest, Model model)
            throws IOException, InterruptedException {
        HttpSession session = httpServletRequest.getSession(false);


        if (session == null) {
            return "Register";
        }
        String jwtToken = null;
        jwtToken = (String) session.getAttribute("token");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/user/profile"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        ProfileResDTO user = objectMapper.readValue(output.body(),
                ProfileResDTO.class);

        model.addAttribute("user", user);
        return "user-withdraw";
    }

    @PostMapping("withdraw")
    public String submitWithdrawPage(@ModelAttribute ProfileResDTO user, Model model)
            throws IOException, InterruptedException {
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("balance", user.getBalance());
        jsonBody.addProperty("id", user.getId().toString());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/user/withdraw"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.body() == null) {

            return "user-withdraw";
        }
        return "redirect:/user/profile";
    }

    @PostMapping("delete-account")
    public String deleteAccount(HttpServletRequest httpServletRequest, Model model)
            throws IOException, InterruptedException {
        HttpSession session = httpServletRequest.getSession(false);


        if (session == null) {
            return "Register";
        }

        String jwtToken = null;
        jwtToken = (String) session.getAttribute("token");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/user/delete-account"))
                .header("Authorization", "Bearer " + jwtToken)
                .DELETE()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return "redirect:/logout-sso";
    }
}
