package com.apapedia.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
import java.util.Map;

@Controller
@RequestMapping("/catalogue")
public class CatalogueController {
    @GetMapping("")
    public String cataloguePage(HttpServletRequest httpServletRequest, Model model) throws IOException, InterruptedException {

        RestTemplate restTemplate = new RestTemplate();

        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); 
        if (session == null) {
            String apiUrl = "http://localhost:8083/api/catalogue/view-all";
            // Menggunakan ParameterizedTypeReference untuk mendapatkan List<CatalogueDTO>
            ResponseEntity<List<CatalogueDTO>> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CatalogueDTO>>() {}
            );
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<CatalogueDTO> listProduct = responseEntity.getBody();
                model.addAttribute("listProduct", listProduct);
                System.out.println("*****************" + listProduct);
                return "catalogue-not-logged";
            } else {
                return "error-page";
            }
        }
        
        jwtToken = (String) session.getAttribute("token");

        String apiUrl = "http://localhost:8083/api/catalogue/seller"; // ganti dengan seller
        // Menggunakan ParameterizedTypeReference untuk mendapatkan List<CatalogueDTO>
        ResponseEntity<List<CatalogueDTO>> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CatalogueDTO>>() {}
        );
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<CatalogueDTO> listProduct = responseEntity.getBody();
            model.addAttribute("listProduct", listProduct);
            System.out.println("*****************" + listProduct);

            //get data untuk chart
            HttpRequest request1 = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/order/chart"))
                    .GET()
                    .build();
            HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
            System.out.println(output1);
            System.out.println(output1.body());

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Integer> statusCountList = objectMapper.readValue(output1.body(), Map.class);

            model.addAttribute("statusCountList", statusCountList);
            return "catalogue-logged";
        } else {
            return "error-page";
        }
    }

//    @GetMapping("/update/{id}")
//    public String updateCatalogue(@PathVariable("id") UUID id,
//                                  HttpServletRequest httpServletRequest,
//                                  Model model) throws IOException, InterruptedException{
//        // Retrieve cookies from the request
//        Cookie[] cookies = httpServletRequest.getCookies();
//
//        System.out.println(cookies);
//        // Search for the "jwtToken" cookie
//        String jwtToken = null;
//        if (cookies == null) {
//            return "Login";
//        }
//        for (Cookie cookie : cookies) {
//            System.out.println(cookie.getName());
//            if ("jwtToken".equals(cookie.getName())) {
//                jwtToken = cookie.getValue();
//                HttpRequest request = HttpRequest.newBuilder()
//                        .uri(URI.create("http://localhost:8082/user/validate"))
//                        .header("Authorization", "Bearer "+jwtToken)
//                        .GET()
//                        .build();
//                HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//                System.out.println(output);
//                System.out.println(output.body());
//                if(output.body() == null) {
//                    return "Login";
//                }
//                else{
//                    break;
//                }
//            }
//        }
//        //Untuk get catalogue for update
//        // Encode the id
//        String encodedId = URLEncoder.encode(String.valueOf(id), "UTF-8");
//
//        HttpRequest request1 = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8080/api/catalogue/update/"+encodedId))
//                .GET()
//                .build();
//        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
//        System.out.println(output1);
//        System.out.println(output1.body());
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        CatalogueUpdateReqDTO catalogueDTO = objectMapper.readValue(output1.body(), CatalogueUpdateReqDTO.class);
//        catalogueDTO.setId(id);
//
//        //Untuk get list of category
//        HttpRequest request2 = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8080/api/category/view-all"))
//                .GET()
//                .build();
//        HttpResponse<String> output2 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
//        System.out.println(output2);
//        System.out.println("list of category: \n");
//        System.out.println(output2.body());
//
//        CategoryListAllDTO categoryList= objectMapper.readValue(output2.body(), CategoryListAllDTO.class);
//
//        model.addAttribute("catalogueDTO", catalogueDTO);
//        model.addAttribute("categoryList", categoryList);
//
//        return "catalogue-update";
//    }

    @GetMapping("/update/{id}")
    public String updateCatalogue(@PathVariable("id") UUID id,
                                  HttpServletRequest httpServletRequest,
                                  Model model) throws IOException, InterruptedException{
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");

        String encodedId = URLEncoder.encode(String.valueOf(id), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue/update/"+encodedId))
                .header("Authorization", "Bearer "+jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1.body());

        ObjectMapper objectMapper = new ObjectMapper();
        CatalogueUpdateReqDTO catalogueDTO = objectMapper.readValue(output1.body(), CatalogueUpdateReqDTO.class);
        catalogueDTO.setId(id);

        //Untuk get list of category
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/category/view-all"))
                .header("Authorization", "Bearer "+jwtToken)
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
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");

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
                .uri(URI.create("http://localhost:8083/api/catalogue/update/"+encodedId))
                .header("content-type", "application/json")
                .header("Authorization", "Bearer "+jwtToken)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1.body());

        //arahin ke mana gitu
        if(output1.body() == null){
            return "Login";
        }
        return "catalogue-update-success";
    }

//    @PostMapping("/update")
//    public String updateCatalogue(@ModelAttribute CatalogueUpdateResDTO catalogueDTO,
//                                  HttpServletRequest httpServletRequest,
//                                  Model model) throws IOException, InterruptedException{
//        System.out.println(catalogueDTO.getCategoryId());
//        // Retrieve cookies from the request
//        Cookie[] cookies = httpServletRequest.getCookies();
//
//        System.out.println(cookies);
//        // Search for the "jwtToken" cookie
//        String jwtToken = null;
//        if (cookies == null) {
//            return "Login";
//        }
//        for (Cookie cookie : cookies) {
//            System.out.println(cookie.getName());
//            if ("jwtToken".equals(cookie.getName())) {
//                jwtToken = cookie.getValue();
//                HttpRequest request = HttpRequest.newBuilder()
//                        .uri(URI.create("http://localhost:8082/user/validate"))
//                        .header("Authorization", "Bearer "+jwtToken)
//                        .GET()
//                        .build();
//                HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//                System.out.println(output);
//                System.out.println(output.body());
//                if(output.body() == null) {
//                    return "Login";
//                }
//                else{
//                    break;
//                }
//            }
//        }
//        JsonObject jsonBody = new JsonObject();
//        jsonBody.addProperty("productName", catalogueDTO.getProductName());
//        jsonBody.addProperty("productDescription", catalogueDTO.getProductDescription());
//        jsonBody.addProperty("price", catalogueDTO.getPrice());
//        jsonBody.addProperty("stock", catalogueDTO.getStock());
//        jsonBody.addProperty("categoryId", String.valueOf(catalogueDTO.getCategoryId()));
////        jsonBody.addProperty("image", catalogueDTO.getImage());
//
//        System.out.println(jsonBody);
//
//        //Untuk update catalogue
//        String encodedId = URLEncoder.encode(String.valueOf(catalogueDTO.getId()), "UTF-8");
//
//        HttpRequest request1 = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8080/api/catalogue/update/"+encodedId))
//                .header("content-type", "application/json")
//                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
//                .build();
//        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
//        System.out.println(output1);
//        System.out.println(output1.body());
//
//        //arahin ke mana gitu
//        if(output1.body() == null){
//            return "Login";
//        }
//        return "catalogue-update-success";
//    }

    @GetMapping("/delete/{id}")
    public String deleteCatalogue(@PathVariable("id") UUID id,
                                  HttpServletRequest httpServletRequest,
                                  Model model) throws IOException, InterruptedException{
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");

        String encodedId = URLEncoder.encode(String.valueOf(id), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue/softdelete/"+encodedId))
                .header("Authorization", "Bearer "+jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1.body());

        //arahin ke mana gitu
        if(output1.body() == null){
            return "Login";
        }
        return "catalogue-update-success";
    }
}
