package com.apapedia.user.service;

import com.apapedia.user.DTO.request.LoginSsoReqDTO;
import com.apapedia.user.model.Users;

import java.util.UUID;

public interface UserService {

    Users updateWithdrawUser(Long amount, String name);

    Users updateTopUpUser(Long amount, String name);

    void createUser(Users users);

    Users getUserByName(String name);

    Users getUserById(UUID id);

    Users updateUser(UUID usersId, Users userFromDTO);

    String loginSsoSeller(LoginSsoReqDTO loginRequestDTO);

    Users deleteUser(UUID id);

    void hardDelete(UUID id);
}
