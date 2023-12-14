package com.apapedia.frontend.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
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
import java.util.List;

@Controller
public class PageController {
    @Autowired
    UserService userService;

    private WebClient webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs()
                    .jaxb2Decoder(new Jaxb2XmlDecoder()))
            .build();

    @GetMapping("/")
    public String cataloguePage(HttpServletRequest httpServletRequest, Model model) {
        String jwtToken = null;
        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            if(session.getAttribute("token") != null){
                return "redirect:/catalogue/viewAll";
            }
        }
        jwtToken = (String) session.getAttribute("token");

        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "http://localhost:8083/api/catalogue/view-all";

        ResponseEntity<List<CatalogueDTO>> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CatalogueDTO>>() {
                });
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<CatalogueDTO> listProduct = responseEntity.getBody();
            model.addAttribute("listProduct", listProduct);

            SearchFilterDTO searchFilterDTO = new SearchFilterDTO();
            model.addAttribute("searchFilterDTO", searchFilterDTO);

            return "catalogue-not-logged";
        } else {
            return "error-page";
        }
    }

    @PostMapping("/")
    public String cataloguePageSearchFilter(HttpServletRequest httpServletRequest, Model model, @ModelAttribute SearchFilterDTO searchFilterDTO) {
        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            if(session.getAttribute("token") != null){
                return "redirect:/catalogue/viewAll";
            }
        }

        RestTemplate restTemplate = new RestTemplate();

        String productName;
        Integer minPrice;
        Integer maxPrice;
        String direction;
        String attribute;
        String apiUrl;

        if (searchFilterDTO.getProductName() != null) {
            productName = searchFilterDTO.getProductName();
            apiUrl = "http://localhost:8083/api/catalogue/name/" + productName;
        } else if (searchFilterDTO.getMinPrice() != null && searchFilterDTO.getMaxPrice() != null) {
            minPrice = searchFilterDTO.getMinPrice();
            maxPrice = searchFilterDTO.getMaxPrice();
            apiUrl = "http://localhost:8083/api/catalogue/price/" + minPrice + "/" + maxPrice;
        } else {
            direction = searchFilterDTO.getDirection();
            attribute = searchFilterDTO.getAttribute();
            apiUrl = "http://localhost:8083/api/catalogue/" + direction + "/" + attribute;
        }

        ResponseEntity<List<CatalogueDTO>> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CatalogueDTO>>() {
                });

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<CatalogueDTO> listProduct = responseEntity.getBody();
            model.addAttribute("listProduct", listProduct);
            model.addAttribute("searchFilterDTO", new SearchFilterDTO());
            return "catalogue-not-logged";
        } else {
            return "error-page";
        }
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

        if (token.equals("None") || token == "None") {
            return new ModelAndView("redirect:/register");
        }

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        httpSession.setAttribute("token", token);

        return new ModelAndView("redirect:/catalogue/viewAll");
    }

    @GetMapping("/login-sso")
    public ModelAndView loginSSO(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            if(session.getAttribute("token") != null){
                return new ModelAndView("redirect:/catalogue/viewAll");
            }
        }
        return new ModelAndView("redirect:" + Setting.SERVER_LOGIN + Setting.CLIENT_LOGIN);
    }

    @GetMapping("/logout-sso")
    public ModelAndView logoutSSO(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);

        if (httpSession != null) {
            httpSession.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            httpSession.removeAttribute("token");
            httpSession.invalidate();
        }
        return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
    }

    @PostMapping("/logout-sso")
    public ModelAndView logoutSSOPost(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);

        if (httpSession != null) {
            httpSession.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            httpSession.removeAttribute("token");
            httpSession.invalidate();
        }
        return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
    }

    @GetMapping("/login-success")
    public String loginSuccess() {
        return "LoginSuccess";
    }

    @GetMapping("/register")
    public String registerPage(HttpServletRequest httpServletRequest,Model model) {
        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            if(session.getAttribute("token") != null){
                return "redirect:/catalogue/viewAll";
            }
        }

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

        if (response1.body() == null) {
            return new ModelAndView("redirect:/register");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        UsersDTO usersDTO = objectMapper.readValue(response1.body(), UsersDTO.class);

        JsonObject jsonBody2 = new JsonObject();
        jsonBody2.addProperty("userId", String.valueOf(usersDTO.getId()));

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/cart/add"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody2.toString()))
                .build();
        HttpResponse<String> response2 = HttpClient.newHttpClient().send(request2,
                HttpResponse.BodyHandlers.ofString());

        if (response2.body() == null) {
            return new ModelAndView("redirect:/register");
        }

        return new ModelAndView("redirect:/login-sso");
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            if(session.getAttribute("token") != null){
                return "redirect:/catalogue/viewAll";
            }
        }

        var loginDTO = new LoginReqDTO();
        model.addAttribute("loginDTO", loginDTO);

        return "Login";
    }

    @PostMapping("/login")
    public String submitFormLogin(@RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            HttpServletResponse response,
            Model model) throws IOException, InterruptedException {

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("username", username);
        jsonBody.addProperty("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/user/login"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (output.body() != null) {
            Cookie cookie = new Cookie("jwtToken", output.body());
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setMaxAge(24 * 60 * 60); // 24 hours in seconds

            // Add the cookie to the response
            response.addCookie(cookie);
            return "LoginSuccess";
        }
        return "Login";
    }
}
