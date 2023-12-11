package com.apapedia.user.service;

import com.apapedia.user.DTO.request.LoginSsoReqDTO;
import com.apapedia.user.jwt.JwtUtils;
import com.apapedia.user.model.Users;
import com.apapedia.user.repository.UsersDB;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Users user = repository.findByUserName(username);
//        return new org.springframework.security.core.userdetails.Users(user.getUserName(), user.getPassword(), new ArrayList<>());
//    }
    public Users updateUser(Users usersFromDTO){
        Users users = getUserByName(usersFromDTO.getName());
        if (users != null) {
            users.setName(usersFromDTO.getName());
            users.setPassword(usersFromDTO.getPassword());
            users.setEmail(usersFromDTO.getEmail());
            users.setUsername(usersFromDTO.getUsername());
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

    public User updateWithdrawUser(Long amount, String name) {
        User user = getUserByName(name);

        if (user != null) {
            Long sisaAmount = user.getBalance() - amount;
            user.setBalance(sisaAmount);
            userDB.save(user);
        }
        return user;
    }

    @Override
    public User updateTopUpUser(Long amount, String name) {
        User user = getUserByName(name);

        if (user != null) {
            Long sisaAmount = user.getBalance() + amount;
            user.setBalance(sisaAmount);
            userDB.save(user);
        }
        return user;
    }

    @Override
    public User deleteUser(String name) {
        User user = getUserByName(name);
        if (user != null) {
            user.setDeleted(true);
            userDB.save(user);
        }
        return user;
    }

}
