package com.apapedia.order.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.order.dto.CartItemMapper;
import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.model.Cart_Item;
import com.apapedia.order.restservice.CartItemRestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cartitem")
public class CartItemRestController {
    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private CartItemRestService cartItemRestService;

    @PostMapping(value = "/add")
    public void addCartItem(@Valid @RequestBody CreateCartItemRequestDTO cartItemDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            cartItemRestService.createCartItem(cartItemDTO);
        }
    }
}
