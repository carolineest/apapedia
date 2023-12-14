package com.apapedia.frontend.controller;

import com.apapedia.frontend.DTO.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    @GetMapping("/chart")
    public String chartOrder(HttpServletRequest httpServletRequest, Model model)
            throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);
        jwtToken = (String) session.getAttribute("token");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/order/chart"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> statusCountList = objectMapper.readValue(output1.body(), Map.class);

        model.addAttribute("statusCountList", statusCountList);
        return "order-chart";
    }

    @GetMapping("/updateStatus/{orderId}")
    public String formUpdateOrder(@PathVariable("orderId") UUID orderId,
            HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);
        jwtToken = (String) session.getAttribute("token");

        String encodedId = URLEncoder.encode(String.valueOf(orderId), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/order/updateStatus/" + encodedId))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        OrderDTO orderDTO = objectMapper.readValue(output1.body(), OrderDTO.class);

        List<Map.Entry<String, Integer>> orderStatus = new ArrayList<>();
        orderStatus.add(Map.entry("Menunggu konfirmasi penjual", 0));
        orderStatus.add(Map.entry("Dikonfirmasi penjual", 1));
        orderStatus.add(Map.entry("Menunggu kurir", 2));
        orderStatus.add(Map.entry("Dalam perjalanan", 3));
        orderStatus.add(Map.entry("Barang diterima", 4));
        orderStatus.add(Map.entry("Barang selesai", 5));

        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("orderStatus", orderStatus);

        return "form-update-order-status";
    }

    @PostMapping("/updateStatus")
    public String updateOrderStatus(@ModelAttribute OrderDTO orderDTO,
            HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);
        jwtToken = (String) session.getAttribute("token");

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("status", orderDTO.getStatus());


        String encodedId = URLEncoder.encode(String.valueOf(orderDTO.getId()), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/order/updateStatus/" + encodedId))
                .header("content-type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String orderHistory(HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);
        jwtToken = (String) session.getAttribute("token");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<OrderDTO>> response = restTemplate.exchange(
                "http://localhost:8081/api/order/history",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<OrderDTO>>() {
                });

        List<OrderDTO> listOrderDTO = response.getBody();

        model.addAttribute("listOrder", listOrderDTO);
        return "order-history";

    }

}
