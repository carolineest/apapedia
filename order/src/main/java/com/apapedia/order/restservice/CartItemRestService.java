package com.apapedia.order.restservice;

import com.apapedia.order.dto.request.CreateCartItemRequestDTO;

public interface CartItemRestService {
    void createCartItem(CreateCartItemRequestDTO cartItem);
}
