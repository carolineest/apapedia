package com.apapedia.user.controller;

import com.apapedia.user.DTO.UserMapper;
import com.apapedia.user.DTO.request.CreateUserDTO;
import com.apapedia.user.DTO.request.LoadUserReqDTO;
import com.apapedia.user.DTO.request.LoginRequestDTO;
import com.apapedia.user.DTO.request.LoginSsoReqDTO;
import com.apapedia.user.DTO.response.LoginResDTO;
import com.apapedia.user.DTO.response.ValidToken;
import com.apapedia.user.model.Users;
import com.apapedia.user.repository.UsersDB;
import com.apapedia.user.security.UserDetailServiceImpl;
import com.apapedia.user.service.AuthService;
import com.apapedia.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @PostMapping("/register")
    public Users register(@Valid @RequestBody CreateUserDTO userDTO, BindingResult bindingResult) {
        System.out.println("masuk POST REGISTER");
        LocalDateTime waktuSkrg = LocalDateTime.now();
        Long balance = 0L;
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            var user = userMapper.userDTOToUser(userDTO);
            user.setBalance(balance);
            user.setCreatedAt(Timestamp.valueOf(waktuSkrg));
            if (userDTO.getRole().equals("seller")) {
                user.setSeller(true);
                user.setCustomer(false);
            } else {
                user.setCustomer(true);
                user.setSeller(false);
            }
            userService.createUser(user);
            return user;
        }
    }

    @PostMapping("/login")
    public LoginResDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, BindingResult bindingResult) {
        System.out.println("masuk POST LOGIN");
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            //cek username exist or not
            Users users = userService.getUserByName(loginRequestDTO.getUsername());
            if(users ==null){
                return null;
            }
            //cek password
            if(!users.getPassword().equals(loginRequestDTO.getPassword())){
                return null;
            }
            if(users.getCustomer()){
                String token = authService.generateToken(users.getUsername(), "customer");
                LoginResDTO loginResDTO = new LoginResDTO(token);
                return loginResDTO;
            }
            if(users.getSeller()){
                String token = authService.generateToken(users.getUsername(), "seller");
                LoginResDTO loginResDTO = new LoginResDTO(token);
                return loginResDTO;
            }
            return null;
        }
    }

    @PostMapping("/login-sso")
    public ResponseEntity<?> loginSso(@Valid @RequestBody LoginSsoReqDTO loginSsoReqDTO, BindingResult bindingResult) {
        try {
            System.out.println("MASUK LOGIN SSO 1");
            String jwtToken = userService.loginSsoSeller(loginSsoReqDTO);
            if(jwtToken == null){
                System.out.println("MASUK LOGIN SSO 2");
                return new ResponseEntity<>(new LoginResDTO("None"), HttpStatus.OK);
            }
            System.out.println("MASUK LOGIN SSO 3");
            return new ResponseEntity<>(new LoginResDTO(jwtToken), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("MASUK LOGIN SSO 4");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/validate")
    public ValidToken validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("masuk GET Validate");
        String token = authorizationHeader.substring(7);
        System.out.println(token);
        var validToken = authService.validateToken(token);
        if (validToken != null) {
            System.out.println("Valid Token");
            return validToken;
        } else {
            System.out.println("Invalid Token");
            return null;
        }
    }

    @PostMapping("/loadUserByUsername")
    public UserDetails apiLoadUser(@RequestBody LoadUserReqDTO loadUserReqDTO){
        return userDetailsService.loadUserByUsername(loadUserReqDTO.getUsername());
    }
}
