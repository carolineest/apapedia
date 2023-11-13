package com.apapedia.user.controller;

import com.apapedia.user.DTO.UserMapper;
import com.apapedia.user.DTO.request.CreateUserDTO;
import com.apapedia.user.DTO.request.LoginRequestDTO;
import com.apapedia.user.DTO.response.LoginResDTO;
import com.apapedia.user.DTO.response.ValidToken;
import com.apapedia.user.model.User;
//import com.apapedia.user.service.AuthService;
import com.apapedia.user.service.AuthService;
import com.apapedia.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    //tinggal handle kalau misalkan usernamenya already exist
    @PostMapping("/register")
    public User register(@Valid @RequestBody CreateUserDTO userDTO, BindingResult bindingResult) {
        System.out.println("masuk POST REGISTER");
        LocalDateTime waktuSkrg = LocalDateTime.now();
        Long balance = 0L;
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            var user = userMapper.userDTOToUser(userDTO);
            user.setBalance(balance);
            user.setCreatedAt(Timestamp.valueOf(waktuSkrg));
            if(userDTO.getRole().equals("seller")){
                user.setSeller(true);
                user.setCustomer(false);
            }
            else{
                user.setCustomer(true);
                user.setSeller(false);
            }
            userService.createUser(user);
            return user;
        }
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, BindingResult bindingResult){
        System.out.println("masuk POST LOGIN");
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            //cek username exist or not
            User user = userService.getUserByName(loginRequestDTO.getUsername());
            if(user==null){
                return null;
            }
            //cek password
            if(!user.getPassword().equals(loginRequestDTO.getPassword())){
                return null;
            }
            if(user.getCustomer()){
                return authService.generateToken(user.getUsername(), "customer");
            }
            if(user.getSeller()){
                return authService.generateToken(user.getUsername(), "seller");
            }
            return null;
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
//    @PostMapping("/authenticate")
//    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
//            );
//        } catch (Exception ex) {
//            throw new Exception("inavalid username/password");
//        }
//        return jwtUtil.generateToken(authRequest.getUserName());
//    }
}
