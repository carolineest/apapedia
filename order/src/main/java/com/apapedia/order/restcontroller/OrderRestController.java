package com.apapedia.order.restcontroller;

import com.apapedia.order.dto.request.UpdateStatusOrderReqDTO;
import com.apapedia.order.dto.response.ListOrderResDTO;
import com.apapedia.order.jwt.JwtUtils;
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

    @Autowired
    JwtUtils jwtUtils;

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
    public Map<String, Integer> orderStatusCount(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.substring(7);
        UUID idSeller = jwtUtils.getUserIdFromToken(token);

        System.out.println("MASUK GET CHART");
        Map<String, Integer> orderList = new LinkedHashMap<>();
        List<Order> status0 = orderRestService.getOrderByStatusAndSeller(idSeller, 0);
        List<Order> status1 = orderRestService.getOrderByStatusAndSeller(idSeller, 1);
        List<Order> status2 = orderRestService.getOrderByStatusAndSeller(idSeller, 2);
        List<Order> status3 = orderRestService.getOrderByStatusAndSeller(idSeller, 3);
        List<Order> status4 = orderRestService.getOrderByStatusAndSeller(idSeller, 4);
        List<Order> status5 = orderRestService.getOrderByStatusAndSeller(idSeller, 5);

        orderList.put("Menunggu konfirmasi penjual", status0.size());
        orderList.put("Dikonfirmasi penjual", status1.size());
        orderList.put("Menunggu kurir", status2.size());
        orderList.put("Dalam perjalanan", status3.size());
        orderList.put("Barang diterima", status4.size());
        orderList.put("Barang selesai", status5.size());

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

    @GetMapping("/updateStatus/{orderId}")
    public ResponseEntity<?> updateOrderStatusReq(@PathVariable("orderId") UUID orderId){
        try{
            Order order = orderRestService.getOrderById(orderId);
            if(order==null){
                return null;
            }
            return new ResponseEntity<>(order, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable("orderId") UUID orderId,
                                               @RequestBody UpdateStatusOrderReqDTO updateStatusOrderReqDTO){
        try{
            Integer newStatus = updateStatusOrderReqDTO.getStatus();
            Order updatedOrder = orderRestService.updateStatusOrder(orderId, newStatus);
            if(updatedOrder == null){
                return null;
            }
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
