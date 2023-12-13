package com.apapedia.user.controller;

import com.apapedia.user.DTO.UserMapper;
import com.apapedia.user.DTO.request.CreateUserDTO;
import com.apapedia.user.DTO.request.LoginRequestDTO;
import com.apapedia.user.DTO.request.WithdrawUserDTO;
import com.apapedia.user.DTO.request.LoginSsoReqDTO;
import com.apapedia.user.DTO.response.LoginResDTO;
import com.apapedia.user.DTO.response.ValidToken;
import com.apapedia.user.jwt.JwtUtils;
import com.apapedia.user.model.Users;
//import com.apapedia.user.service.AuthService;
import com.apapedia.user.service.AuthService;
import com.apapedia.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    @Autowired
    JwtUtils jwtUtils;

    // tinggal handle kalau misalkan usernamenya already exist
    @PostMapping("/register")
    public Users register(@Valid @RequestBody CreateUserDTO userDTO, BindingResult bindingResult) {
        System.out.println("masuk POST REGISTER");
        LocalDateTime waktuSkrg = LocalDateTime.now();
        Long balance = 5L;
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
            System.out.println("******************" + user);
            userService.createUser(user);
            return user;
        }
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, BindingResult bindingResult) {
        System.out.println("masuk POST LOGIN");
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            // cek username exist or not
            Users user = userService.getUserByName(loginRequestDTO.getUsername());
            if (user == null) {
                return null;
            }
            // cek password
            if (!user.getPassword().equals(loginRequestDTO.getPassword())) {
                return null;
            }
            if (user.getCustomer()) {
                return authService.generateToken(user.getUsername(), "customer");
            }
            if (user.getSeller()) {
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

    @GetMapping("/profile")
    public Users getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        System.out.println("MASUK REQUEST PROFILE USER");

        // yg bener
        UUID userid = jwtUtils.getUserIdFromToken(token);
        return userService.getUserById(userid);

        // yg salah
        // var validToken = authService.validateToken(token);
        // var user = userService.getUserByName(validToken.getUsername());
        // return user;
    }
    // @PostMapping("/authenticate")
    // public String generateToken(@RequestBody AuthRequest authRequest) throws
    // Exception {
    // try {
    // authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(authRequest.getUserName(),
    // authRequest.getPassword())
    // );
    // } catch (Exception ex) {
    // throw new Exception("inavalid username/password");
    // }
    // return jwtUtil.generateToken(authRequest.getUserName());
    // }

    @PostMapping("/edit-profile")
    public Users editProfile(@Valid @RequestBody CreateUserDTO userDTO, BindingResult bindingResult,
            @RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("masuk POST editProfile");
        // LocalDateTime waktuSkrg = LocalDateTime.now();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            String token = authorizationHeader.substring(7); // Menghapus "Bearer " dari header
            UUID userId = jwtUtils.getUserIdFromToken(token);
            // Users updatedUsers = userService.updateUser(userId);
            var user = userMapper.userDTOToUser(userDTO);
            // System.out.println(user.getUsername());
            // System.out.println("10 Desember 2023");
            // user.setUpdatedAt(Timestamp.valueOf(waktuSkrg));
            // Users updatedUsers = userService.updateUser(user);
            // System.out.println("ini controller");
            // System.out.println(updatedUsers.getEmail());

            return userService.updateUser(userId, user);
        }
    }

    @PostMapping("/withdraw")
    public Users withdraw(@Valid @RequestBody WithdrawUserDTO withdrawUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            System.out.println(withdrawUserDTO);
            Long withdrawAmount = Long.parseLong(withdrawUserDTO.getBalance());

            String id = withdrawUserDTO.getId();
            System.out.println(id);
            Users updatedUser = userService.updateWithdrawUser(withdrawAmount, id);

            System.out.println("AMAN KOMPETITOR");
            System.out.println(updatedUser);
            System.out.println(updatedUser.getBalance());
            return updatedUser;
        }
    }

    @PostMapping("/topup")
    public Users topup(@Valid @RequestBody WithdrawUserDTO withdrawUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            System.out.println(withdrawUserDTO);
            Long topupAmount = Long.parseLong(withdrawUserDTO.getBalance());

            String id = withdrawUserDTO.getId();
            System.out.println(id);
            Users updatedUser = userService.updateTopUpUser(topupAmount, id);

            System.out.println(updatedUser);
            System.out.println(updatedUser.getBalance());
            return updatedUser;
        }
    }

    @DeleteMapping("/delete-account")
    public Users deleteAccount(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        System.out.println("MASUK REQUEST DELETE USER");

        // yg bener
        UUID userid = jwtUtils.getUserIdFromToken(token);
        return userService.deleteUser(userid);
        // if (bindingResult.hasFieldErrors()) {
        // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has
        // invalid type or missing field");
        // } else {
        // System.out.println(withdrawUserDTO);
        // System.out.println("Tanggal 9 Desember");
        //
        // String id = withdrawUserDTO.getId();
        // System.out.println(id);
        // Users deletedUser = userService.deleteUser(id);
        //
        // System.out.println(deletedUser);
        // deletedUser.setDeleted(true);
        // System.out.println(deletedUser.getDeleted());
        // return deletedUser;
    }

    @DeleteMapping("/hard-delete")
    public String deleteHardAccount(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        System.out.println("MASUK REQUEST DELETE USER");

        // yg bener
        UUID userid = jwtUtils.getUserIdFromToken(token);
        System.out.println(userid);
        userService.hardDelete(userid);
        System.out.println("Berhasil HardDelete");
        return "userService.deleteUser(userid)";
    }

}