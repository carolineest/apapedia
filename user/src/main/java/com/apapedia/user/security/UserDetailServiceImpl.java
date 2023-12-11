package com.apapedia.user.security;

import com.apapedia.user.model.Users;
import com.apapedia.user.repository.UsersDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UsersDB usersDB;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersDB.findByUsername(username);
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet();
        if(users.getSeller()){
            grantedAuthoritySet.add(new SimpleGrantedAuthority("Seller"));
        }
        if(users.getCustomer()){
            grantedAuthoritySet.add(new SimpleGrantedAuthority("Customer"));
        }
        return new User(users.getUsername(), users.getPassword(), grantedAuthoritySet);
    }
}
