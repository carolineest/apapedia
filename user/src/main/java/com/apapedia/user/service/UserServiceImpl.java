package com.apapedia.user.service;

import com.apapedia.user.DTO.request.LoginSsoReqDTO;
import com.apapedia.user.jwt.JwtUtils;
import com.apapedia.user.model.Users;
import com.apapedia.user.repository.UsersDB;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UsersDB usersDB;
    @Autowired
    JwtUtils jwtUtils;

    public void createUser(Users users){
        usersDB.save(users);
    }
    public Users getUserByName(String name){
        return usersDB.findByUsername(name);
    }
    public Users getUserById(UUID id){
        return usersDB.findById(id).get();
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Users user = repository.findByUserName(username);
//        return new org.springframework.security.core.userdetails.Users(user.getUserName(), user.getPassword(), new ArrayList<>());
//    }
    public Users updateUser(UUID usersId, Users usersFromDTO){
        Users users = getUserById(usersId);
        if (users != null) {
            LocalDateTime waktuSkrg = LocalDateTime.now();
            users.setUpdatedAt(Timestamp.valueOf(waktuSkrg));
            users.setName(usersFromDTO.getName());
            users.setEmail(usersFromDTO.getEmail());
            users.setAddress(usersFromDTO.getAddress());
            usersDB.save(users);
        }
        return users;
    }

    @Override
    public String loginSsoSeller(LoginSsoReqDTO loginSsoReqDTO){
        String username = loginSsoReqDTO.getUsername();
        String name = loginSsoReqDTO.getName();
        Users users = usersDB.findByUsername(username);
        if (users == null) {
            return null;
        }
        String token = jwtUtils.generateJwtToken(username, "Seller", users.getId());
        return token;
    }

    @Override
    public Users updateWithdrawUser(Long amount, String id) {
        Users user = getUserById(UUID.fromString(id));

        if (user != null) {
            Long sisaAmount = user.getBalance() - amount;
            user.setBalance(sisaAmount);
            usersDB.save(user);
        }
        return user;
    }

    @Override
    public Users updateTopUpUser(Long amount, String id) {
        Users user = getUserById(UUID.fromString(id));

        if (user != null) {
            Long sisaAmount = user.getBalance() + amount;
            user.setBalance(sisaAmount);
            usersDB.save(user);
        }
        return user;
    }

    @Override
    public Users deleteUser(String id) {
        Users user = getUserById(UUID.fromString(id));
        if (user != null) {
            user.setDeleted(true);
            usersDB.save(user);
        }
        return user;
    }

}
