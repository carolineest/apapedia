package com.apapedia.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderRequestDTO {
    private Integer status;
    private Integer totalPrice;
    private UUID customer;
    private UUID seller;
    private List<Order_ItemDTO> order_itemList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order_ItemDTO {
        private UUID productId;
        private Integer quantity;
        private String productName;
        private Integer productPrice;
    }
}
