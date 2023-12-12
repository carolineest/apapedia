package com.apapedia.frontend.controller;

import com.apapedia.frontend.DTO.CatalogueUpdateReqDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
    @GetMapping("/chart")
    public String chartOrder(HttpServletRequest httpServletRequest, Model model) throws IOException, InterruptedException {
        System.out.println("MASUK CHART 1");
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");

        //get data untuk chart
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/order/chart"))
                .header("Authorization", "Bearer "+jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1.body());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> statusCountList = objectMapper.readValue(output1.body(), Map.class);
        System.out.println(statusCountList);

        model.addAttribute("statusCountList", statusCountList);
        return "order-chart";
    }
}
