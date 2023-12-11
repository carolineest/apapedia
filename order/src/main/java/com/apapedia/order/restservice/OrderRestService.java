package com.apapedia.order.restservice;

import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRestService {
    void createOrder(CreateOrderRequestDTO orderDTO);
    List<Order> getOrderByStatus(Integer status);
    List<Order> getOrderByCustId(UUID id);
}
