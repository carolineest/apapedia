package com.apapedia.order.restcontroller;

import com.apapedia.order.dto.request.UpdateCartItemReqDTO;
import com.apapedia.order.jwt.JwtUtils;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.order.dto.CartItemMapper;
import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.model.Cart_Item;
import com.apapedia.order.restservice.CartItemRestService;

import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/cartitem")
public class CartItemRestController {
    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private CartItemRestService cartItemRestService;

    @Autowired
    JwtUtils jwtUtils;

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

    @GetMapping("/update/{id}")
    public ResponseEntity<?> updateCartItemReq(@PathVariable("id") UUID id,
                                               @RequestHeader("Authorization") String authorizationHeader){
        try{
            //token gabutuh since cartitem selalu berbeda beda kan?
            String token = authorizationHeader.substring(7);
            UUID userid = jwtUtils.getUserIdFromToken(token);

            Cart_Item cartItem = cartItemRestService.getCartItemById(id);
            if(cartItem==null){
                return null;
            }
            return new ResponseEntity<>(cartItem, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?>  updateCartItem(@PathVariable("id") UUID id,
                                             @RequestBody UpdateCartItemReqDTO updateCartItemReqDTO){
        try{
            Integer newQuantity = updateCartItemReqDTO.getQuantity();
            Cart_Item updatedCartItem = cartItemRestService.updateQuantity(id, newQuantity);
            if(updatedCartItem == null){
                return null;
            }
            return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
