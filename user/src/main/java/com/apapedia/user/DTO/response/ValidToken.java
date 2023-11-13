package com.apapedia.user.DTO.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ValidToken {
    private String username;
    private String role;
}
