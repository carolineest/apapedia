package com.apapedia.user.model;

import java.util.Date;
import java.util.UUID;
import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    @NotNull
    @Column(name = "username", nullable = false)
    private String username;
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;
    @NotNull
    @Column(name = " email", nullable = false)
    private String email;
    @NotNull
    @Column(name = "balance", nullable = false)
    private Long balance;
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;
    @NotNull
    @Column(name = "createdAt", nullable = false)
    private Date createdAt;
    @NotNull
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;
}
