package com.apapedia.order.restservice;

import java.util.UUID;

import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.Cart_Item;

public interface CartItemRestService {
    void createCartItem(CreateCartItemRequestDTO cartItem);
    Cart getCartById(UUID id);
    Cart_Item getCartItemById(UUID id);

    Cart_Item updateQuantity(UUID id, Integer q);
}
