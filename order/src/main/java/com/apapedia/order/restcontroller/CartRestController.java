package com.apapedia.order.restcontroller;

import com.apapedia.order.dto.request.CreateCartReqDTO;
import com.apapedia.order.model.Cart;
import com.apapedia.order.restservice.CartRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    CartRestService cartRestService;

    @PostMapping(value = "/add")
    public Cart addCart(@RequestBody CreateCartReqDTO createCartReqDTO,
                        BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            UUID userid = createCartReqDTO.getUserId();
            return cartRestService.createCart(userid);
        }
    }
}
