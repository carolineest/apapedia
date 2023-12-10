package com.apapedia.order.service;

import com.apapedia.order.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getOrderByStatus(Integer status);
    List<Order> getOrderByCustId(UUID id);
}
