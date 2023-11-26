package com.apapedia.order.dto;

import org.hibernate.validator.constraints.UUID;
import org.mapstruct.Mapper;

import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.Cart_Item;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
}
