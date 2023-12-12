package com.apapedia.frontend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class OrderDTO {
    private UUID id;
    private Date createdAt;
    private Date updatedAt;
    private Integer status;
    private Integer totalPrice;
    private UUID customer;
    private UUID seller;
}
