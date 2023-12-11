package com.apapedia.user.service;

import com.apapedia.user.DTO.request.LoginSsoReqDTO;
import com.apapedia.user.model.Users;

public interface UserService {

    Users updateWithdrawUser(Long amount, String name);

    Users updateTopUpUser(Long amount, String name);

    Users deleteUser(String name);
    void createUser(Users users);
    Users getUserByName(String name);
    Users updateUser(Users users);
    String loginSsoSeller(LoginSsoReqDTO loginRequestDTO);
}
