package com.apapedia.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.apapedia.order.model.Cart_Item;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCartRequestDTO {
    private UUID userId;
}
