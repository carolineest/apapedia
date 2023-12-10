package com.apapedia.frontend.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ProfileResDTO {
    private UUID id;

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

    private Boolean deleted;

}
