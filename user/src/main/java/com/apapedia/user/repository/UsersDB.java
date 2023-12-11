package com.apapedia.user.repository;

import com.apapedia.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersDB extends JpaRepository<Users, UUID> {
    Users findByUsername(String name);
}
