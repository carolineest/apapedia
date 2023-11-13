package com.apapedia.frontend.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class RegisterReqDTO {
    private String name;
    private String username;
    private String password;
    private String email;
    private String address;
}
