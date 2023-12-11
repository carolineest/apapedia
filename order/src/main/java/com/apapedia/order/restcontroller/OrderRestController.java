package com.apapedia.order.restcontroller;

import com.apapedia.order.dto.response.ListOrderResDTO;
import com.apapedia.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.restservice.OrderRestService;

import jakarta.validation.Valid;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {
    @Autowired
    private OrderRestService orderRestService;

    @PostMapping(value = "/create")
    public void createOrder(@Valid @RequestBody CreateOrderRequestDTO orderDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            orderRestService.createOrder(orderDTO);
        }
    }

    @GetMapping("/chart")
    public Map<String, Integer> orderStatusCount(){
        System.out.println("MASUK GET CHART");
        Map<String, Integer> orderList = new LinkedHashMap<>();
        List<Order> status1 = orderRestService.getOrderByStatus(1);
        List<Order> status2 = orderRestService.getOrderByStatus(2);
        List<Order> status3 = orderRestService.getOrderByStatus(3);
        List<Order> status4 = orderRestService.getOrderByStatus(4);
        List<Order> status5 = orderRestService.getOrderByStatus(5);

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
            List<Order> orderList= orderRestService.getOrderByCustId(custid);
            if(orderList == null){
                return null;
            }
            return new ResponseEntity<>(new ListOrderResDTO(orderList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
