package com.apapedia.order.restservice;

import com.apapedia.order.dto.request.CreateOrderRequestDTO;

public interface OrderRestService {
    void createOrder(CreateOrderRequestDTO orderDTO);
}
