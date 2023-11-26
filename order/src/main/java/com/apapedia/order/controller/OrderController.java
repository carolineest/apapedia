package com.apapedia.order.controller;

import com.apapedia.order.model.Order;
import com.apapedia.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/chart")
    public Map<String, Integer> orderStatusCount(){
        Map<String, Integer> orderList = new LinkedHashMap<>();
        List<Order> status1 = orderService.getOrderByStatus(1);
        List<Order> status2 = orderService.getOrderByStatus(2);
        List<Order> status3 = orderService.getOrderByStatus(3);
        List<Order> status4 = orderService.getOrderByStatus(4);
        List<Order> status5 = orderService.getOrderByStatus(5);

        orderList.put("Menunggu konfirmasi penjual", status1.size());
        orderList.put("Dikonfirmasi penjual", status2.size());
        orderList.put("Menunggu kurir", status3.size());
        orderList.put("Dalam perjalanan", status4.size());
        orderList.put("Barang diterima", status5.size());

        System.out.println(orderList);

        return orderList;
    }
}
