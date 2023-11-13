package com.apapedia.frontend.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class LoginReqDTO {
    private String username;
    private String password;
}
