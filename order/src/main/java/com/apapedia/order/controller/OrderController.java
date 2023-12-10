package com.apapedia.order.controller;

import com.apapedia.order.dto.response.ListOrderResDTO;
import com.apapedia.order.model.Order;
import com.apapedia.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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


    @GetMapping("/view-all/{custid}")
    public ResponseEntity<?> getOrderByCustId(@PathVariable("custid") UUID custid){
        try{
            List<Order> orderList= orderService.getOrderByCustId(custid);
            if(orderList == null){
                return null;
            }
            return new ResponseEntity<>(new ListOrderResDTO(orderList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
