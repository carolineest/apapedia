package com.apapedia.order.service;

import com.apapedia.order.model.Order;
import com.apapedia.order.repository.OrderDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDB orderDB;

    @Override
    public List<Order> getOrderByStatus(Integer status){
        return orderDB.findByStatus(status);
    }
}
