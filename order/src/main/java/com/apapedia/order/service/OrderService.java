package com.apapedia.order.service;

import com.apapedia.order.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrderByStatus(Integer status);
}
