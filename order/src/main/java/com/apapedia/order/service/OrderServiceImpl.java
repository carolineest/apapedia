package com.apapedia.order.service;

import com.apapedia.order.model.Order;
import com.apapedia.order.repository.OrderDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDb orderDb;

    @Override
    public List<Order> getOrderByStatus(Integer status){
        return orderDb.findByStatus(status);
    }
}
