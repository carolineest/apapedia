package com.apapedia.order.restservice;

import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRestService {
    void createOrder(CreateOrderRequestDTO orderDTO);
    List<Order> getAllOrder();
    List<Order> getOrderByStatus(Integer status);
    List<Order> getOrderByStatusAndSeller(UUID sellerId, Integer status);
    List<Order> getOrderByCustId(UUID id);

    List<Order> getOrderBySellerId(UUID id);
    Order getOrderById(UUID orderId);
    Order updateStatusOrder(UUID orderId, Integer status);
}
