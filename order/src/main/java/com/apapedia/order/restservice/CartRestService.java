package com.apapedia.order.restservice;

import com.apapedia.order.model.Cart;

import java.util.UUID;

public interface CartRestService {
    Cart createCart(UUID userId);
}
