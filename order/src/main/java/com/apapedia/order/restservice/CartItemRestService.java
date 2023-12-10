package com.apapedia.order.restservice;

import java.util.UUID;

import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.model.Cart;

public interface CartItemRestService {
    void createCartItem(CreateCartItemRequestDTO cartItem);
    public Cart getCartById(UUID id);
}
