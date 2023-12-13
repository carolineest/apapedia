package com.apapedia.order.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.model.Order;
import com.apapedia.order.model.Order_Item;
import com.apapedia.order.repository.OrderDb;
import com.apapedia.order.repository.OrderItemDb;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderRestServiceImpl implements OrderRestService {
    @Autowired
    private OrderDb orderDb;

    @Autowired
    private OrderItemDb orderItemDb;

    @Override
    public void createOrder(CreateOrderRequestDTO orderDTO){
        Order orderTemp = new Order();
        orderTemp.setCreatedAt(new Date()); 
        orderTemp.setUpdatedAt(new Date());
        orderTemp.setStatus(orderDTO.getStatus());
        orderTemp.setTotalPrice(orderDTO.getTotalPrice()); 
        orderTemp.setCustomer(orderDTO.getCustomer()); 
        orderTemp.setSeller(orderDTO.getSeller());
        orderTemp.setOrder_itemList(new ArrayList<>());
        orderDb.save(orderTemp);
        if (orderDTO.getOrder_itemList() != null && !orderDTO.getOrder_itemList().isEmpty()) {
            for (CreateOrderRequestDTO.Order_ItemDTO orderItem : orderDTO.getOrder_itemList()) {
                Order_Item orderItemTemp = new Order_Item();
                orderItemTemp.setProductId(orderItem.getProductId());
                orderItemTemp.setQuantity(orderItem.getQuantity());
                orderItemTemp.setProductName(orderItem.getProductName());
                orderItemTemp.setProductPrice(orderItem.getProductPrice());
                orderItemTemp.setOrderId(orderTemp);
                orderItemDb.save(orderItemTemp);
                orderTemp.getOrder_itemList().add(orderItemTemp);
            }   
        }
    }
    @Override
    public List<Order> getAllOrder(){
        return orderDb.findAll();
    }

    @Override
    public List<Order> getOrderByStatus(Integer status){
        return orderDb.findByStatus(status);
    }

    @Override
    public List<Order> getOrderByStatusAndSeller(UUID sellerId, Integer status){
        return orderDb.findBySellerAndStatus(sellerId, status);
    }

    @Override
    public List<Order> getOrderByCustId(UUID id){
        List<Order> orderList = orderDb.findByCustomer(id);
        if(orderList == null || orderList.isEmpty()){
            return null;
        }
        return orderList;
    }

    @Override
    public Order getOrderById(UUID orderId){
        for(Order order : getAllOrder()){
            if(order.getId().equals(orderId)){
                return order;
            }
        }
        return null;
    }

    @Override
    public Order updateStatusOrder(UUID orderId, Integer status){
        Order order = getOrderById(orderId);
        if(order != null){
            order.setStatus(status);
            orderDb.save(order);
            return order;
        }
        return null;
    }
}
