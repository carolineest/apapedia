package com.apapedia.frontend.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private UUID id = UUID.randomUUID();
    private String name;
    private String username;
    private String password;
    private String email;
    private Long balance;
    private String address;
    private Date createdAt;
    private Date updatedAt;
    private Boolean seller;
    private Boolean customer;
    private Boolean deleted = Boolean.FALSE;
}
