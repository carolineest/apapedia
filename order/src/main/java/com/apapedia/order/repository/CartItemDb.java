package com.apapedia.order.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.order.model.Cart_Item;

@Repository
public interface CartItemDb extends JpaRepository<Cart_Item, UUID>{
}
