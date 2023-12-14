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
import jakarta.servlet.http.HttpServletRequest;
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

        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);
        jwtToken = (String) session.getAttribute("token");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<CatalogueDTO>> response = restTemplate.exchange(
                "http://localhost:8083/api/catalogue/seller",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<CatalogueDTO>>() {
                });

        List<CatalogueDTO> listCatalogueDTO = response.getBody();
        model.addAttribute("searchFilterDTO", new SearchFilterDTO());
        model.addAttribute("listCatalogueDTO", listCatalogueDTO);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/order/chart"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> statusCountList = objectMapper.readValue(output1.body(), Map.class);

        model.addAttribute("statusCountList", statusCountList);
        return "catalogue-logged";
    }

    @PostMapping("/viewAll")
    public String catalogViewAll(HttpServletRequest httpServletRequest,
            Model model, @ModelAttribute SearchFilterDTO searchFilterDTO) throws IOException, InterruptedException {

        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);

        if (session == null) {
            return "Register";
        }
        jwtToken = (String) session.getAttribute("token");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String productName;
        Integer minPrice;
        Integer maxPrice;
        String direction;
        String attribute;
        String apiUrl;

        if (searchFilterDTO.getProductName() != null) {
            productName = searchFilterDTO.getProductName();
            apiUrl = "http://localhost:8083/api/catalogue/login/name/" + productName;
        } else if (searchFilterDTO.getMinPrice() != null && searchFilterDTO.getMaxPrice() != null) {
            minPrice = searchFilterDTO.getMinPrice();
            maxPrice = searchFilterDTO.getMaxPrice();
            apiUrl = "http://localhost:8083/api/catalogue/price/" + minPrice + "/" + maxPrice;
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

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/order/chart"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> statusCountList = objectMapper.readValue(output1.body(), Map.class);

        model.addAttribute("statusCountList", statusCountList);
        model.addAttribute("searchFilterDTO", new SearchFilterDTO());
        model.addAttribute("listCatalogueDTO", listCatalogueDTO);
        return "catalogue-logged";
    }

    @GetMapping("/update/{id}")
    public String updateCatalogue(@PathVariable("id") UUID id,
            HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);
        jwtToken = (String) session.getAttribute("token");

        String encodedId = URLEncoder.encode(String.valueOf(id), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue/update/" + encodedId))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        CatalogueUpdateReqDTO catalogueDTO = objectMapper.readValue(output1.body(), CatalogueUpdateReqDTO.class);
        catalogueDTO.setId(id);

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/category/view-all"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output2 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());

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
        HttpSession session = httpServletRequest.getSession(false);
        jwtToken = (String) session.getAttribute("token");

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("productName", catalogueDTO.getProductName());
        jsonBody.addProperty("productDescription", catalogueDTO.getProductDescription());
        jsonBody.addProperty("price", catalogueDTO.getPrice());
        jsonBody.addProperty("stock", catalogueDTO.getStock());
        jsonBody.addProperty("categoryId", String.valueOf(catalogueDTO.getCategoryId()));
        jsonBody.addProperty("image", catalogueDTO.getImage());


        String encodedId = URLEncoder.encode(String.valueOf(catalogueDTO.getId()), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue/update/" + encodedId))
                .header("content-type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        return "redirect:/catalogue/viewAll";
    }

    @GetMapping("/delete/{id}")
    public String deleteCatalogue(@PathVariable("id") UUID id,
            HttpServletRequest httpServletRequest,
            Model model) throws IOException, InterruptedException {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);
        jwtToken = (String) session.getAttribute("token");

        String encodedId = URLEncoder.encode(String.valueOf(id), "UTF-8");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue/softdelete/" + encodedId))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        return "redirect:/catalogue/viewAll";
    }

    @GetMapping("/addProduct")
    public String formAddProduct(HttpServletRequest httpServletRequest,
                                 Model model)throws IOException, InterruptedException{
        AddCatalogueDTO newCatalogueDTO = new AddCatalogueDTO();

        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);
        jwtToken = (String) session.getAttribute("token");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/category/view-all"))
                .header("Authorization", "Bearer "+jwtToken)
                .GET()
                .build();
        HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        CategoryAllResDTO categoryList = objectMapper.readValue(output1.body(), CategoryAllResDTO.class);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("newCatalogueDTO", newCatalogueDTO);

        return "form-create-catalogue";
    }
    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute AddCatalogueDTO addCatalogueDTO,
                             HttpServletRequest httpServletRequest,
                             Model model)throws IOException, InterruptedException{
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false); // Mendapatkan sesi tanpa membuat yang baru jika
        jwtToken = (String) session.getAttribute("token");

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("price", addCatalogueDTO.getPrice());
        jsonBody.addProperty("productName", addCatalogueDTO.getProductName());
        jsonBody.addProperty("productDescription", addCatalogueDTO.getProductDescription());
        jsonBody.addProperty("categoryId", String.valueOf(addCatalogueDTO.getCategoryId()));
        jsonBody.addProperty("stock", addCatalogueDTO.getStock());
        jsonBody.addProperty("image", addCatalogueDTO.getImage());

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/catalogue/add-product"))
                .header("content-type", "application/json")
                .header("Authorization", "Bearer "+jwtToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> output2 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        return "redirect:/catalogue/viewAll";
    }
}
