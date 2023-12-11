package com.apapedia.frontend.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class LoginSsoReqDTO {
    private String username;
    private String name;
}
