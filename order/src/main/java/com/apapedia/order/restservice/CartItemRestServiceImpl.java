package com.apapedia.order.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
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
    public void createCartItem(CreateCartItemRequestDTO cartItemDTO){
        Cart_Item cartItemTem = new Cart_Item();
        cartItemTem.setProductId(cartItemDTO.getProductId());

        // Integer totalTemp = cartItem.getCartId().getTotalPrice();
        // cartItem.getCartId().setTotalPrice(totalTemp+(cartItem.getQuantity()*));
        cartItemTem.setQuantity(cartItemDTO.getQuantity());

        Cart cartTem = cartDb.findById(cartItemDTO.getCartId()).orElse(null);
        cartItemTem.setCartId(cartTem);

        cartItemDb.save(cartItemTem);
    }
}
