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
}
