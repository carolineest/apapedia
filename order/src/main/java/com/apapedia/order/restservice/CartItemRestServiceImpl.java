package com.apapedia.order.restservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;

import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.dto.response.CatalogueResponseDTO;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.Cart_Item;
import com.apapedia.order.repository.CartDb;
import com.apapedia.order.repository.CartItemDb;

@Service
@Transactional
public class CartItemRestServiceImpl implements CartItemRestService {
    @Autowired
    private CartItemDb cartItemDb;

    @Autowired
    private CartDb cartDb;

    @Override
    public Cart getCartById(UUID id){
        for (Cart cart : cartDb.findAll()){
            if (cart.getId().equals(id)){
                return cart;
            }
        }
        return null;
    }

    @Override
    public Cart_Item getCartItemById(UUID id){
        for (Cart_Item cartItem : cartItemDb.findAll()){
            if (cartItem.getId().equals(id)){
                return cartItem;
            }
        }
        return null;
    }

    @Override
    public void createCartItem(CreateCartItemRequestDTO cartItemDTO){
        Cart_Item cartItemTem = new Cart_Item();
        cartItemTem.setProductId(cartItemDTO.getProductId());

        cartItemTem.setQuantity(cartItemDTO.getQuantity());

        Cart cartTem = cartDb.findById(cartItemDTO.getCartId()).orElse(null);
        cartItemTem.setCartId(cartTem);

        cartItemDb.save(cartItemTem);

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8083/api/catalogue/id/" + cartItemDTO.getProductId();
        ResponseEntity<CatalogueResponseDTO> responseEntity = restTemplate.getForEntity(apiUrl, CatalogueResponseDTO.class);
        CatalogueResponseDTO product = responseEntity.getBody();

        Integer totalTemp = getCartById(cartItemDTO.getCartId()).getTotalPrice();
        getCartById(cartItemDTO.getCartId()).setTotalPrice(totalTemp+(cartItemTem.getQuantity()*product.getPrice()));
    }

    @Override
    public Cart_Item updateQuantity(UUID id, Integer q){
        Cart_Item cartItem = getCartItemById(id);
        if(cartItem!=null){
            cartItem.setQuantity(q);
            cartItemDb.save(cartItem);
            return cartItem;
        }
        return null;
    }
}
