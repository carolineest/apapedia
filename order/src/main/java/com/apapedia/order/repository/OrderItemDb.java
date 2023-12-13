package com.apapedia.order.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.order.model.Order_Item;

@Repository
public interface OrderItemDb extends JpaRepository<Order_Item, UUID> {

}
