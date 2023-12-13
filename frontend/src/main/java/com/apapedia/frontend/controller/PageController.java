package com.apapedia.frontend.controller;

import com.apapedia.frontend.DTO.ListCatalogueDTO;
import com.apapedia.frontend.DTO.LoginReqDTO;
import com.apapedia.frontend.DTO.RegisterReqDTO;
import com.apapedia.frontend.DTO.*;
import com.apapedia.frontend.security.xml.Attributes;
import com.apapedia.frontend.security.xml.ServiceResponse;
import com.apapedia.frontend.services.UserService;
import com.apapedia.frontend.setting.Setting;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class PageController {
    @Autowired
    UserService userService;

    private WebClient webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs()
                    .jaxb2Decoder(new Jaxb2XmlDecoder()))
            .build();

    @GetMapping("/")
    // public String cataloguePage(HttpServletRequest httpServletRequest, Model
    // model) throws IOException, InterruptedException { //request param buat id
    // seller?
    public String cataloguePage(Model model) { // request param buat id seller?
        System.out.println("MASUK GET / 1");
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "http://localhost:8083/api/catalogue/view-all";

        // Menggunakan ParameterizedTypeReference untuk mendapatkan List<CatalogueDTO>
        ResponseEntity<List<CatalogueDTO>> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CatalogueDTO>>() {
                });

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("MASUK GET / 2");
            List<CatalogueDTO> listProduct = responseEntity.getBody();
            model.addAttribute("listProduct", listProduct);
            System.out.println("*****************" + listProduct);
            return "catalogue-not-logged";
        } else {
            return "error-page";
        }

        // HttpSession session = httpServletRequest.getSession(false);
        // String jwtToken = null;
        // jwtToken = (String) session.getAttribute("token");

        // HttpRequest request = HttpRequest.newBuilder()
        // .uri(URI.create("http://localhost:8083/api/catalogue/view-all"))
        // .GET()
        // .build();
        // HttpResponse<String> response = HttpClient.newHttpClient().send(request,
        // HttpResponse.BodyHandlers.ofString());
        // System.out.println("Output body: " + response.body());
        // ObjectMapper objectMapper = new ObjectMapper();
        // ListCatalogueDTO listCatalogue = objectMapper.readValue(response.body(),
        // ListCatalogueDTO.class);
        // System.out.println(listCatalogue);
        // model.addAttribute("listCatalogue", listCatalogue); // ganti akses di html
        // return "catalogue-not-logged";
        // }

        // @GetMapping("/")
        // public String cataloguePage(HttpServletRequest httpServletRequest, Model
        // model) throws IOException, InterruptedException {
        // System.out.println("**************** MASUK *************");
        // HttpSession session = httpServletRequest.getSession(false);
        // System.out.println("INI SESSION ******************* " + session);
        // if (session == null) {
        // // String apiUrl = "http://localhost:8083/api/catalogue/view-all";
        // // // Menggunakan ParameterizedTypeReference untuk mendapatkan
        // List<CatalogueDTO>
        // // ResponseEntity<List<CatalogueDTO>> responseEntity = restTemplate.exchange(
        // // apiUrl,
        // // HttpMethod.GET,
        // // null,
        // // new ParameterizedTypeReference<List<CatalogueDTO>>() {}
        // // );
        // // if (responseEntity.getStatusCode().is2xxSuccessful()) {
        // // List<CatalogueDTO> listProduct = responseEntity.getBody();
        // // model.addAttribute("listProduct", listProduct);
        // // System.out.println("*****************" + listProduct);
        // // return "catalogue-not-logged";
        // // } else {
        // // return "error-page";
        // // }
        // String jwtToken = null;
        // jwtToken = (String) session.getAttribute("token");

        // HttpRequest request = HttpRequest.newBuilder()
        // .uri(URI.create("http://localhost:8083/api/catalogue/view-all"))
        // .header("Authorization", "Bearer " + jwtToken)
        // .GET()
        // .build();
        // HttpResponse<String> response = HttpClient.newHttpClient().send(request,
        // HttpResponse.BodyHandlers.ofString());
        // ObjectMapper objectMapper = new ObjectMapper();
        // ListCatalogueDTO listCatalogue = objectMapper.readValue(response.body(),
        // ListCatalogueDTO.class);
        // model.addAttribute("listCatalogue", listCatalogue); // ganti akses di html
        // return "catalogue-not-logged";
        // }

        // String jwtToken = null;
        // jwtToken = (String) session.getAttribute("token");

        // HttpRequest request = HttpRequest.newBuilder()
        // .uri(URI.create("http://localhost:8083/api/catalogue/seller"))
        // .header("Authorization", "Bearer " + jwtToken)
        // .GET()
        // .build();
        // HttpResponse<String> response = HttpClient.newHttpClient().send(request,
        // HttpResponse.BodyHandlers.ofString());
        // ObjectMapper objectMapper = new ObjectMapper();
        // ListCatalogueDTO listCatalogue = objectMapper.readValue(response.body(),
        // ListCatalogueDTO.class);
        // model.addAttribute("listCatalogue", listCatalogue);

        // //get data untuk chart
        // HttpRequest request1 = HttpRequest.newBuilder()
        // .uri(URI.create("http://localhost:8081/order/chart"))
        // .GET()
        // .build();
        // HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1,
        // HttpResponse.BodyHandlers.ofString());
        // System.out.println(output1);
        // System.out.println(output1.body());

        // ObjectMapper objectMapper1 = new ObjectMapper();
        // Map<String, Integer> statusCountList =
        // objectMapper1.readValue(output1.body(), Map.class);

        // model.addAttribute("statusCountList", statusCountList);

        // // // Create an HttpEntity with headers
        // // HttpEntity<?> entity = new HttpEntity<>(headers);
        // // System.out.println("***********JWT Token: " + jwtToken);

        // // String apiUrl = "http://localhost:8083/api/catalogue/seller";
        // // // Menggunakan ParameterizedTypeReference untuk mendapatkan
        // List<CatalogueDTO>
        // // ResponseEntity<List<CatalogueDTO>> responseEntity = restTemplate.exchange(
        // // apiUrl,
        // // HttpMethod.GET,
        // // entity,
        // // new ParameterizedTypeReference<List<CatalogueDTO>>() {}
        // // );
        // // if (responseEntity.getStatusCode().is2xxSuccessful()) {
        // // List<CatalogueDTO> listProduct = responseEntity.getBody();

        // // model.addAttribute("listProduct", listProduct);
        // // System.out.println("*****************" + listProduct);

        // // //get data untuk chart
        // // HttpRequest request1 = HttpRequest.newBuilder()
        // // .uri(URI.create("http://localhost:8081/order/chart"))
        // // .GET()
        // // .build();
        // // HttpResponse<String> output1 = HttpClient.newHttpClient().send(request1,
        // HttpResponse.BodyHandlers.ofString());
        // // System.out.println(output1);
        // // System.out.println(output1.body());

        // // ObjectMapper objectMapper = new ObjectMapper();
        // // Map<String, Integer> statusCountList =
        // objectMapper.readValue(output1.body(), Map.class);

        // // model.addAttribute("statusCountList", statusCountList);
        // return "catalogue-logged";
        // // } else {
        // // return "error-page";
        // // }
    }

    @GetMapping("/validate-ticket")
    public ModelAndView adminLoginSSO(
            @RequestParam(value = "ticket", required = false) String ticket,
            HttpServletRequest request) {
        ServiceResponse serviceResponse = this.webClient.get().uri(
                String.format(
                        Setting.SERVER_VALIDATE_TICKET,
                        ticket,
                        Setting.CLIENT_LOGIN))
                .retrieve().bodyToMono(ServiceResponse.class).block();

        Attributes attributes = serviceResponse.getAuthenticationSuccess().getAttributes();
        String username = serviceResponse.getAuthenticationSuccess().getUser();

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "webadmin", null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        String name = attributes.getNama();
        var token = userService.getToken(username, name);
        System.out.println(token);

        if (token.equals("None") || token == "None") {
            return new ModelAndView("redirect:/register");
        }

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        httpSession.setAttribute("token", token);

        // ganti ke catalogue page yg non login!!
        return new ModelAndView("redirect:/login-success");
    }

    @GetMapping("/login-sso")
    public ModelAndView loginSSO() {
        System.out.println("");
        return new ModelAndView("redirect:" + Setting.SERVER_LOGIN + Setting.CLIENT_LOGIN);
    }

    @GetMapping("/logout-sso")
    public ModelAndView logoutSSO(HttpServletRequest request) {
        System.out.println("MASUK GET LOGOUT SSO");
        HttpSession httpSession = request.getSession(false); // Mengambil session yang sudah ada (tanpa membuat yang
                                                             // baru)
        if (httpSession != null) {
            httpSession.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            httpSession.removeAttribute("token");
            httpSession.invalidate(); // Menghapus seluruh session
        }
        return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
    }

    @PostMapping("/logout-sso")
    public ModelAndView logoutSSOPost(HttpServletRequest request) {
        System.out.println("MASUK GET LOGOUT SSO");
        HttpSession httpSession = request.getSession(false); // Mengambil session yang sudah ada (tanpa membuat yang
                                                             // baru)
        if (httpSession != null) {
            httpSession.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            httpSession.removeAttribute("token");
            httpSession.invalidate(); // Menghapus seluruh session
        }
        return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
    }

    // hapus aja kalo udh gabutuh
    @GetMapping("/login-success")
    public String loginSuccess() {
        return "LoginSuccess";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        System.out.println("MASUK REGISTER");
        RegisterReqDTO registerDTO = new RegisterReqDTO();
        model.addAttribute("registerDTO", registerDTO);
        return "Register";
    }

    @PostMapping("/register")
    public ModelAndView submitFormRegister(@RequestParam(name = "name") String name,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "address") String address,
            Model model) throws IOException, InterruptedException {
        // add user
        JsonObject jsonBody1 = new JsonObject();
        jsonBody1.addProperty("name", name);
        jsonBody1.addProperty("username", username);
        jsonBody1.addProperty("password", password);
        jsonBody1.addProperty("email", email);
        jsonBody1.addProperty("address", address);
        jsonBody1.addProperty("role", "seller");

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/api/auth/register"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody1.toString()))
                .build();
        HttpResponse<String> response1 = HttpClient.newHttpClient().send(request1,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response1.body());
        if (response1.body() == null) {
            return new ModelAndView("redirect:/register");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        UsersDTO usersDTO = objectMapper.readValue(response1.body(), UsersDTO.class);

        // add cart
        JsonObject jsonBody2 = new JsonObject();
        jsonBody2.addProperty("userId", String.valueOf(usersDTO.getId()));

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/cart/add"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody2.toString()))
                .build();
        HttpResponse<String> response2 = HttpClient.newHttpClient().send(request2,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response2.body());
        if (response2.body() == null) {
            return new ModelAndView("redirect:/register");
        }

        return new ModelAndView("redirect:/login-sso");
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
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
}
