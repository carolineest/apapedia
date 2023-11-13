package com.apapedia.user.service;

import com.apapedia.user.model.User;

public interface UserService {
    void createUser(User user);
    User getUserByName(String name);
    User updateUser(User user);
}
