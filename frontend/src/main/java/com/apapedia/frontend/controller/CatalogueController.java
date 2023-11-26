package com.apapedia.frontend.controller;

import com.apapedia.frontend.DTO.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jdk.jfr.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/catalogue")
public class CatalogueController {
    @GetMapping("/update/{id}")
    public String updateCatalogue(@PathVariable("id") UUID id,
                                  HttpServletRequest httpServletRequest,
                                  Model model) throws IOException, InterruptedException{
//        // Retrieve cookies from the request
        Cookie[] cookies = httpServletRequest.getCookies();

        System.out.println(cookies);
        // Search for the "jwtToken" cookie
        String jwtToken = null;
        if (cookies == null) {
            return "Login";
        }
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            if ("jwtToken".equals(cookie.getName())) {
                jwtToken = cookie.getValue();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8082/user/validate"))
                        .header("Authorization", "Bearer "+jwtToken)
                        .GET()
                        .build();
                HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(output);
                System.out.println(output.body());
                if(output.body() == null) {
                    return "Login";
                }
                else{
                    break;
                }
            }
        }
        //Untuk get catalogue for update
        // Encode the id
        String encodedId = URLEncoder.encode(String.valueOf(id), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/catalogue/update/"+encodedId))
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1);
        System.out.println(output1.body());

        ObjectMapper objectMapper = new ObjectMapper();
        CatalogueUpdateReqDTO catalogueDTO = objectMapper.readValue(output1.body(), CatalogueUpdateReqDTO.class);
        catalogueDTO.setId(id);

        //Untuk get list of category
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/category/view-all"))
                .GET()
                .build();
        HttpResponse<String> output2 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
        System.out.println(output2);
        System.out.println("list of category: \n");
        System.out.println(output2.body());

        CategoryListAllDTO categoryList= objectMapper.readValue(output2.body(), CategoryListAllDTO.class);

        model.addAttribute("catalogueDTO", catalogueDTO);
        model.addAttribute("categoryList", categoryList);

        return "catalogue-update";
    }

    @PostMapping("/update")
    public String updateCatalogue(@ModelAttribute CatalogueUpdateResDTO catalogueDTO,
                                  HttpServletRequest httpServletRequest,
                                  Model model) throws IOException, InterruptedException{
        System.out.println(catalogueDTO.getCategoryId());
        // Retrieve cookies from the request
        Cookie[] cookies = httpServletRequest.getCookies();

        System.out.println(cookies);
        // Search for the "jwtToken" cookie
        String jwtToken = null;
        if (cookies == null) {
            return "Login";
        }
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            if ("jwtToken".equals(cookie.getName())) {
                jwtToken = cookie.getValue();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8082/user/validate"))
                        .header("Authorization", "Bearer "+jwtToken)
                        .GET()
                        .build();
                HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(output);
                System.out.println(output.body());
                if(output.body() == null) {
                    return "Login";
                }
                else{
                    break;
                }
            }
        }
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("productName", catalogueDTO.getProductName());
        jsonBody.addProperty("productDescription", catalogueDTO.getProductDescription());
        jsonBody.addProperty("price", catalogueDTO.getPrice());
        jsonBody.addProperty("stock", catalogueDTO.getStock());
        jsonBody.addProperty("categoryId", String.valueOf(catalogueDTO.getCategoryId()));
//        jsonBody.addProperty("image", catalogueDTO.getImage());

        System.out.println(jsonBody);

        //Untuk update catalogue
        String encodedId = URLEncoder.encode(String.valueOf(catalogueDTO.getId()), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/catalogue/update/"+encodedId))
                .header("content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1);
        System.out.println(output1.body());

        //arahin ke mana gitu
        if(output1.body() == null){
            return "Login";
        }
        return "catalogue-update-success";
    }
}
