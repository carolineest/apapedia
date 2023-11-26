package com.apapedia.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.apapedia.order.model.Cart;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCartItemRequestDTO {
    private UUID productId;
    private UUID cartId;
    private Integer quantity;
}
