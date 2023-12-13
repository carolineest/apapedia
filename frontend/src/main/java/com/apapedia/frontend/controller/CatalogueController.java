package com.apapedia.frontend.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

@Controller
@RequestMapping("/catalogue")
public class CatalogueController {

    @GetMapping("/viewAll")
    public String catalogViewAll(HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        System.out.println("MASUK GET VIEWALL 1");

        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
                                                                    // tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");
        System.out.println(jwtToken);

        // Menyiapkan header dengan menyertakan token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        // Menyiapkan HttpEntity dengan header
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<CatalogueDTO>> response = restTemplate.exchange(
                "http://localhost:8083/api/catalogue/seller",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<CatalogueDTO>>() {
                });

        List<CatalogueDTO> listCatalogueDTO = response.getBody();
        System.out.println(listCatalogueDTO);
        System.out.println("masuk liscatalog");
        model.addAttribute("searchFilterDTO", new SearchFilterDTO());
        model.addAttribute("listCatalogueDTO", listCatalogueDTO);
        return "catalogue-logged";
    }

    @PostMapping("/viewAll")
    public String catalogViewAll(HttpServletRequest httpServletRequest,
            Model model, @ModelAttribute SearchFilterDTO searchFilterDTO) throws IOException, InterruptedException {
        System.out.println("MASUK GET VIEWALL 1");

        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
                                                                    // tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");
        System.out.println(jwtToken);

        // Menyiapkan header dengan menyertakan token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        // Menyiapkan HttpEntity dengan header
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String productName;
        Integer minPrice;
        Integer maxPrice;
        String direction;
        String attribute;
        String apiUrl;

        if (searchFilterDTO.getProductName() != null) { // handle seller
            productName = searchFilterDTO.getProductName();
            apiUrl = "http://localhost:8083/api/catalogue/name" + productName;
        } else if (searchFilterDTO.getMinPrice() != null && searchFilterDTO.getMaxPrice() != null) {
            minPrice = searchFilterDTO.getMinPrice();
            maxPrice = searchFilterDTO.getMaxPrice();
            apiUrl = "http://localhost:8083/api/catalogue/price" + minPrice + "/" + maxPrice;
        } else {
            direction = searchFilterDTO.getDirection();
            attribute = searchFilterDTO.getAttribute();
            apiUrl = "http://localhost:8083/api/catalogue/" + direction + "/" + attribute;
        }

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<CatalogueDTO>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<CatalogueDTO>>() {
                });

        List<CatalogueDTO> listCatalogueDTO = response.getBody();
        System.out.println(listCatalogueDTO);
        System.out.println("masuk liscatalog");
        model.addAttribute("searchFilterDTO", new SearchFilterDTO());
        model.addAttribute("listCatalogueDTO", listCatalogueDTO);
        return "catalogue-logged";
    }

    @GetMapping("/update/{id}")
    public String updateCatalogue(@PathVariable("id") UUID id,
            HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
                                                                    // tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");

        String encodedId = URLEncoder.encode(String.valueOf(id), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue/update/" + encodedId))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1.body());

        ObjectMapper objectMapper = new ObjectMapper();
        CatalogueUpdateReqDTO catalogueDTO = objectMapper.readValue(output1.body(), CatalogueUpdateReqDTO.class);
        catalogueDTO.setId(id);

        // Untuk get list of category
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/category/view-all"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output2 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
        System.out.println(output2);
        System.out.println("list of category: \n");
        System.out.println(output2.body());

        CategoryListAllDTO categoryList = objectMapper.readValue(output2.body(), CategoryListAllDTO.class);

        model.addAttribute("catalogueDTO", catalogueDTO);
        model.addAttribute("categoryList", categoryList);

        return "catalogue-update";
    }

    @PostMapping("/update")
    public String updateCatalogue(@ModelAttribute CatalogueUpdateResDTO catalogueDTO,
            HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
                                                                    // tidak ada
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
        // jsonBody.addProperty("image", catalogueDTO.getImage());

        System.out.println(jsonBody);

        // Untuk update catalogue
        String encodedId = URLEncoder.encode(String.valueOf(catalogueDTO.getId()), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue/update/" + encodedId))
                .header("content-type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1.body());

        // arahin ke mana gitu
        if (output1.body() == null) {
            return "Login";
        }
        return "redirect:/catalogue/viewAll";
    }

    @GetMapping("/delete/{id}")
    public String deleteCatalogue(@PathVariable("id") UUID id,
            HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
                                                                    // tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");

        String encodedId = URLEncoder.encode(String.valueOf(id), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue/softdelete/" + encodedId))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1.body());

        // arahin ke mana gitu
        if (output1.body() == null) {
            return "Login";
        }
        return "catalogue-update-success";
    }

    @GetMapping("/addProduct")
    public String formAddProduct(HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        AddCatalogueDTO newCatalogueDTO = new AddCatalogueDTO();

        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
                                                                    // tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/category/view-all"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(output1.body());

        ObjectMapper objectMapper = new ObjectMapper();
        CategoryAllResDTO categoryList = objectMapper.readValue(output1.body(), CategoryAllResDTO.class);
        System.out.println(categoryList);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("newCatalogueDTO", newCatalogueDTO);

        return "form-create-catalogue";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute AddCatalogueDTO addCatalogueDTO,
            HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
        // tidak ada
        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");
        System.out.println(jwtToken);

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("price", addCatalogueDTO.getPrice());
        jsonBody.addProperty("productName", addCatalogueDTO.getProductName());
        jsonBody.addProperty("productDescription", addCatalogueDTO.getProductDescription());
        jsonBody.addProperty("categoryId", String.valueOf(addCatalogueDTO.getCategoryId()));
        jsonBody.addProperty("stock", addCatalogueDTO.getStock());
        jsonBody.addProperty("image", addCatalogueDTO.getImage());

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue.add-product"))
                .header("content-type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();

        // ganti html hrsnya redirect
        return "form-update-order-status";
    }
}
