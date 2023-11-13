package com.apapedia.user.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class LoginRequestDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
