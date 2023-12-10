package com.apapedia.user.DTO.request;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class WithdrawUserDTO {
    @NotNull
    private String balance;
    private String username;
}
