package com.apapedia.order.repository;

import com.apapedia.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDB extends JpaRepository<Order, UUID> {
    List<Order> findByStatus(Integer status);
}
