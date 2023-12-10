package com.apapedia.order.dto.response;

import com.apapedia.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListOrderResDTO {
    private List<Order> listOrder;
}
