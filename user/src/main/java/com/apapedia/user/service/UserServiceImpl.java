package com.apapedia.user.service;

import com.apapedia.user.model.User;
import com.apapedia.user.repository.UserDB;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    UserDB userDB;

    public void createUser(User user){
        userDB.save(user);
    }
    public User getUserByName(String name){
        return userDB.findByUsername(name);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = repository.findByUserName(username);
//        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());
//    }
}
