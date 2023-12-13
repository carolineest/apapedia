package com.apapedia.order.restservice;

import com.apapedia.order.model.Cart;
import com.apapedia.order.repository.CartDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartRestServiceImpl implements CartRestService{
    @Autowired
    private CartDb cartDb;

    @Override
    public Cart createCart(UUID userId){
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalPrice(0);
        cartDb.save(cart);
        return cart;
    }

}
